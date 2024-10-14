import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/verify-error")({
    component: () => (
        <main className="grid place-items-center w-screen h-screen">
            <div className="space-y-6 text-center text-white">
                <h1 className="text-3xl">Verification Unsuccessful</h1>
                <p className="text-lg">Please Try again</p>
            </div>
        </main>
    ),
})
