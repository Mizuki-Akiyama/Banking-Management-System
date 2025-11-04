package db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Models {

    // Entities matching
    public static class Customer {
        public int customerId;
        public String name;
        public String email;
        public String phone;
        public String address;
        public int branchId;
    }

    public static class Branch {
        public int branchId;
        public String branchName;
        public String branchAddress;
    }

    public static class Employee {
        public int empId;
        public String empName;
        public String empPosition;
        public int branchId;
    }

    public static class Account {
        public long accountNo;
        public BigDecimal balance;
        public int customerId;
    }

    public static class Txn {
        public int txnNo;
        public String txnType;           // Deposit / Withdraw / Transfer
        public LocalDateTime tnxDate;    // maps TIMESTAMP
        public BigDecimal amount;
        public long accountNo;
    }

    public static class Loan {
        public int loanId;
        public String loanType;
        public BigDecimal loanAmount;
        public String status;    // Pending/Approved/Rejected
        public int customerId;
    }

    public static class Login {
        public int loginId;
        public String username;
        public String password;
        public int customerId;
    }
}
