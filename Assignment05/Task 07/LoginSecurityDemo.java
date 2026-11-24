import java.sql.*;
public class LoginSecurityDemo {
    static boolean unsafeLogin(Connection c,String id,String pwd)throws SQLException {
        String sql="SELECT * FROM users WHERE id='"+id+"' AND pwd='"+pwd+"'";
        try(Statement s=c.createStatement();ResultSet r=s.executeQuery(sql)){return r.next();}
    }
    static boolean safeLogin(Connection c,String id,String pwd)throws SQLException {
        try(PreparedStatement p=c.prepareStatement("SELECT * FROM users WHERE id=? AND pwd=?")) {
            p.setString(1,id);p.setString(2,pwd);
            try(ResultSet r=p.executeQuery()){return r.next();}
        }
    }
    public static void main(String[] args)throws Exception {
        try(Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/java_assignment5","root","password")) {
            System.out.println("Unsafe login: "+unsafeLogin(c,"' OR '1'='1' -- ","wrong"));
            System.out.println("PreparedStatement login: "+safeLogin(c,"' OR '1'='1' -- ","wrong"));
        }
    }
}