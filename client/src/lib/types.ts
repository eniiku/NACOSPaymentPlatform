export interface Level {
    key: "L100" | "L200" | "L200F" | "L300" | "L400"
    name: string
}

export interface Program {
    key: "CSC" | "DSC" | "IFS" | "IFT" | "ICT" | "SWE" | "CYS"
    name: string
    description: string
}

export interface Student {
    createdAt: string
    updatedAt: string
    id: number
    name: string
    email: string
    level: Level
    program: Program
    phoneNumber: string
    registrationNumber: string
}

interface Payment {
    id: number
    student: Student
    amountPaid: number
    paymentDate: string
    paymentStatus: "SUCCESSFUL" | "PENDING" | "FAILED"
    paymentMethod: string
}

/**
 * Transaction Metadata interface
 */
export interface TransactionMeta {
    __CheckoutInitAddress?: string
    student_id?: string
    invoice_number?: string
    originatoraccountnumber?: string
    originatorname?: string
    bankname?: string
    originatoramount?: string
}

/**
 * Transaction Details interface
 */
export interface TransactionDetails {
    id: number
    tx_ref: string
    amount: number
    currency: string
    charged_amount: number
    app_fee: number
    status: string
    payment_type: string
    created_at: string
    meta: TransactionMeta
}

/**
 * Receipt Data Transfer Object interface
 */
export interface Receipt {
    id: number
    receiptNumber: string
    receiptDate: string
    payment: Payment
}

/**
 * Full Payment Response interface
 */
export interface PaymentResponse {
    successful: boolean
    status: string
    message: string
    receipt: Receipt
    transactionDetails: TransactionDetails
}
