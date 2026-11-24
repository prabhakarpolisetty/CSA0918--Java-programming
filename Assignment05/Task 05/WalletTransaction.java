import java.sql.*;
public class WalletTransaction {
    public static void transfer(Connection con,int fromId,int toId,double amount)throws SQLException {
        if(amount<=0)throw new IllegalArgumentException("Amount must be positive");
        boolean old=con.getAutoCommit();con.setAutoCommit(false);
        try(PreparedStatement debit=con.prepareStatement("UPDATE wallet SET balance=balance-? WHERE wallet_id=? AND balance>=?");
            PreparedStatement credit=con.prepareStatement("UPDATE wallet SET balance=balance+? WHERE wallet_id=?")) {
            debit.setDouble(1,amount);debit.setInt(2,fromId);debit.setDouble(3,amount);
            if(debit.executeUpdate()!=1)throw new SQLException("Insufficient balance or wallet not found");
            credit.setDouble(1,amount);credit.setInt(2,toId);
            if(credit.executeUpdate()!=1)throw new SQLException("Destination wallet not found");
            con.commit();
        } catch(SQLException|RuntimeException e) { con.rollback();throw e; }
        finally { con.setAutoCommit(old); }
    }
    public static boolean login(Connection con,String id,String password)throws SQLException {
        try(PreparedStatement p=con.prepareStatement("SELECT 1 FROM users WHERE id=? AND pwd=?")) {
            p.setString(1,id);p.setString(2,password);
            try(ResultSet r=p.executeQuery()){return r.next();}
        }
    }
}