import { z } from "zod"
import { useForm } from "react-hook-form"
import { createLazyFileRoute } from "@tanstack/react-router"

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

export const Route = createLazyFileRoute("/")({
    component: Index,
})

function Index() {
    const form = useForm<z.infer<typeof userFormSchema>>()

    return (
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
                <form className="w-full bg-white border-4 border-white/15 p-3 max-w-[520px] rounded-md space-y-6">
                    <div className="space-y-3 md:space-y-4">
                        <div className="md:flex gap-4">
                            <FormField
                                control={form.control}
                                name="firstName"
                                render={() => (
                                    <FormItem>
                                        <FormControl>
                                            <Input
                                                placeholder="First Name"
                                                className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
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
                                render={() => (
                                    <FormItem>
                                        <FormControl>
                                            <Input
                                                placeholder="Last Name"
                                                className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
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
                            render={() => (
                                <FormItem>
                                    <FormControl>
                                        <Input
                                            type="email"
                                            placeholder="Email Address"
                                            className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
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
                            render={() => (
                                <FormItem>
                                    <FormControl>
                                        <Input
                                            placeholder="Matric / JAMB Number"
                                            className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
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
                            render={() => (
                                <FormItem>
                                    <FormControl>
                                        <Select>
                                            <SelectTrigger className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium">
                                                <SelectValue placeholder="Level" />
                                            </SelectTrigger>
                                            <SelectContent>
                                                <SelectItem
                                                    value="100"
                                                    className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                >
                                                    100 LEVEL
                                                </SelectItem>
                                                <SelectItem
                                                    value="200"
                                                    className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                >
                                                    200 LEVEL
                                                </SelectItem>
                                                <SelectItem
                                                    value="DE"
                                                    className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                >
                                                    DIRECT ENTRY
                                                </SelectItem>
                                                <SelectItem
                                                    value="300"
                                                    className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                                >
                                                    300 LEVEL
                                                </SelectItem>
                                                <SelectItem
                                                    value="400"
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
                            render={() => (
                                <FormItem>
                                    <FormControl>
                                        <Input
                                            placeholder="Phone Number"
                                            className="bg-[#F3F3F3] rounded py-4 px-5 h-auto text-sm md:text-lg font-medium"
                                        />
                                    </FormControl>
                                    <FormDescription />
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>

                    <Button className="h-auto w-full py-3 md:py-4 mt-6 rounded-md font-bold text-sm md:text-base flex items-center gap-2.5 bg-emerald-700">
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
                    <p className="font-medium text-sm">Nigeria Association of Computing Students</p>
                </div>
            </div>
        </main>
    )
}
