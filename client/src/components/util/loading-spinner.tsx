import { LoaderCircle } from "lucide-react"

const LoadingSpinner = () => {
    return (
        <div className="flex justify-center items-center">
            <LoaderCircle className="animate-spin" width={32} height={32} />
        </div>
    )
}

export default LoadingSpinner
