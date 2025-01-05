import { HttpStatusCode } from "axios"
import { useEffect, useRef, useState } from "react"

import { toast } from "@/hooks/use-toast"
import { apiClient } from "@/lib/api"
import usePaymentStore from "@/lib/store/use-payment-store"
import { createLazyFileRoute, useNavigate, useSearch } from "@tanstack/react-router"

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

    // Use refs to track verification state across re-renders
    const isVerifyingRef = useRef(false)
    const hasRedirectedRef = useRef(false)

    useEffect(() => {
        const txRef = search?.tx_ref
        const transactionId = search?.transaction_id

        if (!txRef) {
            navigate({ to: "/verify-error" })
            return
        }

        // Prevent multiple verification attempts
        if (isVerifyingRef.current || hasRedirectedRef.current) {
            return
        }

        let timeoutId: NodeJS.Timeout

        const attemptVerification = async () => {
            if (hasRedirectedRef.current) return // Don't verify if already redirected

            isVerifyingRef.current = true

            try {
                const res = await apiClient.get(
                    `/payments/verify?transaction_id=${transactionId}&trx_ref=${txRef}`
                )

                if (res.status === HttpStatusCode.Ok && res.data.status === "successful") {
                    hasRedirectedRef.current = true // Mark as redirected
                    setPayment(res.data)
                    navigate({ to: "/success" })
                    toast({
                        title: "Payment Successful!",
                        description: "Your payment has been processed successfully.",
                    })
                    return
                }

                // Only retry if we haven't redirected
                if (!hasRedirectedRef.current && retryCount < MAX_RETRIES) {
                    setRetryCount((prev) => prev + 1)
                    timeoutId = setTimeout(() => {
                        isVerifyingRef.current = false
                        attemptVerification()
                    }, RETRY_DELAY)
                } else if (!hasRedirectedRef.current) {
                    hasRedirectedRef.current = true // Mark as redirected
                    toast({
                        title: "Verification Failed",
                        description: "We couldn't verify your payment. Please contact support.",
                        variant: "destructive",
                    })
                    navigate({ to: "/verify-error" })
                }
            } catch (error) {
                console.error("Verification error:", error)

                // Only retry on error if we haven't redirected
                if (!hasRedirectedRef.current && retryCount < MAX_RETRIES) {
                    setRetryCount((prev) => prev + 1)
                    timeoutId = setTimeout(() => {
                        isVerifyingRef.current = false
                        attemptVerification()
                    }, RETRY_DELAY)
                } else if (!hasRedirectedRef.current) {
                    hasRedirectedRef.current = true // Mark as redirected
                    toast({
                        title: "Verification Failed",
                        description: "We couldn't verify your payment. Please contact support.",
                        variant: "destructive",
                    })
                    navigate({ to: "/verify-error" })
                }
            }
        }

        attemptVerification()

        // Cleanup function
        return () => {
            if (timeoutId) {
                clearTimeout(timeoutId)
            }
            isVerifyingRef.current = false
        }
    }, [search]) // Only depend on search params

    // Prevent re-rendering verification UI if we've redirected
    if (hasRedirectedRef.current) {
        return null
    }

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
