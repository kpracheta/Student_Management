import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class jdbc_project extends JFrame {
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtRollNo;
    private JTextArea txtAreaStudents;

    private Connection connection;

    public jdbc_project() {
        setTitle("Student Management System");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        //setLocationRelativeTo(null);
        //etLayout(new BorderLayout());

        JPanel panelInput = new JPanel();
        panelInput.setLayout(new GridLayout(4, 2));

        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField();
        JLabel lblRollNo = new JLabel("Roll No:");
        txtRollNo = new JTextField();
        JButton btnAdd = new JButton("Add Student");
        JButton btnUpdate = new JButton("Update Student");
        JButton btnDelete = new JButton("Delete Student");

        panelInput.add(lblId);
        panelInput.add(txtId);
        panelInput.add(lblName);
        panelInput.add(txtName);
        panelInput.add(lblRollNo);
        panelInput.add(txtRollNo);
        panelInput.add(new JLabel());
        panelInput.add(btnAdd);
        panelInput.add(new JLabel());
        panelInput.add(btnUpdate);
        panelInput.add(new JLabel());
        panelInput.add(btnDelete);

        add(panelInput, BorderLayout.NORTH);

        JPanel panelOutput = new JPanel();
        panelOutput.setLayout(new BorderLayout());

        txtAreaStudents = new JTextArea();
        txtAreaStudents.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaStudents);

        panelOutput.add(scrollPane, BorderLayout.CENTER);

        add(panelOutput, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        try {
            // Establish the database connection
            String url = "jdbc:mysql://localhost:3306/student_management"; // Replace with your database URL
            String username = "root"; // Replace with your database username
            String password = "Pass@123"; // Replace with your database password
            connection = DriverManager.getConnection(url, username, password);

            loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addStudent() {
        String id = txtId.getText();
        String name = txtName.getText();
        String rollNo = txtRollNo.getText();

        try {
            // Create a prepared statement to insert the student record
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (id, name, roll_no) VALUES (?, ?, ?)");
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, rollNo);
            statement.executeUpdate();

            loadStudents();

            txtId.setText("");
            txtName.setText("");
            txtRollNo.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateStudent() {
        String id = txtId.getText();
        String name = txtName.getText();
        String rollNo = txtRollNo.getText();

        try {
            // Create a prepared statement to update the student record
            PreparedStatement statement = connection.prepareStatement("UPDATE students SET name=?, roll_no=? WHERE id=?");
            statement.setString(1, name);
            statement.setString(2, rollNo);
            statement.setString(3, id);
            statement.executeUpdate();

            loadStudents();

            txtId.setText("");
            txtName.setText("");
            txtRollNo.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteStudent() {
        String id = txtId.getText();

        try {
            // Create a prepared statement to delete the student record
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE id=?");
            statement.setString(1, id);
            statement.executeUpdate();

            loadStudents();

            txtId.setText("");
            txtName.setText("");
            txtRollNo.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadStudents() {
        try {
            // Create a statement object to execute SQL queries
            Statement statement = connection.createStatement();

            // Execute the query to fetch all student records
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String rollNo = resultSet.getString("roll_no");
                sb.append("ID: ").append(id).append(", Name: ").append(name).append(", Roll No: ").append(rollNo).append("\n");
            }

            txtAreaStudents.setText(sb.toString());
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
