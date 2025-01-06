import z from "zod"

export const userFormSchema = z.object({
    firstName: z
        .string()
        .min(2, { message: "First name must be at least 2 characters" })
        .max(50, {
            message: "First name cannot exceed 50 characters",
        })
        .refine((name) => /^[a-zA-Z\s'-]+$/.test(name), {
            message: "First name can only contain letters, spaces, hyphens and apostrophes",
        }),
    lastName: z
        .string()
        .min(2, { message: "Last name must be at least 2 characters" })
        .max(50, {
            message: "Last name cannot exceed 50 characters",
        })
        .refine((name) => /^[a-zA-Z\s'-]+$/.test(name), {
            message: "Last name can only contain letters, spaces, hyphens and apostrophes",
        }),

    emailAddress: z.string().email().max(50, {
        message: "Email address cannot exceed 50 characters",
    }),
    registrationNo: z
        .string()
        .min(8, {
            message: "Registration number must be at least 8 characters",
        })
        .max(14, {
            message: "Registration number cannot exceed 14 characters",
        })
        .regex(/^[A-Z0-9]+$/, {
            message: "Registration number can only contain uppercase letters and numbers",
        }),
    level: z.enum(["L100", "L200", "L200F", "L300", "L400"], {
        errorMap: () => ({
            message: "Please select your current level",
        }),
    }),
    program: z.enum(["CSC", "DSC", "IFS", "IFT", "ICT", "SWE", "CYS"], {
        errorMap: () => ({
            message: "Please select your program",
        }),
    }),
    phoneNo: z.string().regex(/^(?:\+?234|0)([789]\d{9})$/, {
        message: "Please enter a valid phone number (e.g., +2348012345678 or 08012345678)",
    }),
})

export const findFormSchema = z.object({
    registrationNo: z
        .string()
        .min(8, {
            message: "Registration number must be at least 8 characters",
        })
        .max(14, {
            message: "Registration number cannot exceed 14 characters",
        })
        .regex(/^[A-Z0-9]+$/, {
            message: "Registration number can only contain uppercase letters and numbers",
        }),
    level: z.enum(["L100", "L200", "L200F", "L300", "L400"], {
        errorMap: () => ({
            message: "Please select your current level",
        }),
    }),
})
