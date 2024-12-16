import { useState } from "react"

import { apiClient } from "@/lib/api"

type DownloadPDFProps = {
    type: "invoice" | "receipt"
    studentRegistrationNo: string
    identifier: string
}

interface UsePDFDownloadReturn {
    downloading: boolean
    error: string | null
    downloadPDF: ({ type, studentRegistrationNo, identifier }: DownloadPDFProps) => Promise<void>
}

export const usePDFDownload = (): UsePDFDownloadReturn => {
    const [downloading, setDownloading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const downloadPDF = async ({ type, studentRegistrationNo, identifier }: DownloadPDFProps) => {
        try {
            setDownloading(true)
            setError(null)

            const response = await apiClient.get(`/${type}s/generate/${identifier}`, {
                responseType: "blob",
                headers: {
                    Accept: "application/pdf",
                },
            })

            // Handle the PDF download
            const blob = new Blob([response.data], { type: "application/pdf" })
            const url = window.URL.createObjectURL(blob)
            const link = document.createElement("a")
            link.href = url
            link.setAttribute("download", `${studentRegistrationNo}-${identifier}-pdf`)
            document.body.appendChild(link)
            link.click()
            link.parentNode?.removeChild(link)
            window.URL.revokeObjectURL(url)

            // TODO: add toast on success & failure
        } catch (error) {
            setError("Error downloading PDF. Please try again.")
            console.error("PDF download error:", error)
        } finally {
            setDownloading(false)
        }
    }

    return { downloading, error, downloadPDF }
}
