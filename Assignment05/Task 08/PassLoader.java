import java.sql.*;
import java.util.*;
public class PassLoader {
    public static int loadAll(Connection con,List<String[]> passes)throws SQLException {
        String sql="INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)";
        boolean old=con.getAutoCommit();con.setAutoCommit(false);
        try(PreparedStatement p=con.prepareStatement(sql)) {
            int n=0;
            for(String[] row:passes) {
                if(row.length!=5)throw new IllegalArgumentException("Five fields required");
                p.setString(1,row[0]);p.setString(2,row[1]);p.setString(3,row[2]);
                p.setInt(4,Integer.parseInt(row[3]));p.setDouble(5,Double.parseDouble(row[4]));
                p.addBatch();n++;if(n%1000==0)p.executeBatch();
            }
            if(n%1000!=0)p.executeBatch();con.commit();return n;
        } catch(SQLException|RuntimeException e) { con.rollback();throw e; }
        finally { con.setAutoCommit(old); }
    }
}