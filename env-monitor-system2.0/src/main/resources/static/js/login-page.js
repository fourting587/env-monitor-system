import { login } from "./auth.js";

const form = document.getElementById("login-form");
const err = document.getElementById("login-error");

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  err.textContent = "";
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value;
  try {
    const res = await login(username, password);
    if (res.success) {
      window.location.href = "/main.html";
      return;
    }
    err.textContent = res.message || "登录失败，请重试";
  } catch (ex) {
    const msg = ex.response?.data?.message || ex.response?.data?.error || ex.message || "网络错误";
    err.textContent = msg;
  }
});
