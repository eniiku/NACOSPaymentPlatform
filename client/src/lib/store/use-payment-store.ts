import { create } from "zustand"
import { PaymentResponse } from "../types"

interface PaymentStore {
    payment: PaymentResponse | null
    setPayment: (payment: PaymentResponse) => void
    clearPayment: () => void
    // fetchInvoice: (id: number) => Promise<void>
}

const usePaymentStore = create<PaymentStore>((set) => ({
    payment: null,
    setPayment: (payment) => set({ payment }),
    clearPayment: () => set({ payment: null }),
}))

export default usePaymentStore
