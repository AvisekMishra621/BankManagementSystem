CREATE DATABASE bankmanagementsystem;
USE bankmanagementsystem;

CREATE TABLE signup (
    formno VARCHAR(20) PRIMARY KEY, 
    name VARCHAR(50), 
    father_name VARCHAR(50), 
    dob VARCHAR(20),
    gender VARCHAR(10),
    email VARCHAR(50) UNIQUE, 
    marital_status VARCHAR(15), 
    address VARCHAR(100), 
    city VARCHAR(30), 
    pincode VARCHAR(10), 
    state VARCHAR(30)
);

CREATE TABLE signup2 (
    formno VARCHAR(20), 
    religion VARCHAR(20), 
    category VARCHAR(20), 
    income VARCHAR(20), 
    education VARCHAR(50), 
    occupation VARCHAR(50), 
    pan VARCHAR(20) UNIQUE, 
    aadhar VARCHAR(20) UNIQUE, 
    seniorcitizen VARCHAR(5), 
    existingaccount VARCHAR(5),
    FOREIGN KEY (formno) REFERENCES signup(formno) ON DELETE CASCADE
);

CREATE TABLE signup3 (
    formno VARCHAR(20), 
    accountType VARCHAR(40), 
    cardno BIGINT UNIQUE,
    pin INT NOT NULL,
    facility VARCHAR(255),
    FOREIGN KEY (formno) REFERENCES signup(formno) ON DELETE CASCADE
);

CREATE TABLE login (
    formno VARCHAR(20), 
    cardno BIGINT UNIQUE, 
    pin INT NOT NULL UNIQUE,
    FOREIGN KEY (formno) REFERENCES signup(formno) ON DELETE CASCADE
);

CREATE TABLE bank (
    pin INT NOT NULL,
    date VARCHAR(50),
    type ENUM('Deposit', 'Withdraw', 'Transfer'),
    amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pin) REFERENCES login(pin) ON DELETE CASCADE
);

SHOW DATABASES;

ALTER TABLE bank DROP FOREIGN KEY bank_ibfk_1;
ALTER TABLE bank ADD CONSTRAINT bank_ibfk_1 FOREIGN KEY (pin) REFERENCES login(pin) ON UPDATE CASCADE ON DELETE CASCADE;



