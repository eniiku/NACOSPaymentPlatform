import z from "zod"

export const userFormSchema = z.object({
    firstName: z.string().min(2).max(50),
    lastName: z.string().min(2).max(50),
    emailAddress: z.string().email().min(50),
    registrationNo: z.number().min(8).max(14),
    level: z.enum(["100 Level", "200 Level", "Direct Entry", "300 Level", "400 Level"]),
    phoneNo: z.string().regex(/^(?:\+?234|0)([789]\d{9})$/),
})
