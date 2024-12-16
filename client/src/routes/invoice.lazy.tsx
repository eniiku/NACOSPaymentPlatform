import { z } from "zod"
import { useState } from "react"
import { HttpStatusCode } from "axios"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { createLazyFileRoute } from "@tanstack/react-router"
import { ChevronDownIcon, ExclamationTriangleIcon } from "@radix-ui/react-icons"

import { Button } from "@/components/ui/button"
import {
    Form,
    FormField,
    FormItem,
    FormControl,
    FormDescription,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { userFormSchema } from "@/lib/schema"
import {
    Select,
    SelectTrigger,
    SelectValue,
    SelectContent,
    SelectItem,
} from "@/components/ui/select"
import useInvoiceStore from "@/lib/store/useInvoiceStore"
import { formatDate } from "@/lib/utils"
import { apiClient } from "@/lib/api"
import { usePDFDownload } from "@/hooks/use-pdf-download"

export const Route = createLazyFileRoute("/invoice")({
    component: InvoicePage,
})

function InvoicePage() {
    const { invoice } = useInvoiceStore()
    const { downloading, downloadPDF } = usePDFDownload()

    const [isSubmitLoading, setIsSubmitLoading] = useState(false)

    const firstName = invoice?.student.name.split(" ")[0] as string
    const lastName = invoice?.student.name.split(" ")[1] as string
    const email = invoice?.student.email as string
    const level = invoice?.student.level.key
    const program = invoice?.student.program.key
    const phoneNumber = invoice?.student.phoneNumber as string
    const registrationNumber = invoice?.student.registrationNumber as string

    const form = useForm<z.infer<typeof userFormSchema>>({
        resolver: zodResolver(userFormSchema),
        values: {
            firstName,
            lastName,
            emailAddress: email,
            registrationNo: registrationNumber,
            level: level!,
            program: program!,
            phoneNo: phoneNumber,
        },
    })

    // implement logic to alert users of loading state

    function onSubmit() {
        // TODO: REFACTOR
        setIsSubmitLoading(true)
        apiClient
            .post(`/payments/initialize?invoice_no=${invoice?.invoiceNumber}`)
            .then((res) => {
                console.log(res)
                const paymentGatewayUrl = res.data?.paymentUrl
                if (res.status === HttpStatusCode.Ok) window.location.href = paymentGatewayUrl
                setIsSubmitLoading(false)
            })
            .catch((err) => {
                console.log("[ERROR]: ", err)
            })
            .finally(() => setIsSubmitLoading(false))
    }

    const handleDownloadInvoice = () =>
        downloadPDF({
            type: "invoice",
            studentRegistrationNo: registrationNumber,
            identifier: invoice?.invoiceNumber as string,
        })

    return (
        <>
            {/* OVERLAY */}
            <div className="fixed inset-0 z-50 bg-black/80 data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 pointer-events-none"></div>

            {/* INVOICE PAGE */}
            <div className="fixed inset-3.5 z-50 rounded-lg border-4 border-white/15 bg-white p-6 md:w-[70vw] lg:w-[50vw] md:left-auto md:right-8 md:top-3 md:bottom-3">
                <div className="flex items-center justify-between">
                    <div className="p-1 space-y-2">
                        <p className="font-medium text-sm text-black/50 md:text-base lg:text-lg">
                            Amount To Pay
                        </p>

                        <h3 className="font-bold text-xl text-black/80 md:lg-text-2xl lg:text-4xl">
                            NGN {invoice?.amountDue}
                        </h3>
                    </div>

                    <img src="/logo.svg" width={48} height={48} alt="" />
                </div>

                <div className="grid grid-cols-2 pt-3">
                    <div className="p-1 space-y-2">
                        <p className="font-medium text-sm text-black/50 lg:text-base">
                            Invoice Number
                        </p>

                        <h5 className="font-semibold text-black/80">#{invoice?.invoiceNumber}</h5>
                    </div>

                    <div className="p-1 space-y-2">
                        <p className="font-medium text-sm text-black/50 lg:text-base">Date</p>

                        <h3 className="font-semibold text-black/80">
                            {formatDate(invoice?.createdAt as string)}
                        </h3>
                    </div>
                </div>

                {/* Invoice Details */}
                <div className="py-5 border-y border-black/20 space-y-1 my-5 md:grid grid-cols-2 md:space-y-0 gap-2">
                    <div className="space-y-3">
                        <div className="flex items-center justify-between p-1">
                            <p className="font-medium text-sm text-black/50 lg:text-base">
                                Invoice To
                            </p>

                            <Button
                                size="icon"
                                variant="secondary"
                                className="rounded-full grid place-items-center"
                            >
                                <ChevronDownIcon width={20} height={20} />
                            </Button>
                        </div>

                        <ul className="space-y-3">
                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg">
                                    {invoice?.student.name}
                                </p>
                            </li>

                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg">
                                    {invoice?.student.program.name}
                                </p>
                            </li>

                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg">
                                    {invoice?.student.level.name}
                                </p>
                            </li>

                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg">
                                    {invoice?.student.email}
                                </p>
                            </li>
                        </ul>
                    </div>

                    <div className="space-y-3">
                        <div className="flex items-center justify-between p-1">
                            <p className="font-medium text-sm text-black/50 lg:text-base">Pay To</p>

                            <Button
                                size="icon"
                                variant="secondary"
                                className="rounded-full grid place-items-center"
                            >
                                <ChevronDownIcon width={20} height={20} />
                            </Button>
                        </div>

                        <ul className="space-y-3">
                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg">NACOS PAY</p>
                            </li>

                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg text-blue-600 underline underline-offset-4">
                                    https://nacosfunaabpay.com.ng
                                </p>
                            </li>

                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg">09155104851</p>
                            </li>

                            <li className="flex items-center gap-3">
                                <img src="/verified.svg" alt="" width={24} height={24} />

                                <p className="font-medium text-black/90 lg:text-lg">
                                    support@nacosfunaabpay.com.ng
                                </p>
                            </li>
                        </ul>
                    </div>
                </div>

                {/* Invoice Status */}
                <div className="grid grid-cols-2">
                    <div className="p-1 space-y-2">
                        <p className="font-medium text-sm text-black/50 lg:text-base">Type</p>

                        <h5 className="font-semibold text-black/80">NACOS DUES</h5>
                    </div>

                    <div className="p-1 space-y-2">
                        <p className="font-medium text-sm text-black/50 lg:text-base">Status</p>

                        <h5 className="font-semibold text-black/80">Pending</h5>
                    </div>
                </div>

                {/* Alterations warning */}
                <div className="flex border border-[#E4E7EC] rounded my-5 relative overflow-hidden z-10">
                    {/* Line Indicator */}
                    <div className="block h-full w-1.5 bg-yellow-700 rounded-tr rounded-br"></div>
                    <div className="absolute left-0 top-0 bottom-0 h-[100px] w-1.5 bg-amber-500"></div>

                    <div className="flex gap-3 px-4 py-3">
                        <div className="border border-[#FBE2B7] rounded w-6 h-6 bg-[#FEF6E7] flex items-center justify-center">
                            <ExclamationTriangleIcon
                                width={14}
                                height={14}
                                className="text-amber-500"
                            />
                        </div>
                        <div className="text-sm space-y-0.5">
                            <h5 className="font-semibold text-black/80">Please Note:</h5>
                            <p className="text-[#475367]">
                                Any Alteration on this Invoice is Rendered Invalid!
                            </p>
                        </div>
                    </div>
                </div>

                {/* Action Buttons */}
                <div className="space-y-3 lg:mt-16 flex gap-3 items-center md:space-y-0">
                    <Button
                        disabled={isSubmitLoading}
                        onClick={onSubmit}
                        className="w-full h-auto font-bold text-sm py-4 bg-green-700 hover:bg-green-800"
                    >
                        {isSubmitLoading ? "Loading..." : "MAKE PAYMENT"}
                    </Button>
                    <Button
                        onClick={handleDownloadInvoice}
                        disabled={downloading}
                        variant="secondary"
                        className="w-full h-auto font-bold text-sm py-4 bg-green-50 text-green-600 hover:bg-green-100"
                    >
                        {downloading ? "Loading..." : "DOWNLOAD INVOICE"}
                    </Button>
                </div>
            </div>

            <main className="md:flex px-4 pt-11 pb-6 justify-evenly md:px-36 md:pt-24">
                <div className="md:flex flex-col justify-between mb-6">
                    <div className="space-y-2 md:space-y-0 text-center md:text-justify px-3 text-white max-w-[482px]">
                        <h1 className="font-bold text-xl md:text-4xl tracking-tighter uppercase">
                            Hello there! Pay Your NACOS Dues Easily and Securely here
                        </h1>
                        <p className="text-sm md:text-xl md:font-medium">
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
                            <p className="font-medium text-lg">
                                Nigeria Association of Computing Students
                            </p>
                        </div>
                    </div>
                </div>

                <Form {...form}>
                    <form
                        onSubmit={form.handleSubmit(onSubmit)}
                        className="w-full bg-white border-4 border-white/15 p-3 max-w-[520px] rounded-md space-y-6"
                    >
                        <div className="space-y-3 md:space-y-4">
                            <div className="md:flex gap-4">
                                <FormField
                                    control={form.control}
                                    name="firstName"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormControl>
                                                <Input
                                                    placeholder="First Name"
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
                                    name="lastName"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormControl>
                                                <Input
                                                    placeholder="Last Name"
                                                    className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    {...field}
                                                />
                                            </FormControl>
                                            <FormDescription />
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                            </div>

                            <FormField
                                control={form.control}
                                name="emailAddress"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormControl>
                                            <Input
                                                type="email"
                                                placeholder="Email Address"
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
                                name="registrationNo"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormControl>
                                            <Input
                                                placeholder="Matric / JAMB Number"
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
                                                    <SelectValue placeholder="Level" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem
                                                        value="1"
                                                        className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    >
                                                        100 LEVEL
                                                    </SelectItem>
                                                    <SelectItem
                                                        value="2"
                                                        className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    >
                                                        200 LEVEL
                                                    </SelectItem>
                                                    <SelectItem
                                                        // value="DE"
                                                        value="3"
                                                        className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    >
                                                        DIRECT ENTRY
                                                    </SelectItem>
                                                    <SelectItem
                                                        value="4"
                                                        className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    >
                                                        300 LEVEL
                                                    </SelectItem>
                                                    <SelectItem
                                                        value="5"
                                                        className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    >
                                                        400 LEVEL
                                                    </SelectItem>
                                                </SelectContent>
                                            </Select>
                                        </FormControl>
                                        <FormDescription />
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />

                            <FormField
                                control={form.control}
                                name="phoneNo"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormControl>
                                            <Input
                                                placeholder="Phone Number"
                                                className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormDescription />
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                        </div>

                        <Button
                            type="submit"
                            className="h-auto w-full py-3 md:py-4 mt-6 rounded-md font-bold text-sm md:text-base flex items-center gap-2.5 bg-emerald-700"
                        >
                            PROCEED{" "}
                            <span>
                                <img src="/arrow-right.svg" alt="" width={20} height={20} />
                            </span>
                        </Button>
                    </form>
                </Form>

                <div className="md:hidden flex items-center gap-3 px-3 text-white mt-10">
                    {/* TODO: create optimized image component */}
                    <img src="/logo.svg" alt="" width={32} height={32} />

                    <div className="space-y-0.5">
                        <h3 className="font-bold">NACOS, FUNAAB</h3>
                        <p className="font-medium text-sm">
                            Nigeria Association of Computing Students
                        </p>
                    </div>
                </div>
            </main>
        </>
    )
}
