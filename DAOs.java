package db;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import db.Models.*;

public class DAOs {
    // Helper: next IDs
    public static class Ids {
        public static int nextIntId(Connection con, String table, String idCol) throws SQLException {
            String sql = "SELECT COALESCE(MAX(" + idCol + "),0)+1 as next_id FROM " + table;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("next_id");
            }
        }

        public static long nextAccountNo(Connection con) throws SQLException {

            String sql = "SELECT COALESCE(MAX(account_no), 100000010) + 1 AS next_acc FROM Account";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("next_acc");
            }
        }

        public static int nextTxnNoForAccount(Connection con, long accountNo) throws SQLException {
            String sql = "SELECT COALESCE(MAX(txn_no),0)+1 AS next_txn FROM Txn WHERE account_no = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, accountNo);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getInt("next_txn");
                }
            }
        }
    }

    // Customer DAO
    public static class CustomerDAO {
        public int insertCustomer(Connection con, Customer c) throws SQLException {
            String sql = "INSERT INTO Customer(customer_id,name,email,phone,address,branch_id) VALUES(?,?,?,?,?,?)";
            int nextId = Ids.nextIntId(con, "Customer", "customer_id");
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, nextId);
                ps.setString(2, c.name);
                ps.setString(3, c.email);
                ps.setString(4, c.phone);
                ps.setString(5, c.address);
                ps.setInt(6, c.branchId);
                ps.executeUpdate();
            }
            return nextId;
        }

        public Customer findById(Connection con, int customerId) throws SQLException {
            String sql = "SELECT customer_id,name,email,phone,address,branch_id FROM Customer WHERE customer_id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Customer c = new Customer();
                        c.customerId = rs.getInt(1);
                        c.name = rs.getString(2);
                        c.email = rs.getString(3);
                        c.phone = rs.getString(4);
                        c.address = rs.getString(5);
                        c.branchId = rs.getInt(6);
                        return c;
                    }
                }
            }
            return null;
        }
    }

    // Account DAO
    public static class AccountDAO {
        public long createAccount(Connection con, int customerId) throws SQLException {
            long accNo = Ids.nextAccountNo(con);
            String sql = "INSERT INTO Account(account_no,balance,customer_id) VALUES(?,0.00,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, accNo);
                ps.setInt(2, customerId);
                ps.executeUpdate();
            }
            return accNo;
        }

        public BigDecimal getBalance(Connection con, long accountNo) throws SQLException {
            String sql = "SELECT balance FROM Account WHERE account_no=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, accountNo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBigDecimal(1);
                    }
                }
            }
            return null;
        }

        public boolean setBalance(Connection con, long accountNo, BigDecimal newBal) throws SQLException {
            String sql = "UPDATE Account SET balance=? WHERE account_no=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setBigDecimal(1, newBal);
                ps.setLong(2, accountNo);
                return ps.executeUpdate() == 1;
            }
        }

        public boolean exists(Connection con, long accountNo) throws SQLException {
            String sql = "SELECT 1 FROM Account WHERE account_no=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, accountNo);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }

        public boolean deleteByCustomer(Connection con, int customerId) throws SQLException {
            String sql = "DELETE FROM Account WHERE customer_id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                ps.executeUpdate();
            }
            String sqlC = "DELETE FROM Customer WHERE customer_id=?";
            try (PreparedStatement ps = con.prepareStatement(sqlC)) {
                ps.setInt(1, customerId);
                return ps.executeUpdate() >= 1;
            }
        }
    }

    // Txn DAO
    public static class TxnDAO {
        public void insertTxn(Connection con, long accountNo, String type, BigDecimal amt) throws SQLException {
            int nextTxn = Ids.nextTxnNoForAccount(con, accountNo);
            String sql = "INSERT INTO Txn(txn_no, txn_type, amount, account_no) VALUES(?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, nextTxn);
                ps.setString(2, type);
                ps.setBigDecimal(3, amt);
                ps.setLong(4, accountNo);
                ps.executeUpdate(); // tnx_date defaults to CURRENT_TIMESTAMP
            }
        }

        public List<Txn> findByAccount(Connection con, long accountNo) throws SQLException {
            String sql = "SELECT txn_no, txn_type, tnx_date, amount, account_no FROM Txn WHERE account_no=? ORDER BY tnx_date DESC, txn_no DESC";
            List<Txn> list = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, accountNo);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Txn t = new Txn();
                        t.txnNo = rs.getInt(1);
                        t.txnType = rs.getString(2);
                        Timestamp ts = rs.getTimestamp(3);
                        t.tnxDate = ts != null ? ts.toLocalDateTime() : null;
                        t.amount = rs.getBigDecimal(4);
                        t.accountNo = rs.getLong(5);
                        list.add(t);
                    }
                }
            }
            return list;
        }
    }

    // Login DAO
    public static class LoginDAO {
        public int createLogin(Connection con, int customerId, String username, String password) throws SQLException {
            int nextId = Ids.nextIntId(con, "Login", "login_id");
            String sql = "INSERT INTO Login(login_id,username,password,customer_id) VALUES(?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, nextId);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setInt(4, customerId);
                ps.executeUpdate();
            }
            return nextId;
        }

        public Integer auth(Connection con, String username, String password) throws SQLException {
            String sql = "SELECT customer_id FROM Login WHERE username=? AND password=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
            return null;
        }
    }

    //  Loan DAO
    public static class LoanDAO {
        public int apply(Connection con, int customerId, String loanType, BigDecimal amount) throws SQLException {
            int nextId = Ids.nextIntId(con, "Loan", "loan_id");
            String sql = "INSERT INTO Loan(loan_id, loan_type, loan_amount, status, customer_id) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, nextId);
                ps.setString(2, loanType);
                ps.setBigDecimal(3, amount);
                ps.setString(4, "Pending");
                ps.setInt(5, customerId);
                ps.executeUpdate();
            }
            return nextId;
        }

        public List<Loan> listByCustomer(Connection con, int customerId) throws SQLException {
            List<Loan> list = new ArrayList<>();
            String sql = "SELECT loan_id, loan_type, loan_amount, status, customer_id FROM Loan WHERE customer_id=? ORDER BY loan_id DESC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Loan l = new Loan();
                        l.loanId = rs.getInt(1);
                        l.loanType = rs.getString(2);
                        l.loanAmount = rs.getBigDecimal(3);
                        l.status = rs.getString(4);
                        l.customerId = rs.getInt(5);
                        list.add(l);
                    }
                }
            }
            return list;
        }
    }

    // Employee DAO
    public static class EmployeeDAO {
        public List<Employee> listAll(Connection con) throws SQLException {
            List<Employee> list = new ArrayList<>();
            String sql = "SELECT emp_id, emp_name, emp_position, branch_id FROM Employee ORDER BY emp_id";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee e = new Employee();
                    e.empId = rs.getInt(1);
                    e.empName = rs.getString(2);
                    e.empPosition = rs.getString(3);
                    e.branchId = rs.getInt(4);
                    list.add(e);
                }
            }
            return list;
        }
    }
}
