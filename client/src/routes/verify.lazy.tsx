import { toast } from "@/hooks/use-toast"
import { apiClient } from "@/lib/api"
import usePaymentStore from "@/lib/store/use-payment-store"
import { createLazyFileRoute, useNavigate, useSearch } from "@tanstack/react-router"
import { HttpStatusCode } from "axios"
import { useEffect, useState } from "react"

export const Route = createLazyFileRoute("/verify")({
    component: VerifyPage,
})

const MAX_RETRIES = 3
const RETRY_DELAY = 2000

function VerifyPage() {
    const search: { status: string; tx_ref: string; transaction_id: string } = useSearch({
        from: "/verify",
    })

    const navigate = useNavigate()
    const { setPayment } = usePaymentStore()
    const [retryCount, setRetryCount] = useState(0)

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

            if (res.status === HttpStatusCode.Ok && res.data.status === "successful") {
                setPayment(res.data)
                navigate({ to: "/success" })
                toast({
                    title: "Payment Successful!",
                    description: "Your payment has been processed successfully.",
                })
            }

            // If verification failed and we haven't exceeded max retries
            if (retryCount < MAX_RETRIES) {
                setRetryCount((prev) => prev + 1)
                setTimeout(() => {
                    verifyTransaction(txRef, transactionId)
                }, RETRY_DELAY)
            } else {
                // Max retries reached, payment failed
                toast({
                    title: "Payment Verification Failed",
                    description: "We couldn't verify your payment. Please contact support.",
                    variant: "destructive",
                })
                navigate({ to: "/verify-error" })
            }
        } catch (error) {
            console.error("Verification error:", error)

            if (retryCount < MAX_RETRIES) {
                setRetryCount((prev) => prev + 1)
                setTimeout(() => {
                    verifyTransaction(txRef, transactionId)
                }, RETRY_DELAY)
            } else {
                toast({
                    title: "Verification Failed",
                    description: "We couldn't verify your payment. Please contact support.",
                    variant: "destructive",
                })
                navigate({ to: "/verify-error" })
            }
        }
    }

    return (
        <div className="grid place-items-center w-screen h-screen">
            <div className="flex flex-col items-center gap-6">
                <div className="loader"></div>

                <h1 className="text-2xl text-white/80 text-center">
                    Please wait, while we verify your payment...
                    {retryCount > 0 && (
                        <p className="text-sm mt-2">
                            Verifying payment... Attempt {retryCount + 1} of {MAX_RETRIES + 1}
                        </p>
                    )}
                </h1>
            </div>
        </div>
    )
}
