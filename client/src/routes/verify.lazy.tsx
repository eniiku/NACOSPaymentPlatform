import { toast } from "@/hooks/use-toast"
import { apiClient } from "@/lib/api"
import usePaymentStore from "@/lib/store/use-payment-store"
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
    const { setPayment } = usePaymentStore()

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
                `/payments/verify?transaction_id=${transactionId}&trx_ref=${txRef}`
            )

            if (res.status === HttpStatusCode.Ok) {
                setPayment(res.data)
                navigate({ to: "/success" })
                toast({
                    title: "Payment Successful!",
                    description: "Your payment has been processed successfully.",
                })
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

                <h1 className="text-2xl text-white/80 text-center">
                    Please wait, while we verify your payment...
                </h1>
            </div>
        </div>
    )
}
