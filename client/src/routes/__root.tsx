import { Toaster } from "@/components/ui/toaster"
import { createRootRoute, Outlet } from "@tanstack/react-router"

export const Route = createRootRoute({
    component: () => (
        <div className="relative bg-gradient-to-br from-zinc-950 from-30% via-emerald-950 to-emerald-700 min-h-screen p-3.5 sm:p-0">
            <Outlet />

            <footer className="absolute bottom-4 left-1/2 -translate-x-1/2 text-xs max-w-[90vw] text-center text-white/40">
                Designed by <em>Temi Akinbote</em>& <em>Developed by David Enikuomehin</em>
            </footer>

            <Toaster />
        </div>
    ),
})
