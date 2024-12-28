import axios from "axios"

export const apiClient = axios.create({
    baseURL: "/api/v1",
    timeout: 30000,
    withCredentials: true,
    headers: {
        "Content-Type": "application/json",
    },
})
