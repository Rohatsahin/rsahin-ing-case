CREATE TABLE IF NOT EXISTS customer
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              varchar NOT NULL,
    surname           varchar NOT NULL,
    credit_limit      BIGINT  NOT NULL,
    used_credit_limit BIGINT  NOT NULL
);

CREATE TABLE IF NOT EXISTS loan
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id           BIGINT                   NOT NULL,
    amount                BIGINT                   NOT NULL,
    number_of_installment INTEGER                  NOT NULL,
    created_at            timestamp with time zone NOT NULL,
    is_paid               BOOLEAN                  NOT NULL,

    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS loan_installment
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

INSERT INTO customer(id, name, surname, credit_limit, used_credit_limit) VALUES (100, 'Lorem', 'Ipsum', 10000, 0);
INSERT INTO customer(id, name, surname, credit_limit, used_credit_limit) VALUES (101, 'Quis', 'Autem', 11000, 0);
