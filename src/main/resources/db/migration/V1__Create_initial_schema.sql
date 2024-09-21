CREATE TABLE IF NOT EXISTS program
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS level
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    dues_amount DECIMAL(10, 2)     NOT NULL
);

CREATE TABLE IF NOT EXISTS academic_year
(
    id         BIGSERIAL PRIMARY KEY,
    year       VARCHAR(255) UNIQUE NOT NULL,
    start_date DATE                NOT NULL,
    end_date   DATE                NOT NULL,
    is_current BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS student
(
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255)        NOT NULL,
    email               VARCHAR(255) UNIQUE NOT NULL,
    phone_number        VARCHAR(20)         NOT NULL,
    registration_number VARCHAR(50) UNIQUE  NOT NULL,
    program_id          BIGINT              NOT NULL REFERENCES program (id),
    level_id            BIGINT              NOT NULL REFERENCES level (id),
    academic_year_id    BIGINT              NOT NULL REFERENCES academic_year (id),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS invoice
(
    id             BIGSERIAL PRIMARY KEY,
    student_id     BIGINT         NOT NULL REFERENCES student (id),
    amount_due     DECIMAL(10, 2) NOT NULL,
    invoice_date   TIMESTAMP      NOT NULL,
    invoice_status VARCHAR(20)    NOT NULL,
    due_date       DATE           NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payment
(
    id                    BIGSERIAL PRIMARY KEY,
    student_id            BIGINT              NOT NULL REFERENCES student (id),
    invoice_id            BIGINT              NOT NULL REFERENCES invoice (id),
    amount_paid           DECIMAL(10, 2)      NOT NULL,
    payment_date          TIMESTAMP           NOT NULL,
    payment_status        VARCHAR(20)         NOT NULL,
    payment_method        VARCHAR(100)        NOT NULL,
    transaction_reference VARCHAR(255) UNIQUE NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS receipt
(
    id             BIGSERIAL PRIMARY KEY,
    payment_id     BIGINT UNIQUE      NOT NULL REFERENCES payment (id),
    receipt_number VARCHAR(50) UNIQUE NOT NULL,
    receipt_date   TIMESTAMP          NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);