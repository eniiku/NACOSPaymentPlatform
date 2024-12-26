import axios from "axios"

const backendApiBaseUrl = import.meta.env.VITE_BACKEND_API_BASE_URL

export const apiClient = axios.create({
    baseURL: backendApiBaseUrl ? `${backendApiBaseUrl}/api/v1` : "http://localhost:8443/api/v1",
    timeout: 30000,
    withCredentials: true,
})
