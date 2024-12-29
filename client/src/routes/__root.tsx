import Footer from "@/components/ui/navigation/footer"
import { Toaster } from "@/components/ui/toaster"
import { createRootRoute, Outlet } from "@tanstack/react-router"

export const Route = createRootRoute({
    component: () => (
        <div className="relative bg-gradient-to-br from-zinc-950 from-30% via-emerald-950 to-emerald-700">
            <main className="min-h-[90svh] p-3.5 sm:p-0">
                <Outlet />
            </main>

            <Footer />
            <Toaster />
        </div>
    ),
})
