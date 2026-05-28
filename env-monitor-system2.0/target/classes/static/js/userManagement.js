let userPage = 0;
const pageSize = 8;

function esc(s) {
  const d = document.createElement("div");
  d.textContent = s ?? "";
  return d.innerHTML;
}

export async function loadUserTable() {
  const tbody = document.querySelector("#user-table tbody");
  const pageLabel = document.getElementById("user-page-label");
  if (!tbody) return;

  const { data } = await axios.get("/api/users", {
    params: { page: userPage, size: pageSize },
  });
  if (!data.success || !data.data) return;
  const pg = data.data;
  const rows = pg.content || [];
  tbody.innerHTML = rows
    .map(
      (u) => `
    <tr data-id="${u.id}">
      <td>${u.id}</td>
      <td>${esc(u.username)}</td>
      <td>${esc(u.displayName || "")}</td>
      <td><span class="badge ${u.role === "ADMIN" ? "badge-admin" : "badge-user"}">${u.role === "ADMIN" ? "管理员" : "用户"}</span></td>
      <td class="row-actions">
        <button type="button" class="btn btn-soft btn-edit" data-id="${u.id}">编辑</button>
        <button type="button" class="btn btn-primary-outline btn-del" data-id="${u.id}">删除</button>
      </td>
    </tr>`
    )
    .join("");

  if (pageLabel) {
    pageLabel.textContent = `第 ${userPage + 1} / ${Math.max(pg.totalPages, 1)} 页（共 ${pg.totalElements} 条）`;
  }

  tbody.querySelectorAll(".btn-edit").forEach((btn) => {
    btn.addEventListener("click", () => openEdit(Number(btn.dataset.id), rows));
  });
  tbody.querySelectorAll(".btn-del").forEach((btn) => {
    btn.addEventListener("click", () => removeUser(Number(btn.dataset.id)));
  });
}

function openEdit(id, rows) {
  const u = rows.find((x) => x.id === id);
  if (!u) return;
  const backdrop = document.getElementById("user-modal");
  if (!backdrop) return;
  backdrop.classList.remove("hidden");
  document.getElementById("edit-id").value = id;
  document.getElementById("edit-username").value = u.username;
  document.getElementById("edit-display").value = u.displayName || "";
  document.getElementById("edit-password").value = "";
  document.getElementById("edit-role").value = u.role;
}

function closeModal() {
  document.getElementById("user-modal")?.classList.add("hidden");
  document.getElementById("user-create-modal")?.classList.add("hidden");
}

async function removeUser(id) {
  if (!confirm("确定删除该用户？")) return;
  const { data } = await axios.delete(`/api/users/${id}`);
  if (data.success) {
    await loadUserTable();
  }
}

export async function submitEdit(e) {
  e.preventDefault();
  const id = document.getElementById("edit-id").value;
  const password = document.getElementById("edit-password").value;
  const displayName = document.getElementById("edit-display").value;
  const role = document.getElementById("edit-role").value;
  const body = { displayName, role };
  if (password && password.length >= 4) {
    body.password = password;
  }
  const { data } = await axios.put(`/api/users/${id}`, body);
  if (data.success) {
    closeModal();
    await loadUserTable();
  }
}

export async function submitCreate(e) {
  e.preventDefault();
  const username = document.getElementById("create-username").value.trim();
  const password = document.getElementById("create-password").value;
  const displayName = document.getElementById("create-display").value.trim();
  const role = document.getElementById("create-role").value;
  const { data } = await axios.post("/api/users", {
    username,
    password,
    displayName: displayName || username,
    role,
  });
  if (data.success) {
    closeModal();
    userPage = 0;
    await loadUserTable();
  }
}

export function bindUserManagementUi() {
  document.getElementById("btn-prev")?.addEventListener("click", () => {
    if (userPage > 0) {
      userPage -= 1;
      loadUserTable();
    }
  });
  document.getElementById("btn-next")?.addEventListener("click", () => {
    userPage += 1;
    loadUserTable();
  });
  document.getElementById("btn-refresh-users")?.addEventListener("click", () => loadUserTable());
  document.getElementById("btn-open-create")?.addEventListener("click", () => {
    document.getElementById("user-create-modal")?.classList.remove("hidden");
    document.getElementById("create-username").value = "";
    document.getElementById("create-password").value = "";
    document.getElementById("create-display").value = "";
    document.getElementById("create-role").value = "USER";
  });
  document.getElementById("btn-cancel-edit")?.addEventListener("click", closeModal);
  document.getElementById("btn-cancel-create")?.addEventListener("click", closeModal);
  document.getElementById("form-edit-user")?.addEventListener("submit", submitEdit);
  document.getElementById("form-create-user")?.addEventListener("submit", submitCreate);
}

export function resetUserPage() {
  userPage = 0;
}
