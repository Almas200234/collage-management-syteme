package collage_management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.net.URL;

public class CollegeManagement {

    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CollegeManagement window = new CollegeManagement();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // --- CUSTOM PANEL FOR BACKGROUND IMAGES ---
    class BackgroundPanel extends JPanel {
        private Image img;
        public BackgroundPanel(String fileName) {
            try {
                URL imgURL = getClass().getResource("/res/" + fileName);
                if (imgURL != null) {
                    this.img = new ImageIcon(imgURL).getImage();
                } else {
                    System.out.println("Image not found: " + fileName);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(44, 62, 80)); // Fallback color
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public CollegeManagement() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("SND College Management System | Almas Ansari");
        frame.setBounds(100, 100, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frame.getContentPane().add(cardPanel, BorderLayout.CENTER);

        createLoginScreen();
        createDashboard();
    }

    // --- LOGIN SCREEN ---
    private void createLoginScreen() {
        BackgroundPanel loginPanel = new BackgroundPanel("bg.jpg"); 
        loginPanel.setLayout(new GridBagLayout());

        JPanel loginBox = new JPanel(null);
        loginBox.setPreferredSize(new Dimension(380, 420));
        loginBox.setBackground(new Color(255, 255, 255, 210)); 
        loginBox.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 2));
        
        JLabel lblTitle = new JLabel("SYSTEM LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBounds(0, 40, 380, 40);
        loginBox.add(lblTitle);

        JTextField txtUser = new JTextField("Admin");
        txtUser.setBounds(65, 130, 250, 40);
        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));
        loginBox.add(txtUser);

        JPasswordField txtPass = new JPasswordField("password");
        txtPass.setBounds(65, 190, 250, 40);
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));
        loginBox.add(txtPass);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(65, 270, 250, 45);
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));
        loginBox.add(btnLogin);

        loginPanel.add(loginBox);
        cardPanel.add(loginPanel, "LOGIN");
    }

    // --- DASHBOARD NAVIGATION ---
    private void createDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout());
        
        JPanel sidebar = new JPanel(new GridLayout(10, 1, 10, 10));
        sidebar.setBackground(new Color(30, 39, 46));
        sidebar.setPreferredSize(new Dimension(230, 0));
        
        JButton btnHome = createSideBtn("DASHBOARD");
        JButton btnAdd = createSideBtn("ADD STUDENT");
        JButton btnView = createSideBtn("VIEW RECORDS");
        JButton btnLogout = createSideBtn("LOGOUT");

        sidebar.add(btnHome); sidebar.add(btnAdd); sidebar.add(btnView); sidebar.add(btnLogout);
        dashboard.add(sidebar, BorderLayout.WEST);

        CardLayout contentLayout = new CardLayout();
        JPanel contentPanel = new JPanel(contentLayout);
        dashboard.add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(createAttractiveHome(), "HOME");
        contentPanel.add(createAddStudentForm(), "ADD");
        contentPanel.add(createViewRecordsPage(), "VIEW");

        btnHome.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        btnAdd.addActionListener(e -> contentLayout.show(contentPanel, "ADD"));
        btnView.addActionListener(e -> { loadTableData(); contentLayout.show(contentPanel, "VIEW"); });
        btnLogout.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));

        cardPanel.add(dashboard, "DASHBOARD");
    }

    // --- ATTRACTIVE HOME (CENTERED WITH BACKGROUND) ---
    private JPanel createAttractiveHome() {
        BackgroundPanel homeBg = new BackgroundPanel("student.jpg");
        homeBg.setLayout(new GridBagLayout());

        JPanel glassPanel = new JPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBackground(new Color(255, 255, 255, 190));
        glassPanel.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));

        JLabel welcome = new JLabel("Admin Dashboard");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 42));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(welcome);

        glassPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        String info = "<html><center><font size='6' color='#2c3e50'>Welcome, <b>Almas Ansari</b></font><br><br>"
                    + "<font size='5'>SND College of Engineering<br>"
                    + "Department of IT<br><br>"
                    + "<font color='green'>● Database Connected</font></font></center></html>";
        
        JLabel lblInfo = new JLabel(info);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(lblInfo);

        homeBg.add(glassPanel);
        return homeBg;
    }

    // --- ADD STUDENT FORM WITH CONTACT ---
    private JPanel createAddStudentForm() {
        JPanel form = new JPanel(null);
        form.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Register New Student", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(0, 50, 900, 40);
        form.add(title);

        String[] labels = {"Full Name:", "Department:", "Enrollment No:", "Contact No:"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBounds(280, 150 + (i * 60), 150, 30);
            form.add(lbl);

            fields[i] = new JTextField();
            fields[i].setBounds(430, 150 + (i * 60), 250, 35);
            form.add(fields[i]);
        }
        
        fields[1].setText("BE IT");

        JButton btnSave = new JButton("SAVE STUDENT RECORD");
        btnSave.setBounds(330, 450, 300, 50);
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSave.setFocusPainted(false);

        btnSave.addActionListener(e -> {
            saveToDatabase(fields[0].getText(), fields[1].getText(), fields[2].getText(), fields[3].getText());
            for(JTextField f : fields) f.setText("");
            fields[1].setText("BE IT");
        });

        form.add(btnSave);
        return form;
    }

    // --- DATABASE OPERATIONS ---
    private void saveToDatabase(String n, String d, String e, String c) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_db", "root", "root")) {
            String sql = "INSERT INTO students (name, department, enrollment_no, contact) VALUES (?, ?, ?, ?)";
            PreparedStatement p = conn.prepareStatement(sql);
            p.setString(1, n); p.setString(2, d); p.setString(3, e); p.setString(4, c);
            p.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Student Registered Successfully!");
        } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage()); }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_db", "root", "root");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("department"), rs.getString("enrollment_no"), rs.getString("contact")});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JPanel createViewRecordsPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Dept", "Enroll No", "Contact"}, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(35);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        p.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return p;
    }

    private JButton createSideBtn(String text) {
        JButton b = new JButton(text);
        b.setForeground(Color.WHITE); b.setBackground(new Color(30, 39, 46));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}