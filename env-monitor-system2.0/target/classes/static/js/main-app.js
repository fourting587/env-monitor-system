import { fetchCurrentUser, logout, redirectToLogin } from "./auth.js";
import { initMonitor, stopPolling, appendDemo, refreshMetricPage, refreshCards, refreshCharts, initMetricPage } from "./monitor.js";
import { bindUserManagementUi, loadUserTable, resetUserPage } from "./userManagement.js";

async function boot() {
  try {
    const res = await fetchCurrentUser();
    if (!res.success || !res.data) {
      redirectToLogin();
      return;
    }
    const me = res.data;
    const label = document.getElementById("current-user");
    if (label) {
      label.textContent = `${me.displayName || me.username}（${me.role === "ADMIN" ? "管理员" : "用户"}）`;
    }

    const userTabLink = document.querySelector('a[href="/users.html"]');
    if (me.role !== "ADMIN" && userTabLink) {
      userTabLink?.classList.add("hidden");
    }

    const pageMode = document.body.dataset.page || "main";
    if (pageMode === "users" && me.role === "ADMIN") {
      resetUserPage();
      await loadUserTable();
    }
    if (["main", "light", "temperature", "humidity", "pressure", "gps"].includes(pageMode)) {
      await initPage(pageMode);
    }

    document.getElementById("btn-logout")?.addEventListener("click", async () => {
      await logout();
      redirectToLogin();
    });

    document.getElementById("btn-demo")?.addEventListener("click", async () => {
      await appendDemo();
      await refreshPage(pageMode);
    });

    if (me.role === "ADMIN") {
      bindUserManagementUi();
    }
  } catch {
    // nav.activate(TAB_MONITOR); // Commented out as it's no longer needed
  }
}

boot();

async function initPage(pageMode) {
  if (pageMode === "main") {
    await initMonitor();
    return;
  }
  await initMetricPage(pageMode);
}

export async function refreshPage(pageMode) {
  if (pageMode === "main") {
    await refreshCards();
    await refreshCharts();
    return;
  }
  if (pageMode === "users") {
    resetUserPage();
    await loadUserTable();
    return;
  }
  await refreshMetricPage(pageMode);
}
