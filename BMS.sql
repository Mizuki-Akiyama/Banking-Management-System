-- Create Database
CREATE DATABASE Banking_Management_System;
USE Banking_management_system;

-- Branch Table
CREATE TABLE Branch (
branch_id INT,
branch_name VARCHAR(100) NOT NULL,
branch_address VARCHAR(250) NOT NULL,
PRIMARY KEY(branch_id)
);

-- Employee Table
CREATE TABLE Employee (
emp_id INT,
emp_name VARCHAR(100) NOT NULL,
emp_position VARCHAR(50) NOT NULL,
branch_id INT,
PRIMARY KEY(emp_id),
FOREIGN KEY(branch_id) REFERENCES Branch(branch_id)
ON DELETE CASCADE
ON UPDATE CASCADE 
);

-- Customer Table
CREATE TABLE Customer(
customer_id INT,
name VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL,
phone VARCHAR(30) NOT NULL,
address VARCHAR(250) NOT NULL,
branch_id INT,
PRIMARY KEY(customer_id),
FOREIGN KEY(branch_id) REFERENCES Branch(branch_id)
ON DELETE CASCADE
ON UPDATE CASCADE
);

-- Account Table 
CREATE TABLE Account(
account_no BIGINT,
balance DECIMAL(15,2) DEFAULT 0.00,
customer_id INT,
PRIMARY KEY(account_no),
FOREIGN KEY(customer_id) REFERENCES Customer(customer_id)
ON DELETE CASCADE
ON UPDATE CASCADE
);

-- Transaction Table(Weak Entity)
CREATE TABLE Txn(
txn_no INT NOT NULL,
txn_type ENUM('Deposit','Withdraw','Transfer') NOT NULL,
tnx_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
amount DECIMAL(15,2) NOT NULL,
account_no BIGINT,
PRIMARY KEY(account_no,txn_no),
FOREIGN KEY(account_no) REFERENCES Account(account_no)
ON DELETE CASCADE
ON UPDATE CASCADE
);

-- Loan Table
CREATE TABLE Loan(
loan_id INT,
loan_type VARCHAR(50) NOT NULL,
loan_amount DECIMAL(15,2) NOT NULL,
status ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
PRIMARY KEY(loan_id),
customer_id INT,
FOREIGN KEY(customer_id) REFERENCES Customer(customer_id)
ON DELETE CASCADE
ON UPDATE CASCADE
);

-- Login Table 
CREATE TABLE Login(
login_id INT,
username VARCHAR(50),
password VARCHAR(50),
customer_id INT,
PRIMARY KEY(login_id),
FOREIGN KEY(customer_id) REFERENCES Customer(customer_id)
ON DELETE CASCADE
ON UPDATE CASCADE
);

INSERT INTO Branch(branch_id,branch_name,branch_address) 
VALUES
(1, 'Gulshan Branch', 'Gulshan-1, Dhaka'),
(2, 'Banani Branch', 'Banani, Dhaka'),
(3, 'Uttara Branch', 'Uttara, Dhaka'),
(4, 'Mirpur Branch', 'Mirpur-10, Dhaka'),
(5, 'Dhanmondi Branch', 'Dhanmondi, Dhaka'),
(6, 'Motijheel Branch', 'Motijheel, Dhaka'),
(7, 'Chittagong Branch', 'Agrabad, Chittagong'),
(8, 'Rajshahi Branch', 'Shaheb Bazar, Rajshahi'),
(9, 'Sylhet Branch', 'Zindabazar, Sylhet'),
(10, 'Khulna Branch', 'KDA Avenue, Khulna');

INSERT INTO Employee(emp_id,emp_name,emp_position,branch_id) 
VALUES
(101, 'Rakib Hasan', 'Manager', 1),
(102, 'Sadia Rahman', 'Cashier', 1),
(103, 'Fahim Ahmed', 'Clerk', 2),
(104, 'Nusrat Jahan', 'Manager', 2),
(105, 'Tanvir Alam', 'Officer', 3),
(106, 'Mizan Khan', 'Manager', 4),
(107, 'Arif Hossain', 'Cashier', 5),
(108, 'Mehedi Hasan', 'Officer', 6),
(109, 'Rafi Uddin', 'Clerk', 7),
(110, 'Lamia Akter', 'Officer', 8);

INSERT INTO Customer(customer_id,name,email,phone,address,branch_id)
VALUES
(201, 'Sabbir Hossain', 'sabbir@gmail.com', '01710000001', 'Gulshan, Dhaka', 1),
(202, 'Mim Akter', 'mim@gmail.com', '01710000002', 'Banani, Dhaka', 2),
(203, 'Tuhin Ahmed', 'tuhin@gmail.com', '01710000003', 'Uttara, Dhaka', 3),
(204, 'Rina Khatun', 'rina@gmail.com', '01710000004', 'Mirpur, Dhaka', 4),
(205, 'Hridoy Islam', 'hridoy@gmail.com', '01710000005', 'Dhanmondi, Dhaka', 5),
(206, 'Afsana Nahar', 'afsana@gmail.com', '01710000006', 'Motijheel, Dhaka', 6),
(207, 'Siam Rahman', 'siam@gmail.com', '01710000007', 'Chittagong', 7),
(208, 'Nabila Khan', 'nabila@gmail.com', '01710000008', 'Rajshahi', 8),
(209, 'Nayem Hossain', 'nayem@gmail.com', '01710000009', 'Sylhet', 9),
(210, 'Jannat Ferdous', 'jannat@gmail.com', '01710000010', 'Khulna', 10);

INSERT INTO Account(account_no,balance,customer_id)
VALUES
(100000001, 25000.50, 201),
(100000002, 5000.00, 202),
(100000003, 72000.00, 203),
(100000004, 1500.00, 204),
(100000005, 8700.25, 205),
(100000006, 30000.00, 206),
(100000007, 110000.00, 207),
(100000008, 56000.50, 208),
(100000009, 8900.75, 209),
(100000010, 95000.00, 210);

INSERT INTO Txn(txn_no,txn_type,tnx_date,amount,account_no) 
VALUES
(1, 'Deposit', DEFAULT, 5000.00, 100000001),
(2, 'Withdraw', DEFAULT, 2000.00, 100000001),
(1, 'Deposit', DEFAULT, 3000.00, 100000002),
(1, 'Withdraw', DEFAULT, 1000.00, 100000003),
(1, 'Deposit', DEFAULT, 7000.00, 100000004),
(1, 'Transfer', DEFAULT, 1500.00, 100000005),
(1, 'Deposit', DEFAULT, 10000.00, 100000006),
(1, 'Withdraw', DEFAULT, 2500.00, 100000007),
(1, 'Transfer', DEFAULT, 5000.00, 100000008),
(1, 'Deposit', DEFAULT, 12000.00, 100000009);

INSERT INTO Loan(loan_id,loan_type,loan_amount,status,customer_id) 
VALUES
(301, 'Home Loan', 500000.00, 'Approved', 201),
(302, 'Car Loan', 200000.00, 'Pending', 202),
(303, 'Personal Loan', 100000.00, 'Rejected', 203),
(304, 'Business Loan', 800000.00, 'Approved', 204),
(305, 'Education Loan', 150000.00, 'Pending', 205),
(306, 'Home Loan', 600000.00, 'Approved', 206),
(307, 'Car Loan', 180000.00, 'Pending', 207),
(308, 'Personal Loan', 90000.00, 'Approved', 208),
(309, 'Business Loan', 750000.00, 'Approved', 209),
(310, 'Education Loan', 120000.00, 'Pending', 210);

INSERT INTO Login(login_id,username,password,customer_id) 
VALUES
(401, 'sabbir01', 'pass123', 201),
(402, 'mim02', 'mim2025', 202),
(403, 'tuhin03', 'tuhin@123', 203),
(404, 'rina04', 'rina456', 204),
(405, 'hridoy05', 'hridoy@789', 205),
(406, 'afsana06', 'afsana2025', 206),
(407, 'siam07', 'siam@111', 207),
(408, 'nabila08', 'nabila@333', 208),
(409, 'nayem09', 'nayem@444', 209),
(410, 'jannat10', 'jannat@555', 210);

SELECT* FROM Branch;
SELECT* FROM Customer;
SELECT* FROM Employee;
SELECT* FROM Account;
SELECT* FROM Txn;
SELECT* FROM Loan;
SELECT* FROM Login;

-- 1. List of All customers, Names in ASC Order
SELECT customer_id, name, email, phone, branch_id
FROM Customer
ORDER BY name ASC;

-- 2. List of Customers in Dhaka
SELECT customer_id, name, address
FROM Customer
WHERE address LIKE '%Dhaka%';

-- 3. Accounts with a Balance ≥ 50,000
SELECT account_no, customer_id, balance
FROM Account
WHERE balance >= 50000
ORDER BY balance DESC; 

-- 4. Names of Employees in the Manager Position 
SELECT emp_id, emp_name,emp_position, branch_id
FROM Employee
WHERE emp_position = 'Manager';

-- 5. Customer Name With Branch 
SELECT c.customer_id, c.name AS customer_name, b.branch_name
FROM Customer c
JOIN Branch b ON c.branch_id = b.branch_id
ORDER BY c.customer_id;

-- 6. Customer Name With Account's Balance 
SELECT c.customer_id, c.name, a.account_no, a.balance
FROM Customer c
JOIN Account a ON a.customer_id = c.customer_id
ORDER BY a.balance DESC;

--  7. Customer And His Username and Password 
SELECT c.customer_id, c.name, lg.username,lg.password
FROM Customer c
JOIN Login lg ON lg.customer_id = c.customer_id
ORDER BY c.customer_id; 

-- 8. Number Of Customer For Each Branch 
SELECT b.branch_name, COUNT(*) AS total_customers
FROM Customer c
JOIN Branch b ON b.branch_id = c.branch_id
GROUP BY b.branch_id, b.branch_name
ORDER BY total_customers DESC; 

-- 9. Highest Balance Account's Information
SELECT *
FROM Account
WHERE balance = (SELECT MAX(balance) FROM Account); 

-- 10. Customer Who Don't Have Any Loans
SELECT c.customer_id, c.name
FROM Customer c
WHERE c.customer_id NOT IN (SELECT customer_id FROM Loan); 

-- 11. Total Account Balances At Each Branch
SELECT b.branch_name, SUM(a.balance) AS branch_total_balance
FROM Account a
JOIN Customer c ON c.customer_id = a.customer_id
JOIN Branch b ON b.branch_id = c.branch_id
GROUP BY b.branch_id, b.branch_name
ORDER BY branch_total_balance DESC;


-- 12. Check Duplicate Usename
SELECT username, COUNT(*) AS Numbers
FROM Login
GROUP BY username
HAVING COUNT(*) > 1;






















