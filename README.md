# 🏦 Banking Management System (Java + MySQL)

## 📘 Overview
The **Banking Management System (BMS)** is a console-based Java application designed to manage essential banking operations efficiently. It provides functionalities such as account creation, deposit, withdrawal, transfer, transaction tracking, and loan management, all integrated with a secure MySQL database for real-time data storage and retrieval.

## 🚀 Features
- 👤 **Customer Management:** Create, view, and manage customer details.  
- 💰 **Account Management:** Perform deposits, withdrawals, and transfers.  
- 📄 **Transaction Logs:** Tracks all account transactions in real time.  
- 🏦 **Branch & Employee Records:** Each customer and employee is linked to a branch.  
- 💳 **Loan Module:** Allows customers to apply for loans and admins to approve/reject them.  
- 🔐 **Login System:** User authentication with username and password.  
- 🧾 **SQL Integration:** All operations are synced directly with the MySQL database.

## 🗂️ Project Structure
```
/Banking_Management_System
│
├── App.java              # Main entry point for the application
├── DBConnection.java     # Handles MySQL connection
├── Models.java           # Entity classes (Customer, Account, Loan, etc.)
├── DAOs.java             # Database access layer for CRUD operations
├── Services.java         # Business logic and validation
├── BMS.sql               # SQL script to create database and tables with sample data
└── README.md             # Project documentation
```

## 🛠️ Technologies Used
- **Programming Language:** Java (JDK 17 or later recommended)  
- **Database:** MySQL  
- **JDBC:** For database connectivity  
- **IDE:** IntelliJ IDEA / Eclipse / NetBeans  
- **Package Name:** `db`

## ⚙️ Setup Instructions
### 1️⃣ Database Setup
1. Open **MySQL** and create the database using the provided script:
   ```sql
   SOURCE BMS.sql;
   ```
2. This will create tables like:
   - `Branch`, `Employee`, `Customer`, `Account`, `Txn`, `Loan`, and `Login`
   - Insert sample records for initial testing.

### 2️⃣ Java Setup
1. Clone or download the repository.
2. Open it in your preferred Java IDE.
3. Update your database credentials inside `DBConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/BMS";
   private static final String USER = "root";
   private static final String PASSWORD = "your_password";
   ```
4. Run the `App.java` file to launch the system.

## 💻 Sample Operations
| Operation | Description | Example |
|------------|--------------|----------|
| Create Account | Adds a new customer and account record | `Enter customer info → Account created` |
| Deposit | Adds balance to an existing account | `Deposit 5000 → New balance: 25000.50` |
| Withdraw | Deducts balance if sufficient funds | `Withdraw 2000 → Remaining: 23000.50` |
| Transfer | Moves funds between two accounts | `Transfer 1000 from A1 → A2` |
| Loan Approval | Admin approves or rejects pending loans | `Loan #302 → Approved` |

## 📊 Database Schema
Key Entities:
- **Branch** → `branch_id`, `branch_name`, `branch_address`
- **Employee** → `emp_id`, `emp_name`, `emp_position`, `branch_id`
- **Customer** → `customer_id`, `name`, `email`, `phone`, `address`, `branch_id`
- **Account** → `account_no`, `balance`, `customer_id`
- **Txn (Weak Entity)** → `txn_no`, `txn_type`, `tnx_date`, `amount`, `account_no`
- **Loan** → `loan_id`, `loan_type`, `loan_amount`, `status`, `customer_id`
- **Login** → `login_id`, `username`, `password`, `customer_id`

## 🧠 Example SQL Queries
- **List of All Customers**
  ```sql
  SELECT customer_id, name, email, phone, branch_id
  FROM Customer
  ORDER BY name ASC;
  ```
- **Accounts with Balance ≥ 50,000**
  ```sql
  SELECT account_no, customer_id, balance
  FROM Account
  WHERE balance >= 50000
  ORDER BY balance DESC;
  ```
- **Customer Name with Branch**
  ```sql
  SELECT c.name, b.branch_name
  FROM Customer c
  JOIN Branch b ON c.branch_id = b.branch_id;
  ```

## 📸 Sample Output (Console)
```
=== BANKING MANAGEMENT SYSTEM ===
1. Create Account
2. Login
3. Exit

Enter your choice: 1
Enter Name: Sabbir Hossain
Enter Email: sabbir@gmail.com
Phone:xxxxxxxxxxx
Address:xxxxxxx
Account created successfully! Account No: 100000011
```

## 📄 License
This project is open-source and available under the **MIT License**.  
You are free to use, modify, and distribute this software with proper credit.

## 👨‍💻 Author
**Md Rakibul Islam**  
🎓 *Department of CSE, Dhaka International University*  
📧 `mdrakibulislam10009@gmail.com`
