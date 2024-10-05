import { createRootRoute, Outlet } from "@tanstack/react-router"

export const Route = createRootRoute({
    component: () => (
        <>
            <h1 className="font-mono text-blue-300">
                Root Page.. You can think of this as a layout page. If perhaps there is a navbar or
                a footer you need to propagate throughtout multiple pages. You can do that here
            </h1>
            <Outlet />
        </>
    ),
})
