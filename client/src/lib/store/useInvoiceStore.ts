import { create } from "zustand"

interface Student {
    createdAt: string
    updatedAt: string
    id: number
    name: string
    email: string
    phoneNumber: string
    registrationNumber: string
}

interface Invoice {
    id: number
    student: Student
    level: string | null
    amountDue: number
    invoiceDate: string
    invoiceStatus: string
    dueDate: string
    createdAt: string
    updatedAt: string
}

interface InvoiceStore {
    invoice: Invoice | null
    setInvoice: (invoice: Invoice) => void
    clearInvoice: () => void
    // fetchInvoice: (id: number) => Promise<void>
}

const useInvoiceStore = create<InvoiceStore>((set) => ({
    invoice: null,
    setInvoice: (invoice) => set({ invoice }),
    clearInvoice: () => set({ invoice: null }),
    // fetchInvoice: async (id) => {
    //     try {
    //         const response = await fetch(`http://your-api-url/api/v1/invoices/${id}`)
    //         if (!response.ok) {
    //             throw new Error("Failed to fetch invoice")
    //         }
    //         const data = await response.json()
    //         set({ invoice: data })
    //     } catch (error) {
    //         console.error("Error fetching invoice:", error)
    //         // You might want to set an error state here
    //     }
    // },
}))

export default useInvoiceStore
