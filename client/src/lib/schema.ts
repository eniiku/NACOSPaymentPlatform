import z from "zod"

export const userFormSchema = z.object({
    firstName: z.string().min(2, { message: "First name must be at least 2 characters" }).max(50),
    lastName: z.string().min(2).max(50),
    emailAddress: z.string().email().max(50),
    registrationNo: z.string().min(8).max(14),
    level: z.enum(["1", "2", "3", "4", "5"]),
    phoneNo: z.string().regex(/^(?:\+?234|0)([789]\d{9})$/),
})
