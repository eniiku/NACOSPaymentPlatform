import { createLazyFileRoute } from "@tanstack/react-router"

export const Route = createLazyFileRoute("/")({
    component: Index,
})

function Index() {
    return (
        <div className="p-2">
            <h3>Home Page!!! Welcome Home!</h3>
        </div>
    )
}
