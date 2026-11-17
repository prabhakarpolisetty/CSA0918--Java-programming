import java.util.*;

class Student {
    private final String regNo;
    private final String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student other = (Student) o;
        return Objects.equals(regNo, other.regNo);
    }

    // TODO 1 completed: equal registration numbers produce equal hash codes.
    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class Attendance {
    public static void main(String[] args) {
        Set<Student> present = new HashSet<>();
        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));
        present.add(new Student("192311001", "Arun"));
        System.out.println("Unique students: " + present.size());

        // Additional tests
        Set<Student> sameIdDifferentName = new HashSet<>();
        sameIdDifferentName.add(new Student("R101", "Asha"));
        sameIdDifferentName.add(new Student("R101", "Asha K"));
        System.out.println("Same registration number, different name: " + sameIdDifferentName.size());

        Map<String, Student> byRegNo = new HashMap<>();
        byRegNo.put("R101", new Student("R101", "Asha"));
        byRegNo.put("R102", new Student("R102", "Rahul"));
        System.out.println("HashMap-based unique records: " + byRegNo.size());

        System.out.println("Expected output: Unique students: 2");
    }
}
