import { fetchCurrentUser, logout, redirectToLogin } from "./auth.js";
import { initNavigation, TAB_MONITOR, TAB_USERS } from "./navigation.js";
import { initMonitor, stopPolling, appendDemo } from "./monitor.js";
import {
  bindUserManagementUi,
  loadUserTable,
  resetUserPage,
} from "./userManagement.js";

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

    const userTabBtn = document.querySelector('[data-tab="users"]');
    if (me.role !== "ADMIN" && userTabBtn) {
      userTabBtn.classList.add("hidden");
    }

    const nav = initNavigation(async (tab) => {
      if (tab === TAB_MONITOR) {
        stopPolling();
        await initMonitor();
      } else {
        stopPolling();
      }
      if (tab === TAB_USERS && me.role === "ADMIN") {
        resetUserPage();
        await loadUserTable();
      }
    });

    document.getElementById("btn-logout")?.addEventListener("click", async () => {
      await logout();
      redirectToLogin();
    });

    document.getElementById("btn-demo")?.addEventListener("click", async () => {
      await appendDemo();
    });

    if (me.role === "ADMIN") {
      bindUserManagementUi();
    }

    nav.activate(TAB_MONITOR);
  } catch {
    redirectToLogin();
  }
}

boot();
