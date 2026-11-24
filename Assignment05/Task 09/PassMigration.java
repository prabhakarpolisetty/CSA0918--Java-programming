import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
public class PassMigration {
    public static void main(String[] args)throws Exception {
        Path file=Path.of(args.length>0?args[0]:"bus-passes.csv");
        List<String[]> valid=new ArrayList<>();List<String> rejected=new ArrayList<>();
        try(BufferedReader r=Files.newBufferedReader(file)) {
            String line;int number=0;
            while((line=r.readLine())!=null) {
                number++;String[] p=line.split(",",-1);
                try {
                    if(p.length!=5||p[0].isBlank()||p[1].isBlank()||p[2].isBlank())throw new IllegalArgumentException("Invalid fields");
                    int route=Integer.parseInt(p[3]);double amount=Double.parseDouble(p[4]);
                    if(route<0||amount<0)throw new IllegalArgumentException("Negative route or amount");
                    valid.add(p);
                } catch(RuntimeException e) { rejected.add("Line "+number+": "+e.getMessage()); }
            }
        }
        try(Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/java_assignment5","root","password")) {
            System.out.println("Loaded: "+PassLoader.loadAll(c,valid));
            DatabaseMetaData m=c.getMetaData();
            System.out.println("Database: "+m.getDatabaseProductName());
            System.out.println("Driver: "+m.getDriverName()+" "+m.getDriverVersion());
            try(ResultSet t=m.getTables(null,null,"%",new String[]{"TABLE"})) {
                while(t.next())System.out.println("Table: "+t.getString("TABLE_NAME"));
            }
        }
        System.out.println("Rejected: "+rejected.size());for(String e:rejected)System.out.println(e);
    }
}