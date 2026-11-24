import java.sql.*;
import java.util.*;

interface Dao<T,K> {
    void insert(T value)throws SQLException;
    Optional<T> findById(K id)throws SQLException;
    List<T> findAll()throws SQLException;
    void update(T value)throws SQLException;
    void delete(K id)throws SQLException;
}
class Student {
    private final int id;private final String name,email;
    Student(int id,String name,String email){this.id=id;this.name=name;this.email=email;}
    int getId(){return id;}String getName(){return name;}String getEmail(){return email;}
}
public class StudentDao implements Dao<Student,Integer> {
    private final Connection connection;
    public StudentDao(Connection connection){this.connection=connection;}
    public void insert(Student s)throws SQLException {
        try(PreparedStatement p=connection.prepareStatement("INSERT INTO student(id,name,email) VALUES(?,?,?)")){
            p.setInt(1,s.getId());p.setString(2,s.getName());p.setString(3,s.getEmail());p.executeUpdate();
        }
    }
    public Optional<Student> findById(Integer id)throws SQLException {
        try(PreparedStatement p=connection.prepareStatement("SELECT id,name,email FROM student WHERE id=?")){
            p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(new Student(r.getInt(1),r.getString(2),r.getString(3))):Optional.empty();}
        }
    }
    public List<Student> findAll()throws SQLException {
        List<Student> out=new ArrayList<>();
        try(Statement s=connection.createStatement();ResultSet r=s.executeQuery("SELECT id,name,email FROM student")){
            while(r.next())out.add(new Student(r.getInt(1),r.getString(2),r.getString(3)));
        }return out;
    }
    public void update(Student s)throws SQLException {
        try(PreparedStatement p=connection.prepareStatement("UPDATE student SET name=?,email=? WHERE id=?")){
            p.setString(1,s.getName());p.setString(2,s.getEmail());p.setInt(3,s.getId());p.executeUpdate();
        }
    }
    public void delete(Integer id)throws SQLException {
        try(PreparedStatement p=connection.prepareStatement("DELETE FROM student WHERE id=?")){p.setInt(1,id);p.executeUpdate();}
    }
}