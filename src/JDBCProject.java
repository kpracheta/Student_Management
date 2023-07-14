import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class JDBCProject {
    private JFrame jFrame;
    private JTextField nameField;
    private JTextField regField;
    private JTextField deptField;
    private JPanel displayPanel;
    private JScrollPane scrollPane;

    private Connection connection;

    public JDBCProject() {
        jFrame = new JFrame();
        jFrame.setTitle("JDBC PROJECT");

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(20, 40, 100, 20);

        nameField = new JTextField();
        nameField.setBounds(150, 40, 200, 40);

        JLabel regLabel = new JLabel("Registration no.");
        regLabel.setBounds(20, 90, 100, 20);

        regField = new JTextField();
        regField.setBounds(150, 90, 200, 40);

        JLabel deptLabel = new JLabel("Department");
        deptLabel.setBounds(20, 140, 100, 20);

        deptField = new JTextField();
        deptField.setBounds(150, 140, 200, 40);

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setBounds(20, 290, 350, 100);

        scrollPane = new JScrollPane(displayPanel);
        scrollPane.setBounds(20, 290, 350, 100);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        jFrame.add(nameLabel);
        jFrame.add(nameField);
        jFrame.add(regLabel);
        jFrame.add(regField);
        jFrame.add(deptLabel);
        jFrame.add(deptField);
        jFrame.add(scrollPane);

        JButton saveButton = new JButton("SAVE");
        saveButton.setBounds(50, 190, 110, 30);
        jFrame.add(saveButton);

        JButton updateButton = new JButton("UPDATE");
        updateButton.setBounds(220, 190, 110, 30);
        jFrame.add(updateButton);

        JButton deleteButton = new JButton("DELETE");
        deleteButton.setBounds(50, 240, 110, 30);
        jFrame.add(deleteButton);

        JButton displayButton = new JButton("DISPLAY");
        displayButton.setBounds(220, 240, 110, 30);
        jFrame.add(displayButton);

        JButton searchButton = new JButton("SEARCH");
        searchButton.setBounds(220, 140, 110, 30);
        jFrame.add(searchButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudents();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        jFrame.setLayout(null);
        jFrame.setSize(400, 450);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            String url = "jdbc:mysql://localhost:3306/dbms_project";
            String username = "root";
            String password = "Pass@123";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addStudent() {
        String id = regField.getText();
        String name = nameField.getText();
        String dept = deptField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (id, name, dept) VALUES (?, ?, ?)");
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, dept);
            statement.executeUpdate();

            regField.setText("");
            nameField.setText("");
            deptField.setText("");

            //loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateStudent() {
        String id = regField.getText();
        String name = nameField.getText();
        String dept = deptField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE students SET name=?, dept=? WHERE id=?");
            statement.setString(1, name);
            statement.setString(2, dept);
            statement.setString(3, id);
            statement.executeUpdate();

            nameField.setText("");
            regField.setText("");
            deptField.setText("");

            //loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteStudent() {
        String id = regField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE id=?");
            statement.setString(1, id);
            statement.executeUpdate();

            regField.setText("");
            nameField.setText("");
            deptField.setText("");

            //loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadStudents() {
        displayPanel.removeAll();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String dept = resultSet.getString("dept");

                JLabel studentLabel = new JLabel("ID: " + id + ", Name: " + name + ", Dept: " + dept + "\n");
                displayPanel.add(studentLabel);
            }

            jFrame.revalidate();
            jFrame.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchStudent() {
        String id = regField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE id = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            displayPanel.removeAll();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String dept = resultSet.getString("dept");

                JLabel studentLabel = new JLabel("ID: " + id + ", Name: " + name + ", Dept: " + dept + "\n");
                displayPanel.add(studentLabel);
            }

            jFrame.revalidate();
            jFrame.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDBCProject();
            }
        });
    }
}
