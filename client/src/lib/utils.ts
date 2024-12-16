import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

export function formatDate(dateString: string) {
    const date = new Date(dateString)

    const day = String(date.getDate()).padStart(2, "0")
    const month = String(date.getMonth() + 1).padStart(2, "0") // Months are 0-indexed
    const year = date.getFullYear()

    return `${day}-${month}-${year}`
}

/**
 * Converts strings like "bank_transfer" or "bank-transfer" to "Bank Transfer"
 *
 * @param input The input string in snake_case or kebab-case
 * @returns A formatted string in Title Case
 */
export const formatReadableString = (input: string): string => {
    if (!input) return ""

    // Replace underscores and hyphens with spaces
    const spacedString = input.replace(/_/g, " ").replace(/-/g, " ")

    // Split the string into words and capitalize first letter of each
    return spacedString
        .split(/\s+/)
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
        .join(" ")
}
