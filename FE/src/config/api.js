export const API_BASE_URL = (import.meta.env.VITE_API_URL || "").replace(/\/+$/, "");

export function apiUrl(path) {
  const normalizedPath = String(path || "").replace(/^\/+/, "");
  return `${API_BASE_URL}/${normalizedPath}`;
}

