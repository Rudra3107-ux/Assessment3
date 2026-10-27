import java.sql.*;
import java.util.Scanner;
public class test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college",
                "root",
                "12345678"
            );
            Statement st = con.createStatement();
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS students (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "mark1 INT, " +
                "mark2 INT, " +
                "mark3 INT)"
            );
            int choice;
            System.out.println("\n1. Add Student Marks");
            System.out.println("2. Update Student Marks");
            System.out.println("3. Display Students with Average Above 75");
            System.out.println("4. Exit");
            do {
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                if (choice == 1) {
                    System.out.print("Enter student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter student name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter marks in three subjects: ");
                    int m1 = sc.nextInt();
                    int m2 = sc.nextInt();
                    int m3 = sc.nextInt();
                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO students VALUES (?, ?, ?, ?, ?)"
                    );
                    ps.setInt(1, id);
                    ps.setString(2, name);
                    ps.setInt(3, m1);
                    ps.setInt(4, m2);
                    ps.setInt(5, m3);
                    ps.executeUpdate();
                    int total = m1 + m2 + m3;
                    double average = total / 3.0;
                    System.out.println("Student added successfully");
                    System.out.println("Total Marks: " + total);
                    System.out.println("Average Marks: " + average);
                    ps.close();
                }
                else if (choice == 2) {
                    System.out.print("Enter student ID: ");
                    int id = sc.nextInt();
                    System.out.print("Enter new marks in three subjects: ");
                    int m1 = sc.nextInt();
                    int m2 = sc.nextInt();
                    int m3 = sc.nextInt();
                    PreparedStatement ps = con.prepareStatement(
                        "UPDATE students SET mark1=?, mark2=?, mark3=? WHERE id=?"
                    );
                    ps.setInt(1, m1);
                    ps.setInt(2, m2);
                    ps.setInt(3, m3);
                    ps.setInt(4, id);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        int total = m1 + m2 + m3;
                        double average = total / 3.0;
                        System.out.println("Marks updated successfully");
                        System.out.println("Total Marks: " + total);
                        System.out.println("Average Marks: " + average);
                    } else {
                        System.out.println("Student not found");
                    }
                    ps.close();
                }
                else if (choice == 3) {
                    Statement st2 = con.createStatement();
                    ResultSet rs = st2.executeQuery(
                        "SELECT *, (mark1 + mark2 + mark3) AS total, " +
                        "(mark1 + mark2 + mark3) / 3.0 AS average " +
                        "FROM students " +
                        "WHERE (mark1 + mark2 + mark3) / 3.0 > 75"
                    );
                    System.out.println("\nStudents with Average Above 75:");
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("id"));
                        System.out.println("Name: " + rs.getString("name"));
                        System.out.println("Total: " + rs.getInt("total"));
                        System.out.println("Average: " + rs.getDouble("average"));
                    }
                    rs.close();
                    st2.close();
                }
                else if (choice == 4) {
                    System.out.println("Exiting program");
                }
                else {
                    System.out.println("Invalid choice");
                }
            } while (choice != 4);
            st.close();
            con.close();
            sc.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}