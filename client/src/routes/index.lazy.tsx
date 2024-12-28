import { z } from "zod"
import { AxiosError, HttpStatusCode } from "axios"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { createLazyFileRoute, useNavigate } from "@tanstack/react-router"

import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { userFormSchema } from "@/lib/schema"
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { Button } from "@/components/ui/button"
import useInvoiceStore from "@/lib/store/useInvoiceStore"
import { apiClient } from "@/lib/api"
import { LEVELS, PROGRAMS } from "@/lib/data"
import { useToast } from "@/hooks/use-toast"

export const Route = createLazyFileRoute("/")({
    component: Index,
})

function Index() {
    const navigate = useNavigate({ from: "/" })
    const { setInvoice } = useInvoiceStore()
    const { toast } = useToast()

    const form = useForm<z.infer<typeof userFormSchema>>({
        resolver: zodResolver(userFormSchema),
        defaultValues: {
            firstName: "",
            lastName: "",
            emailAddress: "",
            registrationNo: "",
            level: undefined,
            program: undefined,
            phoneNo: "",
        },
    })

    function onSubmit(values: z.infer<typeof userFormSchema>) {
        // TODO: REFACTOR
        //TODO: I THINK THE BACKEND IS FETCHING THE DETAILS PER MATRIC NUMBER. RESTRICT THAT
        apiClient
            .post("/invoices", {
                email: values.emailAddress,
                phoneNumber: values.phoneNo,
                registrationNumber: values.registrationNo,
                ...values,
            })
            .then((res) => {
                const invoiceId = res.data?.id
                if (res.status === HttpStatusCode.Created) {
                    setInvoice(res.data)
                    navigate({ to: "/invoice", params: { invoiceId } })
                }
            })
            .catch((err: AxiosError) => {
                console.log("[ERROR]: ", err)
                toast({
                    variant: "destructive",
                    title: "Oops! Something went wrong",
                    description: err.message,
                })
            })
    }

    return (
        <main className="lg:flex gap-4 px-4 pt-11 pb-6 justify-evenly md:px-12 md:py-16 xl:px-36 lg:pt-24">
            <div className="lg:flex flex-col justify-between mb-6">
                <div className="space-y-2 md:space-y-4 text-center lg:text-justify px-3 text-white max-w-[560px] lg:max-w-[482px] mx-auto">
                    <h1 className="font-bold text-xl md:text-4xl tracking-tighter uppercase">
                        Hello there! Pay Your NACOS Dues Easily and Securely here
                    </h1>
                    <p className="text-sm  text-justify lg:text-left md:text-lg text-white/70">
                        Welcome to the official payment portal for NACOS dues. Kindly provide
                        accurate information to ensure a successful payment.
                    </p>
                </div>

                <div className="hidden lg:flex items-center text-white gap-3">
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

            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    className="w-full bg-white border-4 border-white/15 p-3 md:mx-auto max-w-[520px] rounded-md space-y-6"
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
                            name="program"
                            render={({ field }) => (
                                <FormItem>
                                    <FormControl>
                                        <Select
                                            onValueChange={field.onChange}
                                            defaultValue={field.value}
                                        >
                                            <SelectTrigger className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium">
                                                <SelectValue placeholder="Select Program" />
                                            </SelectTrigger>
                                            <SelectContent>
                                                {PROGRAMS.map((program) => (
                                                    <SelectItem
                                                        key={program.key}
                                                        value={program.key}
                                                        className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                    >
                                                        {program.name}
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
                    <p className="font-medium text-sm text-white/70">
                        Nigeria Association of Computing Students
                    </p>
                </div>
            </div>
        </main>
    )
}
