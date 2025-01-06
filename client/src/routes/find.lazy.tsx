import { createLazyFileRoute } from "@tanstack/react-router"
import { useForm } from "react-hook-form"

import { Button } from "@/components/ui/button"
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { LEVELS } from "@/lib/data"
import { z } from "zod"
import { findFormSchema } from "@/lib/schema"
import { zodResolver } from "@hookform/resolvers/zod"
import { apiClient } from "@/lib/api"
import { toast } from "@/hooks/use-toast"
import { useState } from "react"
import axios from "axios"
import LoadingSpinner from "@/components/util/loading-spinner"
import { Download } from "lucide-react"
import { usePDFDownload } from "@/hooks/use-pdf-download"

export const Route = createLazyFileRoute("/find")({
    component: FindPage,
})

function FindPage() {
    const [isLoading, setIsLoading] = useState<boolean>(false)
    const [paymentDetails, setPaymentDetails] = useState<string>("")
    const [registrationNo, studentRegistrationNo] = useState<string>("")
    const [error, setError] = useState(false)

    const { downloading, downloadPDF } = usePDFDownload()

    const form = useForm<z.infer<typeof findFormSchema>>({
        resolver: zodResolver(findFormSchema),
        defaultValues: {
            registrationNo: "",
        },
    })

    const onSubmit = async (values: z.infer<typeof findFormSchema>) => {
        setIsLoading(true)
        studentRegistrationNo(values.registrationNo)
        try {
            const detailsResponse = await apiClient.get<string>(
                `/payments/retrieve-details?reg_no=${values.registrationNo}`
            )

            const txRef = detailsResponse.data
            if (!txRef) throw new Error("No transaction reference found")

            const verificationResponse = await apiClient.get(
                `/payments/verify-by-reference?tx_ref=${txRef}`
            )

            setPaymentDetails(verificationResponse.data)

            toast({
                title: "Payment verified successfully",
                description: "Your payment details have been retrieved.",
            })
        } catch (error) {
            setError(true)
            if (axios.isAxiosError(error)) {
                const errorMessage = error.response?.data?.message || error.message
                toast({
                    variant: "destructive",
                    title: "Verification Failed",
                    description: errorMessage,
                })
            } else {
                toast({
                    variant: "destructive",
                    title: "Unexpected Error",
                    description: "An unexpected error occurred. Please try again.",
                })
            }
            console.error("[Payment Verification Error]: ", error)
        } finally {
            setIsLoading(false)
        }
    }

    const handleDownloadReceipt = () =>
        downloadPDF({
            type: "receipt",
            studentRegistrationNo: registrationNo as string,
            identifier: paymentDetails as string,
        })

    return (
        <div className="min-h-[90svh] text-white flex flex-col justify-start items-center pt-14 md:pt-20 relative">
            <img
                src="/images/overlay.webp"
                alt=""
                className="absolute left-0 bottom-0 z-10 w-[240px] md:w-auto"
                width={400}
            />

            <div className="space-y-12">
                <div className="space-y-2 md:space-y-4 text-white max-w-[620px]">
                    <h1 className="font-bold text-xl md:text-4xl tracking-tighter">
                        Couldn't generate your NACOS Receipt? <br /> Retrieve it here
                    </h1>
                    <p className="text-sm  md:text-lg text-white/70">
                        This one's on us, mistakes happen. Enter your matric number and current
                        level to retrieve your receipt.
                    </p>
                </div>

                {error ? (
                    <div className="w-full bg-white border-[12px] border-white/15 p-3 max-w-[780px] rounded-md space-y-16 text-black">
                        <div className="flex justify-between">
                            <div className="flex gap-4 items-start">
                                <img
                                    src="/check-failed.svg"
                                    className="w-8 h-8 md:w-auto md:h-auto"
                                    width={52}
                                    height={52}
                                    alt=""
                                />
                                <div className="">
                                    <h3 className="text-lg md:text-2xl font-bold text-black/90">
                                        Sorry, couldn't find receipt
                                    </h3>
                                    <p className="text-sm md:text-lg text-black/60">
                                        Check your matric number and level to see if it's correct
                                    </p>
                                </div>
                            </div>

                            <div></div>
                        </div>

                        <Button
                            onClick={() => setError(false)}
                            className="h-auto py-4 px-16 md:py-4 mt-6 rounded-md font-bold text-sm md:text-base flex items-center gap-2.5 bg-custom-green"
                        >
                            Try Again
                        </Button>
                    </div>
                ) : paymentDetails ? (
                    <div className="w-full bg-white border-[12px] border-white/15 p-3 max-w-[780px] rounded-md space-y-16 text-black">
                        <div className="flex justify-between">
                            <div className="flex gap-1 md:gap-4 items-start">
                                <img
                                    src="/check.svg"
                                    className="w-7 h-7 md:w-auto md:h-auto"
                                    width={48}
                                    height={48}
                                    alt=""
                                />
                                <div className="">
                                    <h3 className="text-lg md:text-2xl font-bold text-black/90">
                                        Receipt Found
                                    </h3>
                                    <p className="text-sm md:text-lgtext-black/60">
                                        You can download your receipt here
                                    </p>
                                </div>
                            </div>

                            <div></div>
                        </div>

                        <Button
                            onClick={handleDownloadReceipt}
                            disabled={downloading}
                            className="h-auto py-4 px-16 md:py-4 mt-6 rounded-md font-bold text-sm md:text-base flex items-center gap-2.5 bg-custom-green min-w-[238px]"
                        >
                            {!downloading ? (
                                <>
                                    Download PDF{" "}
                                    <span>
                                        <Download width={20} height={20} />
                                    </span>
                                </>
                            ) : (
                                <LoadingSpinner />
                            )}
                        </Button>
                    </div>
                ) : (
                    <Form {...form}>
                        <form
                            onSubmit={form.handleSubmit(onSubmit, (errors) => {
                                console.log("Form validation errors:", errors)
                            })}
                            className="w-full bg-white border-[12px] border-white/15 p-3 max-w-[780px] rounded-md space-y-6 text-black"
                        >
                            <div className="space-y-3 md:space-y-4">
                                <FormField
                                    control={form.control}
                                    name="registrationNo"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormControl>
                                                <Input
                                                    placeholder="Matric / Jamb Number"
                                                    className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    {...field}
                                                />
                                            </FormControl>
                                            <FormDescription />
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />

                                <FormField
                                    control={form.control}
                                    name="level"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormControl>
                                                <Select
                                                    onValueChange={field.onChange}
                                                    defaultValue={field.value}
                                                >
                                                    <SelectTrigger className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium">
                                                        <SelectValue placeholder="Select Level" />
                                                    </SelectTrigger>
                                                    <SelectContent>
                                                        {LEVELS.map((level) => (
                                                            <SelectItem
                                                                key={level.key}
                                                                value={level.key}
                                                                className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium uppercase"
                                                            >
                                                                {level.name}
                                                            </SelectItem>
                                                        ))}
                                                    </SelectContent>
                                                </Select>
                                            </FormControl>
                                            <FormDescription />
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                            </div>

                            <Button
                                type="submit"
                                disabled={isLoading}
                                className="h-auto w-full py-3 md:py-4 mt-6 rounded-md font-bold text-sm md:text-base flex items-center gap-2.5 bg-custom-green"
                            >
                                {!isLoading ? (
                                    <>
                                        RETRIEVE MY RECEIPT{" "}
                                        <span>
                                            <img
                                                src="/arrow-right.svg"
                                                alt=""
                                                width={20}
                                                height={20}
                                            />
                                        </span>
                                    </>
                                ) : (
                                    <LoadingSpinner />
                                )}
                            </Button>
                        </form>
                    </Form>
                )}
            </div>
        </div>
    )
}
