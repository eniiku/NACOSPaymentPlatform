import axios from "axios"

export const apiClient = axios.create({
    // baseURL: import.meta.env.BACKEND_API_BASE_URL || "http://localhost:8443/api/v1",
    baseURL: "http://3.8.182.85:8443/api/v1",
    timeout: 30000,
    withCredentials: true,
})
