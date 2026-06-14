CREATE TABLE customers (
   customer_id VARCHAR(36) NOT NULL,
   name VARCHAR(150) NOT NULL,
   document VARCHAR(11) NOT NULL,
   email VARCHAR(150) NOT NULL,
   phone VARCHAR(20),
   income DECIMAL(19,2) NOT NULL,
   status VARCHAR(30) NOT NULL,
   created_at DATETIME NOT NULL,
   updated_at DATETIME,

   CONSTRAINT pk_customers PRIMARY KEY (customer_id),
   CONSTRAINT uk_customers_document UNIQUE (document)
);