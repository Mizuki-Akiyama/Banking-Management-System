package db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import db.Models.*;
import db.Services.SignUpResult;

public class App {

    private static final Services SVC = new Services();
    private static final Scanner IN = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Banking Management System ===");
            System.out.println("1) Create Account");
            System.out.println("2) Login");
            System.out.println("3) Exit");
            System.out.print("Choose: ");
            String ch = IN.nextLine().trim();
            switch (ch) {
                case "1": createAccountFlow(); break;
                case "2": loginFlow(); break;
                case "3": System.out.println("Goodbye!"); return;
                default:  System.out.println("Invalid Choice.");
            }
        }
    }

    // Create Account
    private static void createAccountFlow() {
        try {
            System.out.println("\nCreate Account");
            System.out.print("Name: ");    String name = IN.nextLine().trim();
            System.out.print("Email: ");   String email = IN.nextLine().trim();
            System.out.print("Phone: ");   String phone = IN.nextLine().trim();
            System.out.print("Address: "); String address = IN.nextLine().trim();
            System.out.print("Branch ID (e.g., 1-10 from seed): ");
            int branchId = Integer.parseInt(IN.nextLine().trim());

            SignUpResult r = SVC.createCustomerAccount(name, email, phone, address, branchId);
            System.out.println("\nAccount Created Successfully!");
            System.out.println("Customer ID : " + r.customerId);
            System.out.println("Account No  : " + r.accountNo);
            System.out.println("Username    : " + r.username);
            System.out.println("Password    : " + r.password);
            System.out.println("\nPress Enter to Go Back...");
            IN.nextLine();
        } catch (Exception e) {
            System.out.println("Failed to Create Account: " + e.getMessage());
        }
    }

    // Login
    private static void loginFlow() {
        try {
            System.out.println("\nLogin");
            System.out.print("Username: "); String u = IN.nextLine().trim();
            System.out.print("Password: "); String p = IN.nextLine().trim();

            Integer customerId = SVC.login(u, p);
            if (customerId == null) {
                System.out.println("Invalid Username or Password.");
                return;
            }
            System.out.println("Login successful. Customer ID = " + customerId);

            // After login
            System.out.print("Enter Your Account No: ");
            long acc = Long.parseLong(IN.nextLine().trim());

            while (true) {
                System.out.println("\n-- Main Menu --");
                System.out.println("1) Deposit");
                System.out.println("2) Withdraw");
                System.out.println("3) Transfer");
                System.out.println("4) Check Balance");
                System.out.println("5) Transaction History");
                System.out.println("6) Loan");
                System.out.println("7) Delete Account");
                System.out.println("8) Employee Details");
                System.out.println("9) Exit");
                System.out.print("Choose: ");
                String ch = IN.nextLine().trim();

                try {
                    switch (ch) {
                        case "1": handleDeposit(acc); break;
                        case "2": handleWithdraw(acc); break;
                        case "3": handleTransfer(acc); break;
                        case "4": handleCheckBalance(acc); break;
                        case "5": handleTxnHistory(acc); break;
                        case "6": handleLoanMenu(customerId); break;
                        case "7":
                            if (confirm("Are You Sure You Want to Delete Your Account (and customer)?")) {
                                boolean ok = SVC.deleteAccountByCustomer(customerId);
                                System.out.println(ok ? "Deleted." : "Nothing deleted.");
                                return; // back to top menu
                            }
                            break;
                        case "8": handleEmployeeDetails(); break;
                        case "9": return; // back to root
                        default:  System.out.println("Invalid Choice.");
                    }
                } catch (Exception opEx) {
                    System.out.println("Operation Failed: " + opEx.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }
    }

    // Operations
    private static void handleDeposit(long acc) throws Exception {
        System.out.print("Deposit Amount: ");
        BigDecimal amt = new BigDecimal(IN.nextLine().trim());
        SVC.deposit(acc, amt);
        System.out.println("Deposited " + amt + " Successfully.");
    }

    private static void handleWithdraw(long acc) throws Exception {
        System.out.print("Withdraw Amount: ");
        BigDecimal amt = new BigDecimal(IN.nextLine().trim());
        SVC.withdraw(acc, amt);
        System.out.println("Withdrew " + amt + " Successfully.");
    }

    private static void handleTransfer(long fromAcc) throws Exception {
        System.out.print("To Account No: ");
        long toAcc = Long.parseLong(IN.nextLine().trim());
        System.out.print("Amount: ");
        BigDecimal amt = new BigDecimal(IN.nextLine().trim());
        SVC.transfer(fromAcc, toAcc, amt);
        System.out.println("Transferred " + amt + " from " + fromAcc + " to " + toAcc + " successfully.");
    }

    private static void handleCheckBalance(long acc) throws Exception {
        BigDecimal bal = SVC.checkBalance(acc);
        System.out.println("Current balance: " + bal);
    }

    private static void handleTxnHistory(long acc) throws Exception {
        List<Txn> list = SVC.transactionHistory(acc);
        if (list.isEmpty()) {
            System.out.println("No Transactions.");
        } else {
            System.out.println("TxnNo | Type      | Date & Time           | Amount     | Account");
            for (Txn t : list) {
                System.out.printf("%5d | %-9s | %-20s | %10s | %d%n",
                        t.txnNo, t.txnType, String.valueOf(t.tnxDate), t.amount.toPlainString(), t.accountNo);
            }
        }
    }

    private static void handleLoanMenu(int customerId) throws Exception {
        while (true) {
            System.out.println("\n-- Loan --");
            System.out.println("1) Apply For Loan");
            System.out.println("2) Loan Status");
            System.out.println("3) Exit");
            System.out.print("Choose: ");
            String ch = IN.nextLine().trim();
            if ("1".equals(ch)) {
                System.out.print("Loan Type (Home/Car/Personal/Business/Education/Others): ");
                String type = IN.nextLine().trim();
                System.out.print("Loan Amount: ");
                String amtStr = IN.nextLine().trim();
                BigDecimal amt = new BigDecimal(amtStr);
                int loanId = SVC.applyLoan(customerId, type, amt);
                System.out.println("Loan request submitted. Loan ID: " + loanId + " (status=Pending)");
            } else if ("2".equals(ch)) {
                List<Loan> loans = SVC.loanStatus(customerId);
                if (loans.isEmpty()) {
                    System.out.println("No Loans Found.");
                } else {
                    System.out.println("LoanID | Type       | Amount     | Status");
                    for (Loan l : loans) {
                        System.out.printf("%6d | %-10s | %10s | %s%n",
                                l.loanId, l.loanType, l.loanAmount.toPlainString(), l.status);
                    }
                }
            } else if ("3".equals(ch)) {
                return;
            } else {
                System.out.println("Invalid Choice.");
            }
        }
    }

    private static void handleEmployeeDetails() throws Exception {
        List<Employee> emps = SVC.employeeDetails();
        System.out.println("EmpID | Name             | Position   | BranchID");
        for (Employee e : emps) {
            System.out.printf("%5d | %-16s | %-10s | %d%n", e.empId, e.empName, e.empPosition, e.branchId);
        }
    }

    private static boolean confirm(String msg) {
        System.out.print(msg + " (y/n): ");
        String ans = IN.nextLine().trim().toLowerCase();
        return ans.startsWith("y");
    }
}
