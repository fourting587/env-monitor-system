axios.defaults.withCredentials = true;

export async function login(username, password) {
  const { data } = await axios.post("/api/auth/login", { username, password });
  return data;
}

export async function logout() {
  const { data } = await axios.post("/api/auth/logout");
  return data;
}

export async function fetchCurrentUser() {
  const { data } = await axios.get("/api/auth/me");
  return data;
}

export function redirectToLogin() {
  window.location.href = "/login.html";
}
