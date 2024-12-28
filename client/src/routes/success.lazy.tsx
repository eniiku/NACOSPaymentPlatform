import { createLazyFileRoute } from "@tanstack/react-router"

import { Button } from "@/components/ui/button"
import { DownloadIcon } from "@radix-ui/react-icons"
import { usePDFDownload } from "@/hooks/use-pdf-download"
import usePaymentStore from "@/lib/store/use-payment-store"
import { formatDate, formatReadableString } from "@/lib/utils"

export const Route = createLazyFileRoute("/success")({
    component: SuccessPage,
})

function SuccessPage() {
    const { payment } = usePaymentStore()
    const { downloading, downloadPDF } = usePDFDownload()

    const handleDownloadReceipt = () =>
        downloadPDF({
            type: "receipt",
            studentRegistrationNo: payment?.receipt.payment.student.registrationNumber as string,
            identifier: payment?.receipt.id.toString() as string,
        })

    return (
        <main className="md:flex gap-4 px-4 pt-11 pb-6 justify-evenly  md:px-12 md:py-16 xl:px-36 lg:pt-24">
            <div className="hidden md:flex flex-col justify-between mb-6">
                <div className="space-y-2 md:space-y-4 text-center md:text-justify px-3 text-white max-w-[482px]">
                    <h1 className="font-bold text-xl md:text-4xl tracking-tighter uppercase">
                        Hello there! Pay Your NACOS Dues Easily and Securely here
                    </h1>
                    <p className="text-sm md:text-lg text-left text-white/70">
                        Welcome to the official payment portal for NACOS dues. Kindly provide
                        accurate information to ensure a successful payment.
                    </p>
                </div>

                <div className="hidden md:flex items-center text-white gap-3">
                    {/* TODO: create optimized image component */}
                    <img
                        src="/logo.svg"
                        alt=""
                        width={48}
                        height={48}
                        className="md:w-12 md:h-12"
                    />

                    <div className="">
                        <h3 className="font-bold text-xl tracking-tighter">NACOS, FUNAAB</h3>
                        <p className="text-lg text-white/70">
                            Nigeria Association of Computing Students
                        </p>
                    </div>
                </div>
            </div>

            <section className="bg-white rounded-xl py-7 px-3 w-full md:max-w-[400px] stamped-border relative z-10">
                <div className="space-y-6">
                    <img
                        src="/circle-check.svg"
                        width={84}
                        height={84}
                        alt=""
                        className="mx-auto"
                    />

                    <div className="space-y-2 text-center max-w-[300px] mx-auto">
                        <h2 className="font-bold text-18px lg:text-xl text-black/90">
                            Payment Successful
                        </h2>

                        <p className="text-base text-black/60">
                            You&apos;ve successfully paid your NACOS Dues. Be sure to download your
                            receipt and get it stamped
                        </p>
                    </div>
                </div>

                {/* border decorator */}

                <div className="w-full h-[1px] my-10 rounded-dashes"></div>

                <div>
                    <h4 className="font-semibold text-sm">Your NACOS Due Payment Details</h4>

                    <ul className="mt-2.5 mb-7 text-sm text-black/50 py-3.5 px-2.5 rounded-lg bg-[#F8F8F8]">
                        <li className="p-1 flex items-center justify-between">
                            <p className="">Receipt Number</p>
                            <h3 className="font-medium text-black/90">
                                #{payment?.receipt.receiptNumber}
                            </h3>
                        </li>
                        <li className="p-1 flex items-center justify-between">
                            <p className="">Payment Date</p>
                            <h3 className="font-medium text-black/90">
                                {/* TODO: work on date formatter */}
                                {formatDate(payment?.receipt.payment.paymentDate as string)}
                            </h3>
                        </li>
                        <li className="p-1 flex items-center justify-between">
                            <p className="">Payment Method</p>
                            <h3 className="font-medium text-black/90">
                                {formatReadableString(
                                    payment?.receipt.payment.paymentMethod as string
                                )}
                            </h3>
                        </li>
                        <li className="p-1 flex items-center justify-between">
                            <p className="">Payment Amount</p>
                            <h3 className="font-medium text-black/90">
                                {payment?.receipt.payment.amountPaid}
                            </h3>
                        </li>
                    </ul>

                    <div className="space-y-1">
                        <Button
                            disabled={downloading}
                            onClick={handleDownloadReceipt}
                            className="h-auto py-[18px] w-full bg-custom-green hover:bg-custom-green/95 text-white font-bold text-base"
                        >
                            {downloading ? "Loading..." : "Download Receipt"}
                            <span className="ml-3">
                                <DownloadIcon width={24} height={24} strokeWidth={4} />
                            </span>
                        </Button>
                        <p className="w-full h-auto text-custom-green text-sm text-center py-1">
                            Or check your email
                        </p>
                    </div>
                </div>
            </section>

            <div className="md:hidden flex items-center gap-3 px-3 text-white mt-16">
                {/* TODO: create optimized image component */}
                <img src="/logo.svg" alt="" width={32} height={32} />

                <div className="space-y-0.5">
                    <h3 className="font-bold">NACOS, FUNAAB</h3>
                    <p className="font-medium text-sm text-white/70">
                        Nigeria Association of Computing Students
                    </p>
                </div>
            </div>
        </main>
    )
}
