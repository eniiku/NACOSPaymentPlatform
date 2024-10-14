import { Button } from "@/components/ui/button"
import { createLazyFileRoute } from "@tanstack/react-router"

export const Route = createLazyFileRoute("/success")({
    component: () => (
        <main className="grid place-items-center w-screen h-screen">
            <div className="space-y-6 text-center text-white">
                <h1 className="text-3xl">Payment Succesful</h1>
                <p className="text-lg">Be sure to remit your reciept & get it stamped.</p>

                <Button>Download Here</Button>

                <p>or, Check you email!</p>
            </div>
        </main>
    ),
})
