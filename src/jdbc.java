import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class jdbc {
    //private JFrame jFrame;
    //private JTextField name;
    //private JTextField reg;
    //private JTextField dept;
    private Label name_res;
    private Label reg_res;
    private Label dept_res;

    private Connection connection;
    private JTextArea txtAreaStudent;


    public jdbc() {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("JDBC PROJECT");

        JLabel name = new JLabel("Name");
        name.setBounds(20, 40, 100, 20);

        //JButton jb = new JButton("Click Me");
        //jb.setBounds(20, 40, 110, 30);


        JTextField name_res = new JTextField();
        name_res.setBounds(150, 40, 200, 40);

        JLabel reg = new JLabel("Registration no.");
        reg.setBounds(20, 90, 100, 20);

        JTextField reg_res = new JTextField();
        reg_res.setBounds(150, 90, 200, 40);

        JLabel dept = new JLabel("Department");
        dept.setBounds(20, 140, 100, 20);

        JTextField dept_res = new JTextField();
        dept_res.setBounds(150, 140, 200, 40);

        txtAreaStudent = new JTextArea();
        txtAreaStudent.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaStudent);

        //jFrame.add(jl);

        jFrame.add(name_res);

        jFrame.add(name);

        jFrame.add(reg);

        jFrame.add(reg_res);

        jFrame.add(dept);

        jFrame.add(dept_res);

        jFrame.add(scrollPane, BorderLayout.CENTER);

        JButton save = new JButton("SAVE");
        save.setBounds(50, 190, 110, 30);
        /*save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = name.getText();

            }
        });*/
        jFrame.add(save);

        JButton update = new JButton("UPDATE");
        update.setBounds(220, 190, 110, 30);
        jFrame.add(update);

        JButton delete = new JButton("DELETE");
        delete.setBounds(50, 240, 110, 30);
        jFrame.add(delete);

        JButton display = new JButton("DISPLAY");
        display.setBounds(220, 240, 110, 30);
        jFrame.add(display);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addStudent();
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        jFrame.setLayout(null);
        jFrame.setSize(400, 400);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Establish the database connection
            String url = "jdbc:mysql://localhost:3306/dbms_project"; // Replace with your database URL
            String username = "root"; // Replace with your database username
            String password = "Pass@123"; // Replace with your database password
            connection = DriverManager.getConnection(url, username, password);

            loadstudent();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addStudent() {
        String id = reg_res.getText();
        String name = name_res.getText();
        String dept = dept_res.getText();

        try {
            // Create a prepared statement to insert the student record
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (id, name, dept) VALUES (?, ?, ?)");
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, dept);
            statement.executeUpdate();

            loadstudent();

            reg_res.setText("");
            name_res.setText("");
            dept_res.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateStudent() {
        String id = reg_res.getText();
        String name = name_res.getText();
        String dept = dept_res.getText();

        try {
            // Create a prepared statement to update the student record
            PreparedStatement statement = connection.prepareStatement("UPDATE students SET name=?, dept=? WHERE id=?");
            statement.setString(1, name);
            statement.setString(2, dept);
            statement.setString(3, id);
            statement.executeUpdate();

            loadstudent();

            name_res.setText("");
            reg_res.setText("");
            dept_res.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteStudent() {
        String id = reg_res.getText();

        try {
            // Create a prepared statement to delete the student record
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE id=?");
            statement.setString(1, id);
            statement.executeUpdate();

            loadstudent();

            reg_res.setText("");
            name_res.setText("");
            dept_res.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadstudent() {
        try {
            // Create a statement object to execute SQL queries
            Statement statement = connection.createStatement();

            // Execute the query to fetch all student records
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student");

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString("reg");
                String name = resultSet.getString("name");
                String rollNo = resultSet.getString("dept");
                sb.append("REG_NO.: ").append(id).append(", Name: ").append(name).append(", DEPT: ").append(rollNo).append("\n");
            }

            txtAreaStudent.setText(sb.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new jdbc_project().setVisible(true);
            }
        });
    }

}

    /*public static Connection getConnection(){
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String databaseUrl = "jdbc:mysql://localhost:3306/dbms_project";
            String userName = "root";
            String password = "Pass@123";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(databaseUrl, userName, password);
            System.out.println("Database created");
            return conn;
        }
        catch(Exception e){
            System.out.println("Some Error"+ e);
        }
        return null;
    }

    public static void getData(){
        try{
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery("Select * from student");
            while(result.next()) {
                System.out.println(result.getString("name"));
            }
        }
        catch(Exception e){
            System.out.println("Error "+e);
        }
    }

    public static void insertData(){
        try{
            Statement statement = getConnection().createStatement();
            //long reg_no = nextInt();
            int result = statement.executeUpdate("Insert into student(reg_no, name, dept) values (21070689, 'Anita Pawar', 'ETC')");
            System.out.println(result);
            if(result==1){
                System.out.println("data inserted");
            }else{
                System.out.println("some error");
            }

        }
        catch(Exception e){
            System.out.println("error "+e);
        }
    }
}*/
