import { apiClient } from "@/lib/api"
import { createLazyFileRoute, useNavigate, useSearch } from "@tanstack/react-router"
import { HttpStatusCode } from "axios"
import { useEffect } from "react"

export const Route = createLazyFileRoute("/verify")({
    component: VerifyPage,
})

function VerifyPage() {
    const search: { status: string; tx_ref: string; transaction_id: string } = useSearch({
        from: "/verify",
    })
    const navigate = useNavigate()

    useEffect(() => {
        const txRef = search?.tx_ref
        const transactionId = search.transaction_id

        if (txRef && transactionId) {
            verifyTransaction(txRef, transactionId)
        } else {
            navigate({ to: "/verify-error" })
        }
    }, [search])

    const verifyTransaction = async (txRef: string, transactionId: string) => {
        try {
            // Replace with your actual backend API endpoint
            const res = await apiClient.get(
                `/payments/verify?transaction_id=${transactionId}&invoice_no=${txRef}`
                // `http://18.175.120.168:8443/api/v1/payments/verify?transaction_id=${transactionId}&invoice_id=${txRef}`
            )

            if (res.status === HttpStatusCode.Ok) {
                navigate({ to: "/success" })
            } else {
                throw new Error("Transaction verifiction failed")
            }
        } catch (error) {
            console.error("Verification error:", error)
        }
    }

    return (
        <div className="grid place-items-center w-screen h-screen">
            <div className="flex flex-col items-center gap-6">
                <div className="loader"></div>

                <h1 className="text-2xl text-white/80 text-center">Verifying...</h1>
            </div>
        </div>
    )
}
