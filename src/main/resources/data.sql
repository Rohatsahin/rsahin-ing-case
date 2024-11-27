CREATE TABLE customer
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              varchar NOT NULL,
    surname           varchar NOT NULL,
    credit_limit      BIGINT  NOT NULL,
    used_credit_limit BIGINT  NOT NULL
);

CREATE TABLE loan
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id           BIGINT                   NOT NULL,
    amount                BIGINT                   NOT NULL,
    number_of_installment INTEGER                  NOT NULL,
    created_at            timestamp with time zone NOT NULL,
    is_paid               BOOLEAN                  NOT NULL,

    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);


CREATE TABLE loanInstallment
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id      BIGINT                   NOT NULL,
    amount       BIGINT                   NOT NULL,
    paid_amount  BIGINT                   DEFAULT NULL,
    due_date     timestamp with time zone NOT NULL,
    payment_date timestamp with time zone DEFAULT NULL,
    is_paid      BOOLEAN                  NOT NULL,

    CONSTRAINT fk_loan FOREIGN KEY (loan_id) REFERENCES loan (id) ON DELETE CASCADE
);

INSERT INTO customer VALUES (1, 'Customer_1', 'SurName', 10000, 0);
INSERT INTO customer VALUES (2, 'Customer_2', 'SurName', 10000, 0);
