const TAB_MONITOR = "monitor";
const TAB_USERS = "users";

export function initNavigation(onTab) {
  const tabs = document.querySelectorAll("[data-tab]");
  const panels = document.querySelectorAll("[data-panel]");

  function activate(name) {
    tabs.forEach((btn) => {
      btn.classList.toggle("active", btn.dataset.tab === name);
    });
    panels.forEach((p) => {
      p.classList.toggle("hidden", p.dataset.panel !== name);
    });
    if (typeof onTab === "function") {
      onTab(name);
    }
  }

  tabs.forEach((btn) => {
    btn.addEventListener("click", () => activate(btn.dataset.tab));
  });

  return { activate };
}

export { TAB_MONITOR, TAB_USERS };
