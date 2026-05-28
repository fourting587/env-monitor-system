let charts = {};
let pollTimer;

function theme() {
  return {
    textColor: "#eef4ff",
    axisLine: "rgba(255,255,255,0.15)",
    splitLine: "rgba(255,255,255,0.08)",
  };
}

function lineOption(title, seriesName, times, values, color) {
  const t = theme();
  return {
    backgroundColor: "transparent",
    title: { text: title, left: 0, top: 10, textStyle: { fontSize: 12, color: t.textColor } },
    tooltip: { trigger: "axis" },
    grid: { left: 42, right: 16, top: 40, bottom: 28 },
    xAxis: {
      type: "category",
      data: times,
      axisLine: { lineStyle: { color: t.axisLine } },
      axisLabel: { color: "#9fb5d3", fontSize: 10 },
    },
    yAxis: {
      type: "value",
      splitLine: { lineStyle: { color: t.splitLine } },
      axisLabel: { color: "#9fb5d3", fontSize: 10 },
    },
    series: [
      {
        name: seriesName,
        type: "line",
        smooth: true,
        symbol: "circle",
        symbolSize: 6,
        lineStyle: { width: 2, color },
        areaStyle: { color: color + "33" },
        data: values,
      },
    ],
  };
}

function ensureChart(domId) {
  const el = document.getElementById(domId);
  if (!el) return null;
  if (!charts[domId]) {
    charts[domId] = echarts.init(el);
    window.addEventListener("resize", () => charts[domId]?.resize());
  }
  return charts[domId];
}

function formatTime(iso) {
  if (!iso) return "";
  const d = new Date(iso);
  const hh = String(d.getHours()).padStart(2, "0");
  const mm = String(d.getMinutes()).padStart(2, "0");
  const ss = String(d.getSeconds()).padStart(2, "0");
  return `${hh}:${mm}:${ss}`;
}

async function loadSeries(type, valueKey, title, unit, chartId, color) {
  const { data } = await axios.get(`/api/sensors/series/${type}`, { params: { limit: 36 } });
  if (!data.success || !data.data) return;
  const rows = data.data;
  const times = rows.map((r) => formatTime(r.recordedAt));
  const values = rows.map((r) => r[valueKey]);
  const chart = ensureChart(chartId);
  if (chart) {
    chart.setOption(lineOption(title, unit, times, values, color), true);
  }
}

export async function refreshCards() {
  const { data } = await axios.get("/api/sensors/latest");
  if (!data.success || !data.data) return;
  const s = data.data;
  const set = (id, text) => {
    const el = document.getElementById(id);
    if (el) el.textContent = text;
  };
  set("val-light", s.lightLux != null ? `${s.lightLux.toFixed(1)} lux` : "—");
  set("val-temp", s.temperatureCelsius != null ? `${s.temperatureCelsius.toFixed(1)} °C` : "—");
  set("val-hum", s.humidityPercent != null ? `${s.humidityPercent.toFixed(1)} %` : "—");
  set("val-press", s.pressureHpa != null ? `${s.pressureHpa.toFixed(1)} hPa` : "—");
  if (s.gpsLatitude != null && s.gpsLongitude != null) {
    set("val-gps", `${s.gpsLatitude.toFixed(5)}, ${s.gpsLongitude.toFixed(5)}`);
  } else {
    set("val-gps", "—");
  }
  set("meta-light", s.lightAt ? formatTime(s.lightAt) : "");
  set("meta-temp", s.temperatureAt ? formatTime(s.temperatureAt) : "");
  set("meta-hum", s.humidityAt ? formatTime(s.humidityAt) : "");
  set("meta-press", s.pressureAt ? formatTime(s.pressureAt) : "");
  set("meta-gps", s.gpsAt ? formatTime(s.gpsAt) : "");
}

export async function appendDemo() {
  const { data } = await axios.post("/api/sensors/demo");
  if (data.success) {
    await refreshCards();
    await refreshCharts();
  }
}

export function startPolling() {
  stopPolling();
  pollTimer = setInterval(() => {
    refreshCards().catch(() => {});
    refreshCharts().catch(() => {});
  }, 5000);
}

export function stopPolling() {
  if (pollTimer) {
    clearInterval(pollTimer);
    pollTimer = null;
  }
}

export async function initMonitor() {
  await refreshCards();
  await refreshCharts();
  startPolling();
}

export async function refreshCharts() {
  await Promise.all([
    loadSeries("LIGHT", "lux", "光照趋势", "lux", "chart-light", "#5dd8ff"),
    loadSeries("TEMPERATURE", "celsius", "温度变化", "°C", "chart-temperature", "#ffb86c"),
    loadSeries("HUMIDITY", "percent", "湿度变化", "%", "chart-humidity", "#6ee7b7"),
    loadSeries("PRESSURE", "hpa", "气压变化", "hPa", "chart-pressure", "#a78bfa"),
  ]);
}

export async function refreshMetricPage(pageMode) {
  switch (pageMode) {
    case "light":
      await loadSeries("LIGHT", "lux", "光照趋势", "lux", "chart-light", "#5dd8ff");
      break;
    case "temperature":
      await loadSeries("TEMPERATURE", "celsius", "温度变化", "°C", "chart-temperature", "#ffb86c");
      break;
    case "humidity":
      await loadSeries("HUMIDITY", "percent", "湿度变化", "%", "chart-humidity", "#6ee7b7");
      break;
    case "pressure":
      await loadSeries("PRESSURE", "hpa", "气压变化", "hPa", "chart-pressure", "#a78bfa");
      break;
    case "gps":
      await refreshGpsPage();
      break;
    default:
      break;
  }
}

export async function initMetricPage(pageMode) {
  await refreshMetricPage(pageMode);
  startPollingPage(pageMode);
}

export function startPollingPage(pageMode) {
  stopPolling();
  pollTimer = setInterval(() => {
    refreshMetricPage(pageMode).catch(() => {});
  }, 5000);
}

async function refreshGpsPage() {
  const { data } = await axios.get("/api/sensors/latest");
  if (!data.success || !data.data) return;
  const s = data.data;
  const set = (id, text) => {
    const el = document.getElementById(id);
    if (el) el.textContent = text;
  };
  set("val-gps", s.gpsLatitude != null && s.gpsLongitude != null ? `${s.gpsLatitude.toFixed(5)}, ${s.gpsLongitude.toFixed(5)}` : "—");
  set("meta-gps", s.gpsAt ? formatTime(s.gpsAt) : "");
  const historyEl = document.getElementById("gps-history");
  if (historyEl) {
    historyEl.innerHTML = `<div class="gps-row">最新定位：${s.gpsLatitude != null && s.gpsLongitude != null ? `${s.gpsLatitude.toFixed(5)}, ${s.gpsLongitude.toFixed(5)}` : "—"}</div>`;
  }
}
