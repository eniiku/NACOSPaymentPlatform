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

export const Route = createLazyFileRoute("/find")({
    component: FindPage,
})

function FindPage() {
    const form = useForm()

    const onSubmit = () => {
        console.log("big bunda's probably getting dicked out crazy now")
    }

    return (
        <div className="min-h-[90svh] text-white flex flex-col justify-start items-center pt-14 md:pt-20 relative">
            <img
                src="/images/overlay.webp"
                alt=""
                className="absolute left-0 bottom-0 z-10"
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

                <Form {...form}>
                    <form
                        onSubmit={form.handleSubmit(onSubmit)}
                        className="w-full bg-white border-[12px] border-white/15 p-3 max-w-[780px] rounded-md space-y-6"
                    >
                        <div className="space-y-3 md:space-y-4">
                            <FormField
                                control={form.control}
                                name="matricNo"
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
                            className="h-auto w-full py-3 md:py-4 mt-6 rounded-md font-bold text-sm md:text-base flex items-center gap-2.5 bg-emerald-700"
                        >
                            RETRIEVE MY RECEIPT{" "}
                            <span>
                                <img src="/arrow-right.svg" alt="" width={20} height={20} />
                            </span>
                        </Button>
                    </form>
                </Form>
            </div>
        </div>
    )
}
