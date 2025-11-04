package db;

import java.sql.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import db.DAOs.*;
import db.Models.*;

public class Services {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TxnDAO txnDAO = new TxnDAO();
    private final LoginDAO loginDAO = new LoginDAO();
    private final LoanDAO loanDAO = new LoanDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    // Sign-up
    public static class SignUpResult {
        public int customerId;
        public long accountNo;
        public String username;
        public String password;
    }

    public SignUpResult createCustomerAccount(String name, String email, String phone, String address, int branchId) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                // 1) Create Customer
                Customer c = new Customer();
                c.name = name;
                c.email = email;
                c.phone = phone;
                c.address = address;
                c.branchId = branchId;
                int customerId = customerDAO.insertCustomer(con, c);

                // 2) Create Account with 0 balance
                long accNo = accountDAO.createAccount(con, customerId);

                // 3) Create Login
                String username = genUsername(name, customerId);
                String password = genPassword();

                loginDAO.createLogin(con, customerId, username, password);

                con.commit();

                SignUpResult r = new SignUpResult();
                r.customerId = customerId;
                r.accountNo = accNo;
                r.username = username;
                r.password = password;
                return r;
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    private static String genUsername(String name, int id) {
        String base = name.trim().toLowerCase().replaceAll("\\s+", "");
        if (base.length() > 10) base = base.substring(0, 10);
        return base + id;
    }

    private static String genPassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789@#";
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 10; i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }

    // Auth
    public Integer login(String username, String password) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            return new LoginDAO().auth(con, username, password);
        }
    }

    // Banking operations
    public BigDecimal checkBalance(long accountNo) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            return accountDAO.getBalance(con, accountNo);
        }
    }

    public void deposit(long accountNo, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive.");
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                if (!accountDAO.exists(con, accountNo)) throw new IllegalArgumentException("Account not found.");
                BigDecimal bal = accountDAO.getBalance(con, accountNo);
                BigDecimal newBal = bal.add(amount);
                accountDAO.setBalance(con, accountNo, newBal);
                txnDAO.insertTxn(con, accountNo, "Deposit", amount);
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally { con.setAutoCommit(true); }
        }
    }

    public void withdraw(long accountNo, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive.");
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                if (!accountDAO.exists(con, accountNo)) throw new IllegalArgumentException("Account not found.");
                BigDecimal bal = accountDAO.getBalance(con, accountNo);
                if (bal.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient balance.");
                BigDecimal newBal = bal.subtract(amount);
                accountDAO.setBalance(con, accountNo, newBal);
                txnDAO.insertTxn(con, accountNo, "Withdraw", amount);
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally { con.setAutoCommit(true); }
        }
    }

    public void transfer(long fromAccount, long toAccount, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (fromAccount == toAccount) throw new IllegalArgumentException("Cannot transfer to same account.");
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                if (!accountDAO.exists(con, fromAccount) || !accountDAO.exists(con, toAccount))
                    throw new IllegalArgumentException("One or both accounts do not exist.");

                BigDecimal balFrom = accountDAO.getBalance(con, fromAccount);
                if (balFrom.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient balance.");

                BigDecimal balTo = accountDAO.getBalance(con, toAccount);

                accountDAO.setBalance(con, fromAccount, balFrom.subtract(amount));
                accountDAO.setBalance(con, toAccount, balTo.add(amount));

                txnDAO.insertTxn(con, fromAccount, "Transfer", amount);
                txnDAO.insertTxn(con, toAccount, "Deposit", amount); // receiver sees a deposit

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally { con.setAutoCommit(true); }
        }
    }

    public List<Txn> transactionHistory(long accountNo) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            return txnDAO.findByAccount(con, accountNo);
        }
    }

    public boolean deleteAccountByCustomer(int customerId) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                boolean ok = accountDAO.deleteByCustomer(con, customerId);
                con.commit();
                return ok;
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally { con.setAutoCommit(true); }
        }
    }

    // Loan
    public int applyLoan(int customerId, String loanType, BigDecimal amount) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            return loanDAO.apply(con, customerId, loanType, amount);
        }
    }

    public List<Loan> loanStatus(int customerId) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            return loanDAO.listByCustomer(con, customerId);
        }
    }

    // Employees
    public List<Employee> employeeDetails() throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            return employeeDAO.listAll(con);
        }
    }
}
