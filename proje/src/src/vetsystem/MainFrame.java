package vetsystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField lgnId;
    private JPasswordField lgnPassword;
    private String currentUserTc;
    private int petId;
    DefaultTableModel petsModel = new DefaultTableModel(new String[]{"ID","Pet Name", "Species", "Breed", "Birthdate"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel storeModel = new DefaultTableModel(new String[]{"ID", "Image", "Name", "Price $","Amount"}, 0) {
		private static final long serialVersionUID = 1L;
	    @Override
	    public Class<?> getColumnClass(int columnIndex) {
	        if (columnIndex == 1) {
	            return ImageIcon.class;
	        }
	        return Object.class;
	    }
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private ArrayList<BasketItem> basket = new ArrayList<>();
    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
		        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		        int screenWidth = screenSize.width;
		        int screenHeight = screenSize.height;
		        int frameWidth = frame.getSize().width;
		        int frameHeight = frame.getSize().height;
		        int x = (screenWidth - frameWidth) / 2;
		        int y = (screenHeight - frameHeight) / 2;
		        frame.setLocation(x, y);
				frame.setResizable(false);
				frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainFrame() {
    	Database.connect();
        setTitle("User Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(2, 2, 2, 3));
        contentPane.setLayout(new CardLayout());
        setContentPane(contentPane);
        
        JPanel logsgnPanel = createLoginPanel();
        JPanel signupPanel = createSignupPanel();
        JPanel mainPanel = createMainPanel();
        JPanel petsPanel = createPetsPanel();
        JPanel storePanel = createStorePanel();
        JPanel appointmentPanel = createAppointmentPanel();
        JPanel adminPanel = createAdminPanel();
        JPanel adminStorePanel = createAdminStorePanel();
        //JPanel profilePanel;

        contentPane.add(logsgnPanel, "Login");
        contentPane.add(signupPanel, "Signup");
        contentPane.add(mainPanel, "MainMenu");
        contentPane.add(petsPanel, "Pets");
        contentPane.add(storePanel, "Store");
        //contentPane.add(profilePanel, "Profile");
        contentPane.add(appointmentPanel, "Appointments");
        contentPane.add(adminPanel,"AdminMainMenu");
        contentPane.add(adminStorePanel,"AdminStore");

        CardLayout cl = (CardLayout) (contentPane.getLayout());
        cl.show(contentPane, "Login");
    }
    
    private JPanel createAdminPanel() {
        JPanel adminMainMenuPanel = new JPanel();
        adminMainMenuPanel.setBackground(new Color(0, 0, 0));
        adminMainMenuPanel.setLayout(null);
        
        RoundedButton animalbutton = new RoundedButton("LOGIN", 30);
        animalbutton.setText("ANIMAL TABLE");
        animalbutton.setForeground(new Color(0, 255, 0));
        animalbutton.setFont(new Font("Arial", Font.BOLD, 20));
        animalbutton.setBackground(new Color(0, 0, 0));
        animalbutton.setBounds(29, 62, 318, 50);
        adminMainMenuPanel.add(animalbutton);
        
        RoundedButton storebutton = new RoundedButton("LOGIN", 30);
        storebutton.setText("STORE SETTING");
        storebutton.setForeground(new Color(0, 255, 0));
        storebutton.setFont(new Font("Arial", Font.BOLD, 20));
        storebutton.setBackground(new Color(0, 0, 0));
        storebutton.setBounds(419, 62, 318, 50);
        adminMainMenuPanel.add(storebutton);
        storebutton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("AdminStore");
        	}
        });
        
        RoundedButton quitbutton = new RoundedButton("LOGIN", 30);
        quitbutton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("Login");
        		lgnPassword.setText("");
        		lgnId.setText("");
        	}
        });
        quitbutton.setText("QUIT");
        quitbutton.setForeground(new Color(0, 255, 0));
        quitbutton.setFont(new Font("Arial", Font.BOLD, 20));
        quitbutton.setBackground(new Color(0, 0, 0));
        quitbutton.setBounds(231, 408, 318, 67);
        adminMainMenuPanel.add(quitbutton);
    	
    	
    	return adminMainMenuPanel;
    }
    
    private JPanel createAdminStorePanel() {
        JPanel adminStorePanel = new JPanel();
        adminStorePanel.setBackground(SystemColor.desktop);
        adminStorePanel.setLayout(null);
        JLabel storeLabel = new JLabel("Mağaza Ürünleri", JLabel.CENTER);
        storeLabel.setForeground(new Color(0, 255, 0));
        storeLabel.setBounds(266, 5, 255, 55);
        storeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        adminStorePanel.add(storeLabel);
        
        JTable storeTable = new JTable();
        storeTable.setFont(new Font("Arial", Font.PLAIN, 20));
        storeTable.setModel(storeModel);
        storeTable.setRowHeight(100);
        JScrollPane scrollPane = new JScrollPane(storeTable);
        scrollPane.setBounds(0, 65, 781, 427);
        adminStorePanel.add(scrollPane);
        storeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = storeTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int productId = (int) storeModel.getValueAt(selectedRow, 0);
                        String currentName = (String) storeModel.getValueAt(selectedRow, 2);
                        double currentPrice = (double) storeModel.getValueAt(selectedRow, 3);
                        int currentAmount = (int) storeModel.getValueAt(selectedRow, 4);
                        String newName = JOptionPane.showInputDialog(null, "Eski ismi : "+currentName +"\nÜrün ismini girin:", "Ürün İsmi", JOptionPane.QUESTION_MESSAGE);
                        if (newName == null || newName.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Geçerli bir ürün ismi girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        double newPrice = 0;
                        while (true) {
                            String priceStr = JOptionPane.showInputDialog(null, "Eski fiyat : "+currentPrice+"\nÜrün fiyatını girin:", "Güncelleme", JOptionPane.QUESTION_MESSAGE);
                            if (priceStr == null) return;
                            try {
                                newPrice = Double.parseDouble(priceStr);
                                if (newPrice < 0) throw new NumberFormatException();
                                break;
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Geçerli bir fiyat girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        int newAmount = 0;
                        while (true) {
                            String amountStr = JOptionPane.showInputDialog(null, "Eski miktar : "+currentAmount+"\nÜrün miktarını girin:", "Güncelleme", JOptionPane.QUESTION_MESSAGE);
                            if (amountStr == null) return;
                            try {
                                newAmount = Integer.parseInt(amountStr);
                                if (newAmount < 0) throw new NumberFormatException();
                                break;
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Geçerli bir miktar girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        String updateQuery = "UPDATE store SET product_name = ?, price = ?, amount = ? WHERE product_id = ?";
                        try (PreparedStatement stmt = Database.getConnection().prepareStatement(updateQuery)) {
                            stmt.setString(1, newName);
                            stmt.setDouble(2, newPrice);
                            stmt.setInt(3, newAmount);
                            stmt.setInt(4, productId);
                            stmt.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Ürün başarıyla güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                            loadStoreData(storeModel);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < storeTable.getColumnCount(); i++) {
        	if(i != 1) {
                storeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);	
        	}
        }
        
        RoundedButton backButton = new RoundedButton("BACK", 30);
        backButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("AdminMainMenu");
        	}
        });
        backButton.setText("<");
        backButton.setForeground(new Color(0, 255, 0));
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(SystemColor.desktop);
        backButton.setBounds(5, 5, 55, 55);
        adminStorePanel.add(backButton);
        
        RoundedButton addItem = new RoundedButton("SIGN UP", 30);
        addItem.setText("ADD ITEM");
        addItem.setForeground(new Color(0, 255, 0));
        addItem.setFont(new Font("Arial", Font.BOLD, 20));
        addItem.setBackground(new Color(0, 0, 0));
        addItem.setBounds(110, 502, 250, 47);
        adminStorePanel.add(addItem);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/default/basket.png"));
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        RoundedButton backButton_1 = new RoundedButton("BACK", 30);
        backButton_1.setIcon(icon);
        backButton_1.setText("");
        backButton_1.setForeground(new Color(255, 255, 255));
        backButton_1.setFont(new Font("Arial", Font.BOLD, 35));
        backButton_1.setBackground(new Color(0, 255, 0));
        backButton_1.setBounds(666, 5, 93, 55);
        adminStorePanel.add(backButton_1);
        
        RoundedButton deleteItem = new RoundedButton("SIGN UP", 30);
        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int selectedRow = storeTable.getSelectedRow();

            	if (selectedRow == -1) {
            	    JOptionPane.showMessageDialog(null, "Lütfen silmek istediğiniz satırı seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            	    return;
            	}

            	int productId;
            	try {
            	    productId = (int) storeModel.getValueAt(selectedRow, 0);
            	} catch (ArrayIndexOutOfBoundsException ex) {
            	    JOptionPane.showMessageDialog(null, "Seçili satırın bilgisi alınamadı. Lütfen geçerli bir satır seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            	    return;
            	}

            	String deleteQuery = "DELETE FROM store WHERE product_id = ?";
            	try (PreparedStatement stmt = Database.getConnection().prepareStatement(deleteQuery)) {
            	    stmt.setInt(1, productId);
            	    stmt.executeUpdate();
            	    JOptionPane.showMessageDialog(null, "Seçilen ürün başarıyla silindi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            	    loadStoreData(storeModel);
            	} catch (SQLException ex) {
            	    ex.printStackTrace();
            	    JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            	}
            }
        });
        deleteItem.setText("DELETE ITEM");
        deleteItem.setForeground(Color.GREEN);
        deleteItem.setFont(new Font("Arial", Font.BOLD, 20));
        deleteItem.setBackground(Color.BLACK);
        deleteItem.setBounds(413, 502, 250, 47);
        adminStorePanel.add(deleteItem);
        addItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
                int returnValue = fileChooser.showOpenDialog(null);
                File selectedFile = null;
                byte[] imageBytes = null;

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    try (FileInputStream fis = new FileInputStream(selectedFile)) {
                        imageBytes = fis.readAllBytes();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Resim seçilemedi: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    imageBytes = null;
                }
                String productName = JOptionPane.showInputDialog(null, "Ürün ismini girin:", "Ürün İsmi", JOptionPane.QUESTION_MESSAGE);
                if (productName == null || productName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ürün ismi gerekli!", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int amount = 0;
                while (true) {
                    String amountStr = JOptionPane.showInputDialog(null, "Ürün miktarını girin:", "Miktar", JOptionPane.QUESTION_MESSAGE);
                    try {
                        amount = Integer.parseInt(amountStr);
                        if (amount < 0) throw new NumberFormatException();
                        break;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Geçerli bir miktar girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
                double price = 0;
                while (true) {
                    String priceStr = JOptionPane.showInputDialog(null, "Ürün ücretini girin:", "Ücret", JOptionPane.QUESTION_MESSAGE);
                    try {
                        price = Double.parseDouble(priceStr);
                        if (price < 0) throw new NumberFormatException();
                        break;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Geçerli bir ücret girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
                String insertQuery = "INSERT INTO store (product_name, amount, price, product_image) VALUES (?, ?, ?, ?)";
                try {
                    PreparedStatement stmt = Database.getConnection().prepareStatement(insertQuery);
                    stmt.setString(1, productName);
                    stmt.setInt(2, amount);
                    stmt.setDouble(3, price);
                    stmt.setBytes(4, imageBytes);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Ürün başarıyla eklendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }
                loadStoreData(storeModel);
            }
        });
        loadStoreData(storeModel);
        return adminStorePanel;
    }
    
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);

//********************* LABEL *********************//        
        
        JLabel idLabel = new JLabel("ID Number:");
        idLabel.setBounds(150, 100, 120, 30);
        idLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        loginPanel.add(idLabel);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150, 150, 120, 30);
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        loginPanel.add(passwordLabel);

//********************* TEXT FIELD *********************//
        
        lgnId = new JTextField();
        lgnId.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lgnId.setBounds(300, 100, 250, 30);
        loginPanel.add(lgnId);

        lgnPassword = new JPasswordField();
        lgnPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lgnPassword.setBounds(300, 150, 250, 30);
        loginPanel.add(lgnPassword);
        

//********************* BUTTONS *********************//
        
        RoundedButton loginButton = new RoundedButton("LOGIN", 30);
        loginButton.setBounds(300, 220, 250, 50);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginPanel.add(loginButton);
        loginButton.addActionListener(e -> {
            String id = lgnId.getText().trim();
            String password = new String(lgnPassword.getPassword()).trim();
            
            if(id.equals("admin") && password.equals("admin")) {
            	switchToPanel("AdminMainMenu");
            	return;
            }

            if (id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen ID ve Şifreyi girin!", "WARNING", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String loginQuery = "SELECT * FROM users WHERE tc = ? AND password = ?";
            try {
                PreparedStatement stmt = Database.getConnection().prepareStatement(loginQuery);
                stmt.setString(1, id);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                	currentUserTc = id;
                	switchToPanel("MainMenu");
                    JOptionPane.showMessageDialog(this, "Giriş başarılı!", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    lgnId.setText("");
                    lgnPassword.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "ID veya şifre hatalı!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        RoundedButton registerButton = new RoundedButton("REGISTER", 30);
        registerButton.setBounds(300, 290, 250, 50);
        registerButton.setBackground(new Color(34, 139, 34));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginPanel.add(registerButton);
        registerButton.addActionListener(e -> switchToPanel("Signup"));
        
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBounds(560, 150, 120, 30);
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    lgnPassword.setEchoChar((char) 0);
                } else {
                    lgnPassword.setEchoChar('*');
                }
            }
        });
        loginPanel.add(showPasswordCheckBox);
        
        return loginPanel;
    }
    
    private JPanel createSignupPanel() {
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(null);

//********************* LABEL *********************//
        
        JLabel idlbl = new JLabel("ID Number:");
        idlbl.setBounds(150, 177, 120, 30);
        idlbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
        signUpPanel.add(idlbl);
        
        JLabel passLbl = new JLabel("Password :");
        passLbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
        passLbl.setBounds(150, 226, 120, 30);
        signUpPanel.add(passLbl);
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblNewLabel.setBounds(363, 26, 45, 13);
        signUpPanel.add(lblNewLabel);
        
        JLabel nameLbl = new JLabel("Name:");
        nameLbl.setBounds(150, 70, 120, 30);
        nameLbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
        signUpPanel.add(nameLbl);
        
        JLabel surnameLbl = new JLabel("Surname:");
        surnameLbl.setBounds(150, 123, 120, 30);
        surnameLbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
        signUpPanel.add(surnameLbl);
        
        JLabel phoneLbl = new JLabel("Phone Nr:");
        phoneLbl.setBounds(150, 280, 120, 30);
        phoneLbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
        signUpPanel.add(phoneLbl);
        
        JLabel addressLbl = new JLabel("Address:");
        addressLbl.setBounds(150, 334, 120, 30);
        addressLbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
        signUpPanel.add(addressLbl);

//********************* TEXT FIELD *********************//
        
        JTextField sgnName = new JTextField();
        sgnName.setFont(new Font("Tahoma", Font.PLAIN, 15));
        sgnName.setBounds(300, 71, 250, 30);
        signUpPanel.add(sgnName);

        JTextField sgnSurname = new JTextField();
        sgnSurname.setFont(new Font("Tahoma", Font.PLAIN, 15));
        sgnSurname.setBounds(300, 124, 250, 30);
        signUpPanel.add(sgnSurname);
        
        JTextField sgnId = new JTextField();
        sgnId.setFont(new Font("Tahoma", Font.PLAIN, 15));
        sgnId.setBounds(300, 178, 250, 30);
        signUpPanel.add(sgnId);
        
        JTextField sgnPassword = new JTextField();
        sgnPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
        sgnPassword.setBounds(300, 227, 250, 30);
        signUpPanel.add(sgnPassword);
        
        JTextField sgnPhone = new JTextField();
        sgnPhone.setFont(new Font("Tahoma", Font.PLAIN, 15));
        sgnPhone.setBounds(300, 281, 250, 30);
        signUpPanel.add(sgnPhone);

        JTextField sgnAddress = new JTextField();
        sgnAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
        sgnAddress.setBounds(300, 335, 250, 30);
        signUpPanel.add(sgnAddress);

//********************* BUTTONS *********************//
        
        RoundedButton signupButton = new RoundedButton("SIGN UP", 30);
        signupButton.setBounds(251, 413, 250, 40);
        signupButton.setFont(new Font("Arial", Font.BOLD, 20));
        signupButton.setBackground(new Color(34, 139, 34));
        signupButton.setForeground(Color.WHITE);
        signUpPanel.add(signupButton);
        signupButton.addActionListener(e -> {
            String id = sgnId.getText().trim();
            String name = sgnName.getText().trim();
            String surname = sgnSurname.getText().trim();
            String phone = sgnPhone.getText().trim();
            String address = sgnAddress.getText().trim();
            String password = sgnPassword.getText().trim();
            
            // Validation
            if (id.isEmpty() || name.isEmpty() || surname.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "WARNING", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID alanı boş olamaz!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (id.length() != 11) {
                JOptionPane.showMessageDialog(this, "ID 11 haneli olmalıdır!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!id.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "ID sadece rakamlardan oluşmalıdır!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Telefon numarası sadece rakamlardan oluşmalıdır!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 6 || password.length() > 20 ) {
                JOptionPane.showMessageDialog(this, "Şifre en az 6 en fazla 20 karakter olmalıdır!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String checkUserQuery = "SELECT * FROM users WHERE tc = ?";
            try {
                PreparedStatement stmt = Database.getConnection().prepareStatement(checkUserQuery);
                stmt.setString(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Kullanıcı zaten mevcut!", "INFO", JOptionPane.INFORMATION_MESSAGE);
                    switchToPanel("Login");
                    lgnId.setText(id);
                } else {
                    String insertQuery = "INSERT INTO users (tc, password, fname, lname, phoneNr, address) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = Database.getConnection().prepareStatement(insertQuery);
                    insertStmt.setString(1, id);
                    insertStmt.setString(2, password);
                    insertStmt.setString(3, name);
                    insertStmt.setString(4, surname);
                    insertStmt.setString(5, phone);
                    insertStmt.setString(6, address);
                    int rowsInserted = insertStmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Kayıt başarılı!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                        lgnId.setText("");
                        lgnPassword.setText("");
                        sgnId.setText("");
                        sgnPassword.setText("");
                        sgnName.setText("");
                        sgnSurname.setText("");
                        sgnPhone.setText("");
                        sgnAddress.setText("");
                        switchToPanel("Login");
                    } else {
                        JOptionPane.showMessageDialog(this, "Kayıt sırasında bir hata oluştu!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        RoundedButton backButton = new RoundedButton("BACK", 30);
        backButton.setBounds(5, 5, 55, 55);
        backButton.setText("<");
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        signUpPanel.add(backButton);
        backButton.addActionListener(e -> {
            switchToPanel("Login");
            lgnId.setText("");
            lgnPassword.setText("");
            sgnId.setText("");
            sgnName.setText("");
            sgnSurname.setText("");
            sgnPhone.setText("");
            sgnAddress.setText("");
        });
        
        return signUpPanel;
    }
    
    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 781, 43);
        menuBar.setForeground(new Color(255, 255, 0));

        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/icon/profile.png"));
        ImageIcon animalsIcon = new ImageIcon(getClass().getResource("/icon/pets.png"));
        ImageIcon storeIcon = new ImageIcon(getClass().getResource("/icon/store.png"));
        ImageIcon exitIcon = new ImageIcon(getClass().getResource("/icon/exit.png"));

        JMenu mainMenu = new JMenu("--- Menu ---");
        menuBar.add(mainMenu);

        JMenuItem profileMenuItem = new JMenuItem("PROFILE", profileIcon);
        mainMenu.add(profileMenuItem);
        profileMenuItem.addActionListener(e -> switchToPanel("Profile"));

        JMenuItem petsMenuItem = new JMenuItem("PETS", animalsIcon);
        mainMenu.add(petsMenuItem);
        petsMenuItem.addActionListener(e -> {
            switchToPanel("Pets");
            loadPetsData(petsModel);
        });

        JMenuItem storeMenuItem = new JMenuItem("STORE", storeIcon);
        mainMenu.add(storeMenuItem);
        storeMenuItem.addActionListener(e -> {
            switchToPanel("Store");
        });

        JMenuItem signOutMenuItem = new JMenuItem("SIGN OUT", exitIcon);
        mainMenu.add(signOutMenuItem);
        signOutMenuItem.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(null, "Emin misiniz?", "Çıkış", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                switchToPanel("Login");
            }
        });
        
        return menuBar;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        JMenuBar menuBar = createMenu();
        mainPanel.add(menuBar);
        
        JButton btnNewButton = new JButton("New button");
        btnNewButton.setBounds(226, 289, 165, 51);
        mainPanel.add(btnNewButton);
        return mainPanel;
    }
    
    private JPanel createPetsPanel() {
        JPanel petsPanel = new JPanel();
        petsPanel.setLayout(null);
        
        JTable petsTable = new JTable();
        petsTable.setModel(petsModel);
        JScrollPane scrollPane = new JScrollPane(petsTable);
        scrollPane.setBounds(0, 65, 781, 427);
        petsPanel.add(scrollPane);
        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        petsTable.setFont(tableFont);
        petsTable.setRowHeight(25);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < petsTable.getColumnModel().getColumnCount(); i++) {
            petsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JLabel lblYourPets = new JLabel("Your Pets", SwingConstants.CENTER);
        lblYourPets.setFont(new Font("Arial", Font.BOLD, 20));
        lblYourPets.setBounds(305, 4, 170, 54);
        petsPanel.add(lblYourPets);
        
        RoundedButton backButton = new RoundedButton("BACK", 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel("MainMenu");
            }
        });
        backButton.setText("<");
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setBounds(5, 5, 55, 55);
        petsPanel.add(backButton);

        RoundedButton addPets = new RoundedButton("ADD", 30);
        addPets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ArrayList<String> speciesOptions = getAnimalNamesFromDatabase();
                if (speciesOptions.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "! No animal types found in database !", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String species = (String) JOptionPane.showInputDialog(
                        null,
                        "Select species:",
                        "Species Selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        speciesOptions.toArray(),
                        speciesOptions.get(0)
                );
                if (species == null) return;
                ArrayList<String> breedOptions = getBreedsForSpecies(species);
                if (breedOptions.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "! No breeds found for the selected species !", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String breed = (String) JOptionPane.showInputDialog(
                        null,
                        "Select breed:",
                        "Breed Selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        breedOptions.toArray(),
                        breedOptions.get(0)
                );
                if (breed == null) return;
                String petName = JOptionPane.showInputDialog("Enter pet name:");
                if (petName == null || petName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "! Pet name cannot be empty !", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String birthdate = JOptionPane.showInputDialog("Enter birthdate (yyyy-MM-dd):");
                if (birthdate == null) return;

                if (!isValidDate(birthdate)) {
                    JOptionPane.showMessageDialog(null, "! Invalid date format , Please use yyyy-MM-dd !", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean animalExists = checkForAnimal(species, breed, birthdate, petName);
                if (!animalExists) {
                    addAnimalToDatabase(species, breed, birthdate, petName);
                    JOptionPane.showMessageDialog(null, "Animal added successfully.", "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
                    loadPetsData(petsModel);
                } else {
                    JOptionPane.showMessageDialog(null, "! Animal with these details already exists !", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            private boolean isValidDate(String date) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    sdf.parse(date);
                    return true;
                } catch (ParseException ex) {
                    return false;
                }
            }
        });
        addPets.setText("ADD");
        addPets.setForeground(Color.WHITE);
        addPets.setFont(new Font("Arial", Font.BOLD, 30));
        addPets.setBackground(new Color(255, 128, 192));
        addPets.setBounds(10, 502, 180, 47);
        petsPanel.add(addPets);

        petsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = petsTable.getSelectedRow();

                    if (selectedRow != -1) {
                        petId = (int) petsModel.getValueAt(selectedRow, 0);
                        switchToPanel("Appointments");
                    }
                }
            }
        });

        return petsPanel;
    }
    
    private ArrayList<String> getAnimalNamesFromDatabase() {
    	ArrayList<String> animalNames = new ArrayList<>();
        String query = "SELECT animal_name FROM animals";
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                animalNames.add(rs.getString("animal_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return animalNames;
    }

    private ArrayList<String> getBreedsForSpecies(String species) {
        ArrayList<String> breedOptions = new ArrayList<>();
        String query = "SELECT breed_name FROM breeds_" + species.toLowerCase().replace(" ", "_");
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                breedOptions.add(rs.getString("breed_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return breedOptions;
    }

    private void addAnimalToDatabase(String species, String breed, String birthdate, String name) {
        String query = "INSERT INTO pets (owner_tc, pname, species, breed, birthdate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
        	java.sql.Date sqlDate = java.sql.Date.valueOf(birthdate);
            stmt.setString(1, currentUserTc);
            stmt.setString(2,name);
            stmt.setString(3, species);
            stmt.setString(4, breed);
            stmt.setDate(5, sqlDate);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private boolean checkForAnimal(String species, String breed, String birthdate, String name) {
        boolean animalExists = false;

        String query = "SELECT * FROM pets WHERE owner_tc = ? AND species = ? AND breed = ? AND birthdate = ? AND pname = ? ";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
        	java.sql.Date sqlDate = java.sql.Date.valueOf(birthdate);
            stmt.setString(1, currentUserTc);
            stmt.setString(2, species);
            stmt.setString(3, breed);
            stmt.setDate(4, sqlDate);
            stmt.setString(5, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                animalExists = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return animalExists;
    }
    
    private JPanel createStorePanel() {
        JPanel storePanel = new JPanel();
        storePanel.setLayout(null);

        JLabel storeLabel = new JLabel("Mağaza Ürünleri", JLabel.CENTER);
        storeLabel.setBounds(266, 5, 255, 55);
        storeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        storePanel.add(storeLabel);

        JTable storeTable = new JTable();
        storeTable.setFont(new Font("Arial", Font.PLAIN, 20));
        storeTable.setModel(storeModel);
        storeTable.setRowHeight(100);
        JScrollPane scrollPane = new JScrollPane(storeTable);
        scrollPane.setBounds(0, 65, 781, 427);
        storePanel.add(scrollPane);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < storeTable.getColumnCount(); i++) {
        	if(i != 1) {
                storeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);	
        	}
        }

        RoundedButton backButton = new RoundedButton("BACK", 30);
        backButton.addActionListener(e -> {
            switchToPanel("MainMenu");
            storeTable.getSelectionModel().clearSelection();
        });
        backButton.setText("<");
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setBounds(5, 5, 55, 55);
        storePanel.add(backButton);

        RoundedButton addBasketButton = new RoundedButton("ADD BASKET", 30);
        addBasketButton.setFont(new Font("Arial", Font.BOLD, 20));
        addBasketButton.setBackground(new Color(34, 139, 34));
        addBasketButton.setForeground(Color.WHITE);
        addBasketButton.setBounds(10, 502, 250, 47);
        addBasketButton.addActionListener(e -> {
            int selectedRow = storeTable.getSelectedRow();
            if (selectedRow != -1) {
                int stock = Integer.parseInt(storeTable.getValueAt(selectedRow, 4).toString());
                if (stock > 0) {
                    int productId = Integer.parseInt(storeTable.getValueAt(selectedRow, 0).toString());
                    String productName = storeTable.getValueAt(selectedRow, 2).toString();
                    double unitPrice = Double.parseDouble(storeTable.getValueAt(selectedRow, 3).toString());

                    Integer[] options = new Integer[stock];
                    for (int i = 0; i < stock; i++) {
                        options[i] = i + 1;
                    }
                    Integer quantity = (Integer) JOptionPane.showInputDialog(
                            storePanel,
                            "Miktar seçiniz:",
                            "Miktar Seçimi",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (quantity != null) {
                        Optional<BasketItem> existingItemOpt = basket.stream()
                                .filter(item -> item.productName.equals(productName))
                                .findFirst();

                        if (existingItemOpt.isPresent()) {
                            BasketItem existingItem = existingItemOpt.get();
                            existingItem.quantity = quantity;
                        } else {
                            basket.add(new BasketItem(productId, productName, quantity, unitPrice));
                        }

                        storeTable.getSelectionModel().clearSelection();
                        JOptionPane.showMessageDialog(
                                storePanel,
                                "Ürün sepete eklendi: " + productName + " x" + quantity,
                                "Sepete Ekleme",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    storeTable.getSelectionModel().clearSelection();
                    JOptionPane.showMessageDialog(
                            storePanel,
                            "Bu ürün stokta yok!",
                            "Stok Hatası",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(
                        storePanel,
                        "Lütfen bir ürün seçiniz!",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        storePanel.add(addBasketButton);

        ImageIcon icon = new ImageIcon(getClass().getResource("/default/basket.png"));
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        RoundedButton basketButton = new RoundedButton("", 30);
        basketButton.setIcon(icon);
        basketButton.setBackground(new Color(70, 130, 180));
        basketButton.setBounds(666, 5, 93, 55);
        basketButton.addActionListener(e -> {
            if (basket.isEmpty()) {
                JOptionPane.showMessageDialog(
                        storePanel,
                        "Sepetiniz boş!",
                        "Sepet",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder basketContent = new StringBuilder("Sepetinizdeki Ürünler:\n");
                double totalPrice = 0;
                for (BasketItem item : basket) {
                    basketContent.append(item.productName)
                            .append(" x")
                            .append(item.quantity)
                            .append(" = ")
                            .append(item.quantity * item.unitPrice)
                            .append(" TL\n");
                    totalPrice += item.quantity * item.unitPrice;
                }
                basketContent.append("Toplam Tutar: ").append(totalPrice).append(" TL\n");

                int confirmation = JOptionPane.showConfirmDialog(
                        storePanel,
                        basketContent.toString() + "\nSatın almak istiyor musunuz?",
                        "Sepet Onayı",
                        JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                	storeTable.getSelectionModel().clearSelection();
                    boolean itemsRemoved = removeOutOfStockItemsFromBasket();

                    if (itemsRemoved) {
                        JOptionPane.showMessageDialog(
                            storePanel,
                            "Sepetinizden stokta olmayan ürünler çıkarıldı.",
                            "Uyarı",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }else {
                        updateStockInDatabase();
                        basket.clear();
                        JOptionPane.showMessageDialog(
                            storePanel,
                            "Satın alım başarıyla tamamlandı!\nSiparişiniz size en kısa sürede gönderilecektir.",
                            "Başarılı",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                    loadStoreData(storeModel);
                }
            }
        });
        storePanel.add(basketButton);

        loadStoreData(storeModel);
        return storePanel;
    }
    
    public class BasketItem {
        int productId;
        String productName;
        int quantity;
        double unitPrice;

        public BasketItem(int productId, String productName, int quantity, double unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
    }
    
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        RoundedButton backButton = new RoundedButton("BACK", 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel("Pets");
            }
        });
        backButton.setText("<");
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setBounds(5, 5, 55, 55);
        panel.add(backButton);
        int x = 5, y = 70, width = 150, height = 70, gap = 6;
        int buttonsPerRow = 5;
        int totalRows = 6;
        int maxButtons = buttonsPerRow * totalRows;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        String[] hours = {"09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00"};
        for (int i = 0; i < maxButtons; i++) {
            RoundedButton button = new RoundedButton("", 30);
            button.setFont(new Font("Arial", Font.BOLD, 15));
            button.setBounds(x, y, width, height);
            java.util.Date currentDate = calendar.getTime();
            String buttonText = new SimpleDateFormat("dd - MM - yyyy").format(currentDate);
            button.setText(buttonText);
            final String selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
            HashMap<String, Boolean> availabilityMap = checkAllTimeSlots(selectedDate, hours);

            boolean hasAvailableTimes = availabilityMap.values().stream().anyMatch(available -> available);
            if (!hasAvailableTimes) {
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
                button.setEnabled(false);
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            } else {
                button.setBackground(new Color(34, 139, 34));
                button.setForeground(Color.WHITE);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<String> availableHours = new ArrayList<>();
                        for (String hour : hours) {
                            if (availabilityMap.get(hour)) {
                                availableHours.add(hour);
                            }
                        }
                        String selectedHour = (String) JOptionPane.showInputDialog(
                            null,
                            "Select an hour:",
                            "Hour Selection",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            availableHours.toArray(),
                            availableHours.get(0)
                        );
                        if (selectedHour != null) {
                            String[] types = {"Vaccine", "Treatment"};
                            String selectedType = (String) JOptionPane.showInputDialog(
                                null,
                                "Select the type of appointment:",
                                "Appointment Type",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                types,
                                types[0]
                            );

                            if (selectedType != null) {
                                int confirm = JOptionPane.showConfirmDialog(
                                    null,
                                    "Do you confirm the appointment?\nDate: " + selectedDate + "\nTime: " + selectedHour + "\nType: " + selectedType,
                                    "Confirm Appointment",
                                    JOptionPane.YES_NO_OPTION
                                );

                                if (confirm == JOptionPane.YES_OPTION) {
                                	boolean isDateTimeAvailable = checkAppointmentAvailability(selectedDate, selectedHour);
                                	if(isDateTimeAvailable) {
                                		insertAppointment(petId, selectedDate, selectedHour, selectedType);
                                        availabilityMap.put(selectedHour, false);
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Appointment created successfully!\nDate: " + selectedDate + "\nTime: " + selectedHour + "\nType: " + selectedType,
                                                "Appointment Confirmation",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                	}else {
                                		availabilityMap.put(selectedHour, false);
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "The selected date and time is not available. Please choose another time.",
                                                "Availability Error",
                                                JOptionPane.WARNING_MESSAGE
                                        );
                                    }
                                    boolean noAvailableHours = availabilityMap.values().stream().noneMatch(available -> available);

                                    if (noAvailableHours) {
                                        button.setBackground(Color.red);
                                        button.setForeground(Color.RED);
                                        button.setEnabled(false);
                                    }
                                }
                            }
                        }
                    }
                });
            }
            panel.add(button);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            x += width + gap;
            if ((i + 1) % buttonsPerRow == 0) {
                x = 5;
                y += height + gap;
            }
        }
        return panel;
    }
    
    private boolean checkAppointmentAvailability(String selectedDate, String selectedTime) {
        String dateTime = selectedDate + " " + selectedTime + ":00";
        Timestamp appointmentTimestamp = Timestamp.valueOf(dateTime);
        String query = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ?";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, appointmentTimestamp);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
    
    private void insertAppointment(int petId, String selectedDate, String selectedTime, String type) {
        String dateTime = selectedDate + " " + selectedTime + ":00";
        Timestamp appointmentTimestamp = Timestamp.valueOf(dateTime);
        String query = "INSERT INTO appointments (pet_id, appointment_date, type) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setInt(1, petId); // Pet ID
            stmt.setTimestamp(2, appointmentTimestamp);
            stmt.setString(3, type);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Appointment successfully added!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while adding appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isTimeSlotAvailable(String date, String hour) {
        String timestamp = date + " " + hour + ":00"; // Timestamp format: yyyy-MM-dd HH:mm:ss
        String query = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(timestamp));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Eğer sonuç 0 ise bu zaman dilimi uygun
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private HashMap<String, Boolean> checkAllTimeSlots(String date, String[] hours) {
    	HashMap<String, Boolean> availabilityMap = new HashMap<>();
        for (String hour : hours) {
            availabilityMap.put(hour, isTimeSlotAvailable(date, hour));
        }
        return availabilityMap;
    }

    private void switchToPanel(String panelName) {
        CardLayout cl = (CardLayout) (contentPane.getLayout());
        cl.show(contentPane, panelName);
    }
    
    private void loadPetsData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT pet_id, pname, species, breed, birthdate FROM pets WHERE owner_tc = ?";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, currentUserTc);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	int id = rs.getInt("pet_id");
                String name = rs.getString("pname");
                String species = rs.getString("species");
                String type = rs.getString("breed");
                Date birthdate = rs.getDate("birthdate");

                model.addRow(new Object[]{id ,name, species, type, birthdate});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "! Database Error : " + ex.getMessage() + " !", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStoreData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT product_id, product_image, product_name, price, amount FROM store";
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("amount");
                byte[] imageBytes = rs.getBytes("product_image");
                ImageIcon imageIcon;
                if (imageBytes == null) {
                    imageIcon = new ImageIcon(getClass().getResource("/default/store.png"));
                } else {
                    imageIcon = new ImageIcon(imageBytes);
                }
                Image img = imageIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(img);

                model.addRow(new Object[]{id, imageIcon, name, price, quantity});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStockInDatabase() {
        for (BasketItem item : basket) {
            String query = "UPDATE store SET amount = amount - ? WHERE product_id = ?";
            try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
                stmt.setInt(1, item.quantity);
                stmt.setInt(2, item.productId);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private boolean removeOutOfStockItemsFromBasket() {
        boolean itemsRemoved = false;
        for (int i = basket.size() - 1; i >= 0; i--) {
            BasketItem item = basket.get(i);
            String query = "SELECT amount FROM store WHERE product_name = ?";
            try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
                stmt.setString(1, item.productName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int currentStock = rs.getInt("amount");
                        if (currentStock < item.quantity) {
                            basket.remove(i);
                            itemsRemoved = true;
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return itemsRemoved;
    }
    
}
