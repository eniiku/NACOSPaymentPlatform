import { Button } from "@/components/ui/button"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/verify-error")({
    component: ErrorPage,
})

function ErrorPage() {
    return (
        <main className="flex flex-col h-[90svh] justify-between md:h-auto md:justify-evenly md:flex-row px-4 pt-11 pb-6 md:px-36 md:pt-24">
            <div className="hidden md:flex flex-col justify-between mb-6">
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

            <section className="bg-white rounded-xl py-24 px-6 w-full md:max-w-[340px]">
                <div className="space-y-5">
                    <img src="/cloud-cross.svg" width={66} height={66} alt="" className="mx-auto" />

                    <div className="space-y-2 text-center max-w-[300px] mx-auto">
                        <h2 className="font-bold text-18px lg:text-xl text-black/90">
                            Verification Unsuccessful
                        </h2>

                        <p className="text-base text-black/60">Please try again</p>
                    </div>

                    <Button className="h-auto py-[18px] w-full bg-custom-green hover:bg-custom-green/95 text-white font-bold text-base">
                        Try Again
                    </Button>
                </div>
            </section>

            <div className="md:hidden flex items-center gap-3 px-3 text-white mt-16">
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
