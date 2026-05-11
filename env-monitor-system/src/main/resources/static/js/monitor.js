let charts = {};
let pollTimer;

function theme() {
  return {
    textColor: "#e7ecf3",
    axisLine: "#2d3a4d",
    splitLine: "rgba(45, 58, 77, 0.45)",
  };
}

function lineOption(title, seriesName, times, values, color) {
  const t = theme();
  return {
    backgroundColor: "transparent",
    title: { text: title, left: 0, top: 4, textStyle: { fontSize: 13, color: t.textColor } },
    tooltip: { trigger: "axis" },
    grid: { left: 48, right: 16, top: 40, bottom: 28 },
    xAxis: {
      type: "category",
      data: times,
      axisLine: { lineStyle: { color: t.axisLine } },
      axisLabel: { color: "#8b9bb4", fontSize: 10 },
    },
    yAxis: {
      type: "value",
      splitLine: { lineStyle: { color: t.splitLine } },
      axisLabel: { color: "#8b9bb4", fontSize: 10 },
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
  const { data } = await axios.get(`/api/sensors/series/${type}`, { params: { limit: 48 } });
  if (!data.success || !data.data) return;
  const rows = data.data;
  const times = rows.map((r) => formatTime(r.recordedAt));
  const values = rows.map((r) => r[valueKey]);
  const chart = ensureChart(chartId);
  if (chart) {
    chart.setOption(lineOption(title, unit, times, values, color), true);
  }
}

export async function refreshCharts() {
  await Promise.all([
    loadSeries("light", "lux", "光照", "lux", "chart-light", "#fbbf24"),
    loadSeries("temperature", "celsius", "温度", "°C", "chart-temperature", "#38bdf8"),
    loadSeries("humidity", "percent", "湿度", "%", "chart-humidity", "#34d399"),
    loadSeries("pressure", "hpa", "气压", "hPa", "chart-pressure", "#a78bfa"),
  ]);
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
  }, 10000);
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
