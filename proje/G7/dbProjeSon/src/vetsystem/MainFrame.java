package vetsystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    DefaultTableModel petsModel = new DefaultTableModel(new String[]{"ID","PET NAME", "SPECIES", "BREED", "BIRTHDATE"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel adminPetsModel = new DefaultTableModel(new String[]{"ID","OWNER TC", "PET NAME","SPECIES", "BREED", "BIRTHDATE"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel adminUserModel = new DefaultTableModel(new String[]{"TC", "NAME", "SURNAME", "PHONENR", "ADDRESS", "NR OF PETS"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel userVaccineModel = new DefaultTableModel(new String[]{"PET NAME", "SPECIES", "BREED", "LAST VACCINE DATE"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel appointmentModel = new DefaultTableModel(new String[]{"NAME","SPECIES", "BREED", "DATE", "TYPE"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel adminAppointmentModel = new DefaultTableModel(new String[]{"OWNER TC","ID","SPECIES", "BREED", "DATE", "TYPE"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel storeModel = new DefaultTableModel(new String[]{"ID", "IMAGE", "NAME", "PRICE $","AMOUNT"}, 0) {
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
    DefaultTableModel orderModel = new DefaultTableModel(new String[]{"ID","TOTAL $", "DATE"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel adminOrdersModel = new DefaultTableModel(new String[]{"TC","ID","TOTAL $", "DATE"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel adminSpeciesModel = new DefaultTableModel(new String[]{"ANIMAL SPECIES"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel adminBreedModel = new DefaultTableModel(new String[]{"ANIMAL BREED"}, 0) {
		private static final long serialVersionUID = 1L;
		@Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    private HashMap<String, HashMap<String, Boolean>> availabilityMapPerDay = new HashMap<>();
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
        JPanel adminPetsPanel = createAdminPetsPanel();
        JPanel adminUserPanel = createAdminUserPanel();
        JPanel adminAnimalPanel = createAdminAnimalPanel();
        JPanel adminAppointmentsPanel = createAppointmentsPanel();
        JPanel adminOrdersPanel = createAdminOrdersPanel();

        contentPane.add(logsgnPanel, "Login");
        contentPane.add(signupPanel, "Signup");
        contentPane.add(mainPanel, "MainMenu");
        contentPane.add(petsPanel, "Pets");
        contentPane.add(storePanel, "Store");
        //contentPane.add(profilePanel, "Profile");
        contentPane.add(appointmentPanel, "Appointments");
        contentPane.add(adminPanel,"AdminMainMenu");
        contentPane.add(adminStorePanel,"AdminStore");
        contentPane.add(adminPetsPanel,"AdminPets");
        contentPane.add(adminUserPanel,"AdminUser");
        contentPane.add(adminAnimalPanel,"AdminAnimal");
        contentPane.add(adminAppointmentsPanel,"AdminAppointments");
        contentPane.add(adminOrdersPanel,"AdminOrders");
        

        CardLayout cl = (CardLayout) (contentPane.getLayout());
        cl.show(contentPane, "Login");
    }
        
    private void createUserViews() {
        String petsViewQuery = "SELECT create_user_pets_view(' " + currentUserTc + "')";
        String appointmentsViewQuery = "SELECT create_user_appointments_view('" + currentUserTc + "')";
        String vaccineViewQuery = "SELECT create_user_vaccines_view('" + currentUserTc + "')";
        String orderQuery = "SELECT create_user_orders_view('" + currentUserTc + "')";

        try (Statement stmt = Database.getConnection().createStatement()) {
            stmt.execute(petsViewQuery);
            stmt.execute(appointmentsViewQuery);
            stmt.execute(vaccineViewQuery);
            stmt.execute(orderQuery);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
      
    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0));
        panel.setLayout(null);
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        RoundedButton backButton = new RoundedButton("<", 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel("AdminMainMenu");
            }
        });
        backButton.setBounds(5, 5, 55, 55);
        backButton.setForeground(Color.GREEN);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(SystemColor.desktop);
        panel.add(backButton);

        JTable appointmentsTable = new JTable();
        appointmentsTable.setModel(adminAppointmentModel);
        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        appointmentsTable.setFont(tableFont);
        appointmentsTable.setRowHeight(25);
        appointmentsTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane1 = new JScrollPane(appointmentsTable);
        scrollPane1.setBounds(0, 70, 780, 390);
        panel.add(scrollPane1);

        JLabel title = new JLabel("APPOINTMENTS TABLE", SwingConstants.CENTER);
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(203, 5, 374, 55);
        panel.add(title);

        for (int i = 0; i < appointmentsTable.getColumnModel().getColumnCount(); i++) {
            appointmentsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Radio Button'lar
        JRadioButton pastAppointmentsButton = new JRadioButton("Past App");
        pastAppointmentsButton.setBounds(400, 500, 126, 30);
        pastAppointmentsButton.setForeground(Color.GREEN);
        pastAppointmentsButton.setBackground(Color.BLACK);
        pastAppointmentsButton.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(pastAppointmentsButton);

        JRadioButton upcomingAppointmentsButton = new JRadioButton("Upcoming App");
        upcomingAppointmentsButton.setBounds(200, 500, 170, 30);
        upcomingAppointmentsButton.setForeground(Color.GREEN);
        upcomingAppointmentsButton.setBackground(Color.BLACK);
        upcomingAppointmentsButton.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(upcomingAppointmentsButton);

        JRadioButton allAppointmentsButton = new JRadioButton("All App");
        allAppointmentsButton.setBounds(50, 500, 126, 30);
        allAppointmentsButton.setForeground(Color.GREEN);
        allAppointmentsButton.setBackground(Color.BLACK);
        allAppointmentsButton.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(allAppointmentsButton);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(pastAppointmentsButton);
        buttonGroup.add(upcomingAppointmentsButton);
        buttonGroup.add(allAppointmentsButton);

        pastAppointmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAppointmentsTable("past",adminAppointmentModel);
            }
        });

        upcomingAppointmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAppointmentsTable("upcoming",adminAppointmentModel);
            }
        });

        allAppointmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAppointmentsTable("all",adminAppointmentModel);
            }
        });
        
        appointmentsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = appointmentsTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Lütfen bir randevu seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String appointmentDateStr = (String) appointmentsTable.getValueAt(selectedRow, 4);
                    LocalDateTime appointmentDate = LocalDateTime.parse(appointmentDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    if (appointmentDate.isBefore(LocalDateTime.now())) {
                        JOptionPane.showMessageDialog(null, "Geçmiş randevular üzerinde işlem yapılamaz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Integer pet_id = (Integer) appointmentsTable.getValueAt(selectedRow, 1);
                    String type = (String) appointmentsTable.getValueAt(selectedRow, 5);

                    Object[] options = {"Randevuyu İptal Et", "Randevuyu Erken Gerçekleştir", "İptal"};
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Seçiminizi yapın:",
                            "Randevu İşlemleri",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[2]
                    );

                    if (choice == 0) {
                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Randevuyu iptal etmek istediğinize emin misiniz?",
                                "Onay",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            cancelAppointment(appointmentDateStr);
                            ((DefaultTableModel) appointmentsTable.getModel()).removeRow(selectedRow);
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Appointment cancelled successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            buttonGroup.clearSelection();
                        }
                    } else if (choice == 1) {
                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Randevuyu erkenden gerçekleştirmek istediğinize emin misiniz?",
                                "Onay",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {   		
                            completeAppointmentEarly(pet_id, type);
                            String selectedHour = "";
                            if (appointmentDateStr.length() == 10) {
                                appointmentDateStr += " 00:00:00";
                            }
                            Timestamp timestamp = Timestamp.valueOf(appointmentDateStr);
                            LocalDateTime localDateTime = timestamp.toLocalDateTime();
                            selectedHour = localDateTime.toLocalTime().toString();
                            selectedHour = selectedHour.substring(0, 5);
                            String dateKey = appointmentDateStr.substring(0, 10);
                            if (availabilityMapPerDay.containsKey(dateKey)) {
                                HashMap<String, Boolean> dayMap = availabilityMapPerDay.get(dateKey);
                                if (dayMap.containsKey(selectedHour)) {
                                    dayMap.put(selectedHour, true);
                                }
                            }
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Appointment Completed Successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            buttonGroup.clearSelection();
                        }
                    }
                }
            }
        });


        return panel;
    }
    
    private void completeAppointmentEarly(int pet_id, String type) {
        String updateVaccineQuery = "UPDATE vaccine SET vaccine_date = NOW() WHERE pet_id = ?";
        String updateAppointmentQuery;

        if (type.equalsIgnoreCase("vaccine")) {
            updateAppointmentQuery = "UPDATE appointments SET appointment_date = NOW() - INTERVAL '1 hour' WHERE pet_id = ? AND appointment_type = ?";
        } else if (type.equalsIgnoreCase("treatment")) {
            updateAppointmentQuery = "UPDATE appointments SET appointment_date = NOW() - INTERVAL '1 hour' WHERE pet_id = ? AND appointment_type = ?";
        } else {
            JOptionPane.showMessageDialog(null, "Geçersiz randevu tipi. Güncelleme yapılmadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement updateAppointmentStmt = Database.getConnection().prepareStatement(updateAppointmentQuery)) {
            
            if (type.equalsIgnoreCase("vaccine")) {
                try (PreparedStatement updateVaccineStmt = Database.getConnection().prepareStatement(updateVaccineQuery)) {
                    updateVaccineStmt.setInt(1, pet_id);
                    int vaccineRowsAffected = updateVaccineStmt.executeUpdate();

                    if (vaccineRowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Aşı kartı başarıyla güncellendi.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Aşı kartı güncellenemedi. Lütfen bilgileri kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            updateAppointmentStmt.setInt(1, pet_id);
            updateAppointmentStmt.setString(2, type);
            int appointmentRowsAffected = updateAppointmentStmt.executeUpdate();

            if (appointmentRowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Randevu tarihi başarıyla güncellendi.");
            } else {
                JOptionPane.showMessageDialog(null, "Randevu tarihi güncellenemedi. Lütfen bilgileri kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Güncelleme sırasında bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
   
    private void updateAppointmentsTable(String type, DefaultTableModel model) {
        model.setRowCount(0);

        String query;
        if (type.equals("past")) {
            query = "SELECT p.owner_tc, a.pet_id, p.species, p.breed, TO_CHAR(a.appointment_date, 'YYYY-MM-DD HH24:MI:SS') AS appointment_date, a.appointment_type "
                  + "FROM appointments a "
                  + "JOIN pets p ON a.pet_id = p.pet_id WHERE a.appointment_date < NOW() ORDER BY a.appointment_date DESC";
        } else if (type.equals("upcoming")) {
            query = "SELECT p.owner_tc, a.pet_id, p.species, p.breed, TO_CHAR(a.appointment_date, 'YYYY-MM-DD HH24:MI:SS') AS appointment_date, a.appointment_type "
                  + "FROM appointments a "
                  + "JOIN pets p ON a.pet_id = p.pet_id WHERE a.appointment_date >= NOW() ORDER BY a.appointment_date ASC";
        } else {
            query = "SELECT p.owner_tc, a.pet_id, p.species, p.breed, TO_CHAR(a.appointment_date, 'YYYY-MM-DD HH24:MI:SS') AS appointment_date, a.appointment_type "
                  + "FROM appointments a "
                  + "JOIN pets p ON a.pet_id = p.pet_id ORDER BY a.appointment_date";
        }

        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String owner = rs.getString("owner_tc");
                Integer pet_id = rs.getInt("pet_id");
                String species = rs.getString("species");
                String breed = rs.getString("breed");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentType = rs.getString("appointment_type");

                model.addRow(new Object[]{owner, pet_id, species, breed, appointmentDate, appointmentType});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Randevular getirilirken bir hata oluştu: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
	private JPanel createAdminAnimalPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0));
        panel.setLayout(null);
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        
        RoundedButton backButton = new RoundedButton("<", 30);
        backButton.setBounds(5, 5, 55, 55);
        backButton.setForeground(Color.GREEN);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(SystemColor.desktop);
        panel.add(backButton);
        
        JTable speciesTable = new JTable();
        speciesTable.setModel(adminSpeciesModel);
        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        speciesTable.setFont(tableFont);
        speciesTable.setRowHeight(25);
        speciesTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane1 = new JScrollPane(speciesTable);
        scrollPane1.setBounds(60, 90, 290, 385);
        panel.add(scrollPane1);
        for (int i = 0; i < speciesTable.getColumnModel().getColumnCount(); i++) {
            speciesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JTable breedTable = new JTable();
        breedTable.setModel(adminBreedModel);
        breedTable.setFont(tableFont);
        breedTable.setRowHeight(25);
        breedTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane2 = new JScrollPane(breedTable);
        scrollPane2.setBounds(427, 90, 290, 385);
        panel.add(scrollPane2);
        
        RoundedButton addButton = new RoundedButton("ADD", 30);
        addButton.setForeground(Color.GREEN);
        addButton.setFont(new Font("Arial", Font.BOLD, 25));
        addButton.setBackground(SystemColor.desktop);
        addButton.setBounds(228, 494, 144, 55);
        panel.add(addButton);
        
        RoundedButton deleteButton = new RoundedButton("DELETE", 30);
        deleteButton.setForeground(Color.GREEN);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 25));
        deleteButton.setBackground(SystemColor.desktop);
        deleteButton.setBounds(391, 494, 144, 55);
        panel.add(deleteButton);
        
        JLabel lblSpeces = new JLabel("SPECIES - BREED", SwingConstants.CENTER);
        lblSpeces.setForeground(Color.GREEN);
        lblSpeces.setFont(new Font("Arial", Font.BOLD, 30));
        lblSpeces.setBounds(245, 25, 290, 55);
        panel.add(lblSpeces);
        for (int i = 0; i < breedTable.getColumnModel().getColumnCount(); i++) {
            breedTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        speciesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = speciesTable.getSelectedRow();
                if (selectedRow != -1) {
                    String speciesName = (String) adminSpeciesModel.getValueAt(selectedRow, 0);
                    loadBreedsData(speciesName,adminBreedModel);
                }
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Add Species", "Add Breed"};
                int choice = JOptionPane.showOptionDialog(
                    null,
                    "What would you like to add?",
                    "Add Option",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                );
                if (choice == -1) {
                    return;
                }
                if (choice == 0) {
                    String newSpecies = JOptionPane.showInputDialog(null, "Enter new species name:");
                    if (newSpecies == null || newSpecies.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Species name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    newSpecies = formatInput(newSpecies.trim());
                    addSpeciesToDatabase(newSpecies);

                } else if (choice == 1) { // Add Breed
                    int selectedRow = speciesTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Please select a species from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String speciesName = (String) adminSpeciesModel.getValueAt(selectedRow, 0);
                    String newBreed = JOptionPane.showInputDialog(null, "Enter new breed name for species: " + speciesName);
                    if (newBreed == null || newBreed.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Breed name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    newBreed = formatInput(newBreed.trim());
                    addBreedToDatabase(speciesName, newBreed);
                }
            }
        });
		
        backButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("AdminMainMenu");
        		adminBreedModel.setRowCount(0);
        	}
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int speciesSelectedRow = speciesTable.getSelectedRow();
                int breedsSelectedRow = breedTable.getSelectedRow();
                
                if (breedsSelectedRow == -1 && speciesSelectedRow != -1) {
                    String speciesName = (String) speciesTable.getValueAt(speciesSelectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "Tür '" + speciesName + "' silmek istediğinize emin misiniz?", 
                        "Silme Onayı", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteSpecies(speciesName);
                        JOptionPane.showMessageDialog(null, "Tür başarıyla silindi: " + speciesName);
                        loadSpeciesData(adminSpeciesModel);
                        speciesTable.clearSelection();
                        adminBreedModel.setRowCount(0);
                    }
                } else if (breedsSelectedRow != -1 && speciesSelectedRow != -1) {
                    String speciesName = (String) speciesTable.getValueAt(speciesSelectedRow, 0);
                    String breedName = (String) breedTable.getValueAt(breedsSelectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "Tür'e ait seçilen breed '" + breedName + "' silmek istediğinize emin misiniz?", 
                        "Silme Onayı", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteBreed(speciesName, breedName);
                        JOptionPane.showMessageDialog(null, "Tür'e ait seçilen breed başarıyla silindi: " + breedName);
                        loadSpeciesData(adminSpeciesModel);
                        loadBreedsData(speciesName, adminBreedModel);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen seçim yapınız.", "WARNING", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return panel;
	}
        
	private JPanel createAdminUserPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0));
        panel.setLayout(null);
        
        RoundedButton backButton = new RoundedButton("<", 30);
        backButton.setBounds(5, 5, 55, 55);
        backButton.setForeground(Color.GREEN);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(SystemColor.desktop);
        panel.add(backButton);
        
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 19));
        searchField.setBounds(169, 477, 219, 47);
        panel.add(searchField);
        
        JTable userTable = new JTable();
        userTable.setModel(adminUserModel);
        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        userTable.setFont(tableFont);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(0, 70, 780, 390);
        panel.add(scrollPane);
        
        RoundedButton searchButton = new RoundedButton("SEARCH", 30);
        searchButton.setForeground(Color.GREEN);
        searchButton.setFont(new Font("Arial", Font.BOLD, 25));
        searchButton.setBackground(SystemColor.desktop);
        searchButton.setBounds(418, 473, 162, 55);
        panel.add(searchButton);
        
        JLabel title = new JLabel("USER", SwingConstants.CENTER);
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(253, 10, 255, 55);
        panel.add(title);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < userTable.getColumnModel().getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel("AdminMainMenu");
                searchField.setText("");
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                searchUser(searchText, adminUserModel);
                searchField.setText("");
            }
        });
        
        
        return panel;
    }
    
	private JPanel createAdminPetsPanel() {
        JPanel adminPetsPanel = new JPanel();
        adminPetsPanel.setBackground(new Color(0, 0, 0));
        adminPetsPanel.setLayout(null);

        RoundedButton backButton = new RoundedButton("<", 30);
        backButton.setBounds(5, 5, 55, 55);
        backButton.setForeground(Color.GREEN);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(SystemColor.desktop);
        adminPetsPanel.add(backButton);

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 19));
        searchField.setBounds(169, 477, 219, 47);
        adminPetsPanel.add(searchField);

        JTable petsTable = new JTable();
        petsTable.setModel(adminPetsModel);
        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        petsTable.setFont(tableFont);
        petsTable.setRowHeight(25);
        petsTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(petsTable);
        scrollPane.setBounds(0, 70, 781, 390);
        adminPetsPanel.add(scrollPane);
        
        RoundedButton searchButton = new RoundedButton("<", 30);
        searchButton.setText("SEARCH");
        searchButton.setForeground(Color.GREEN);
        searchButton.setFont(new Font("Arial", Font.BOLD, 25));
        searchButton.setBackground(SystemColor.desktop);
        searchButton.setBounds(418, 473, 162, 55);
        adminPetsPanel.add(searchButton);
        
        JLabel title = new JLabel("PETS", SwingConstants.CENTER);
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(253, 10, 255, 55);
        adminPetsPanel.add(title);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < petsTable.getColumnModel().getColumnCount(); i++) {
            petsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        petsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = petsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int petId = (int) adminPetsModel.getValueAt(selectedRow, 0);
                        String petName = (String) adminPetsModel.getValueAt(selectedRow, 2);
                        String ownerTc = (String) adminPetsModel.getValueAt(selectedRow, 1);
                        String species = (String) adminPetsModel.getValueAt(selectedRow, 3);
                        String breed = (String) adminPetsModel.getValueAt(selectedRow, 4);
                        Date birthDate = (Date) adminPetsModel.getValueAt(selectedRow, 5);
                        String userInfo = getUserInfo(ownerTc);
                        String vaccinationInfo = getVaccinationDetails(petId);
                        
                        String[] options = {"View Details", "Delete Pet"};
                        int choice = JOptionPane.showOptionDialog(
                                null,
                                "What would you like to do?",
                                "Pet Actions",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                        );
                        if (choice == 0) {
                            String petInfo = "\t-- USER --\n" + userInfo + "\n\n\t-- PET --\n"
                                    + "Pet ID: " + petId + "\n"
                                    + "Pet Name: " + petName + "\n"
                                    + "Species: " + species + "\n"
                                    + "Breed: " + breed + "\n"
                                    + "Birth Date: " + birthDate + "\n\n"
                                    + vaccinationInfo;
                            JOptionPane.showMessageDialog(
                                    null,
                                    petInfo,
                                    "Pet Details",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }else if (choice == 1) {
                        	int confirm = JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to delete this pet?\nPet Name: " + petName,
                                    "Confirm Deletion",
                                    JOptionPane.YES_NO_OPTION
                            );

                            if (confirm == JOptionPane.YES_OPTION) {
                                deletePet(petId);
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Pet deleted successfully!",
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                                loadAllPets(adminPetsModel);
                            }
                        }
                    }
                }
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                searchPets(searchText, adminPetsModel);
                searchField.setText("");
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel("AdminMainMenu");
                searchField.setText("");
            }
        });
        
        return adminPetsPanel;
    }
    
	private JPanel createAdminOrdersPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0));
        panel.setLayout(null);

        RoundedButton backButton = new RoundedButton("<", 30);
        backButton.setBounds(5, 5, 55, 55);
        backButton.setForeground(Color.GREEN);
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(SystemColor.desktop);
        panel.add(backButton);

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 19));
        searchField.setBounds(169, 477, 219, 47);
        panel.add(searchField);

        JTable ordersTable = new JTable();
        ordersTable.setModel(adminOrdersModel);
        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        ordersTable.setFont(tableFont);
        ordersTable.setRowHeight(25);
        ordersTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBounds(0, 70, 781, 390);
        panel.add(scrollPane);
        
        RoundedButton searchButton = new RoundedButton("<", 30);
        searchButton.setText("SEARCH");
        searchButton.setForeground(Color.GREEN);
        searchButton.setFont(new Font("Arial", Font.BOLD, 25));
        searchButton.setBackground(SystemColor.desktop);
        searchButton.setBounds(418, 473, 162, 55);
        panel.add(searchButton);
        
        JLabel title = new JLabel("ORDERS", SwingConstants.CENTER);
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(253, 10, 255, 55);
        panel.add(title);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < ordersTable.getColumnModel().getColumnCount(); i++) {
        	ordersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        ordersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = ordersTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Lütfen bir sipariş seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int orderId = (int) ordersTable.getValueAt(selectedRow, 1);
                    showOrderDetails(orderId);
                }
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                searchOrders(searchText, adminOrdersModel);
                searchField.setText("");
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel("AdminMainMenu");
                searchField.setText("");
            }
        });
        
        return panel;
    }
	
	private void loadAllOrders(DefaultTableModel model) {
	    model.setRowCount(0);

	    String query = "SELECT order_id, tc, order_date, total_price FROM orders ORDER BY order_date DESC";

	    try (Statement stmt = Database.getConnection().createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            int orderId = rs.getInt("order_id");
	            String ownerTc = rs.getString("tc");
	            Date orderDate = rs.getDate("order_date");
	            double totalPrice = rs.getDouble("total_price");
	            model.addRow(new Object[]{ownerTc, orderId, orderDate, totalPrice});
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Siparişler yüklenirken bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private JPanel createAdminPanel() {
        JPanel adminMainMenuPanel = new JPanel();
        adminMainMenuPanel.setBackground(new Color(0, 0, 0));
        adminMainMenuPanel.setLayout(null);
        
        RoundedButton animalButton = new RoundedButton("PET TABLE", 30);
        animalButton.setForeground(new Color(0, 255, 0));
        animalButton.setFont(new Font("Arial", Font.BOLD, 20));
        animalButton.setBackground(new Color(0, 0, 0));
        animalButton.setBounds(30, 160, 318, 50);
        adminMainMenuPanel.add(animalButton);
        animalButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("AdminPets");
                loadAllPets(adminPetsModel);
        	}
        });
        
        RoundedButton storeButton = new RoundedButton("STORE TABLE", 30);
        storeButton.setForeground(new Color(0, 255, 0));
        storeButton.setFont(new Font("Arial", Font.BOLD, 20));
        storeButton.setBackground(new Color(0, 0, 0));
        storeButton.setBounds(30, 240, 318, 50);
        adminMainMenuPanel.add(storeButton);
        storeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("AdminStore");
        	}
        });
        
        RoundedButton quitbutton = new RoundedButton("QUIT", 30);
        quitbutton.setForeground(new Color(0, 255, 0));
        quitbutton.setFont(new Font("Arial", Font.BOLD, 20));
        quitbutton.setBackground(new Color(0, 0, 0));
        quitbutton.setBounds(231, 408, 318, 67);
        adminMainMenuPanel.add(quitbutton);
        quitbutton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("Login");
        		lgnPassword.setText("");
        		lgnId.setText("");
        	}
        });
        
        RoundedButton userButoon = new RoundedButton("USER TABLE", 30);
        userButoon.setForeground(Color.GREEN);
        userButoon.setFont(new Font("Arial", Font.BOLD, 20));
        userButoon.setBackground(Color.BLACK);
        userButoon.setBounds(30, 80, 318, 50);
        adminMainMenuPanel.add(userButoon);
        
        JLabel lblAdminPanel = new JLabel("ADMİN PANEL", SwingConstants.CENTER);
        lblAdminPanel.setForeground(Color.GREEN);
        lblAdminPanel.setFont(new Font("Arial", Font.BOLD, 30));
        lblAdminPanel.setBounds(250, 5, 255, 55);
        adminMainMenuPanel.add(lblAdminPanel);
        
        RoundedButton animalButoon = new RoundedButton("ANIMALS TREATED", 30);
        animalButoon.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		loadSpeciesData(adminSpeciesModel);
        		switchToPanel("AdminAnimal");
        	}
        });
        animalButoon.setForeground(Color.GREEN);
        animalButoon.setFont(new Font("Arial", Font.BOLD, 20));
        animalButoon.setBackground(Color.BLACK);
        animalButoon.setBounds(422, 80, 318, 50);
        adminMainMenuPanel.add(animalButoon);
        
        RoundedButton storeButton_1 = new RoundedButton("STORE TABLE", 30);
        storeButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		loadAllOrders(adminOrdersModel);
        		switchToPanel("AdminOrders");
        	}
        });
        storeButton_1.setText("ORDER TABLE");
        storeButton_1.setForeground(Color.GREEN);
        storeButton_1.setFont(new Font("Arial", Font.BOLD, 20));
        storeButton_1.setBackground(Color.BLACK);
        storeButton_1.setBounds(30, 320, 318, 50);
        adminMainMenuPanel.add(storeButton_1);
        
        RoundedButton rndbtnApponetmentTable = new RoundedButton("ANIMALS TREATED", 30);
        rndbtnApponetmentTable.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		updateAppointmentsTable("all",adminAppointmentModel);
        		switchToPanel("AdminAppointments");
        	}
        });
        rndbtnApponetmentTable.setText("APPOINETMENT TABLE");
        rndbtnApponetmentTable.setForeground(Color.GREEN);
        rndbtnApponetmentTable.setFont(new Font("Arial", Font.BOLD, 20));
        rndbtnApponetmentTable.setBackground(Color.BLACK);
        rndbtnApponetmentTable.setBounds(422, 160, 318, 50);
        adminMainMenuPanel.add(rndbtnApponetmentTable);
        userButoon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	loadAllUser(adminUserModel);
            	switchToPanel("AdminUser");
            }
        });
            	
    	return adminMainMenuPanel;
    }
	    
    private JPanel createAdminStorePanel() {
        JPanel adminStorePanel = new JPanel();
        adminStorePanel.setBackground(SystemColor.desktop);
        adminStorePanel.setLayout(null);
        
        JLabel title = new JLabel("STORE", JLabel.CENTER);
        title.setForeground(new Color(0, 255, 0));
        title.setBounds(266, 5, 255, 55);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        adminStorePanel.add(title);
        
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
        
        RoundedButton backButton = new RoundedButton("<", 30);
        backButton.setForeground(new Color(0, 255, 0));
        backButton.setFont(new Font("Arial", Font.BOLD, 35));
        backButton.setBackground(SystemColor.desktop);
        backButton.setBounds(5, 5, 55, 55);
        adminStorePanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		switchToPanel("AdminMainMenu");
        	}
        });
        
        RoundedButton addItem = new RoundedButton("ADD ITEM", 30);
        addItem.setForeground(new Color(0, 255, 0));
        addItem.setFont(new Font("Arial", Font.BOLD, 20));
        addItem.setBackground(new Color(0, 0, 0));
        addItem.setBounds(110, 502, 250, 47);
        adminStorePanel.add(addItem);
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
        
        RoundedButton deleteItem = new RoundedButton("SIGN UP", 30);
        deleteItem.setText("DELETE ITEM");
        deleteItem.setForeground(Color.GREEN);
        deleteItem.setFont(new Font("Arial", Font.BOLD, 20));
        deleteItem.setBackground(Color.BLACK);
        deleteItem.setBounds(413, 502, 250, 47);
        adminStorePanel.add(deleteItem);
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
        RoundedButton btn = new RoundedButton("LOGIN", 30);
        btn.setVisible(false);
        btn.setBounds(300, 220, 250, 50);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        loginPanel.add(btn);
        btn.addActionListener(e -> { 
        	searchPets1( " ", adminPetsModel);
			searchOrders1(" ", adminAppointmentModel);
			searchUser1(" ", adminAppointmentModel);

        });
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
                	createUserViews();
                    loadAppointments(appointmentModel);
                    loadOrders(orderModel);
                    loadVaccines(userVaccineModel);
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

        ImageIcon animalsIcon = new ImageIcon(getClass().getResource("/icon/pets.png"));
        ImageIcon storeIcon = new ImageIcon(getClass().getResource("/icon/store.png"));
        ImageIcon exitIcon = new ImageIcon(getClass().getResource("/icon/exit.png"));

        JMenu mainMenu = new JMenu("--- Menu ---");
        menuBar.add(mainMenu);

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
                basket.clear() ;
            }
        });
        
        return menuBar;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        JMenuBar menuBar = createMenu();
        mainPanel.add(menuBar);
        
        JTable orderTable = new JTable();
        orderTable.setModel(orderModel);
        JScrollPane scrollPane2 = new JScrollPane(orderTable);
        scrollPane2.setBounds(543, 82, 205, 411);
        mainPanel.add(scrollPane2);
        orderTable.setRowHeight(25);
        orderTable.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
        centerRenderer1.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < orderTable.getColumnModel().getColumnCount(); i++) {
            orderTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);
        }
        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = orderTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int orderId = (int) orderTable.getValueAt(selectedRow, 0);
                        showOrderDetails(orderId);
                    }
                }
            }
        });

        JTable appTable = new JTable();
        appTable.setModel(appointmentModel);
        JScrollPane scrollPane1 = new JScrollPane(appTable);
        scrollPane1.setBounds(28, 82, 490, 177);
        mainPanel.add(scrollPane1);
        appTable.setRowHeight(25);
        appTable.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
        centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < appTable.getColumnModel().getColumnCount(); i++) {
            appTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
        }
        appTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = appTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String petName = appTable.getValueAt(selectedRow, 0).toString();
                        String species = appTable.getValueAt(selectedRow, 1).toString();
                        String breed = appTable.getValueAt(selectedRow, 2).toString();
                        String appointmentDate = appTable.getValueAt(selectedRow, 3).toString();
                        String appointmentType = appTable.getValueAt(selectedRow, 4).toString();

                        String message = "Pet Name: " + petName + "\n" +
                                         "Species: " + species + "\n" +
                                         "Breed: " + breed + "\n" +
                                         "Appointment Date: " + appointmentDate + "\n" +
                                         "Appointment Type: " + appointmentType + "\n\n" +
                                         "Do you want to cancel this appointment?";
                        
                        int confirm = JOptionPane.showConfirmDialog(
                                mainPanel,
                                message,
                                "Cancel Appointment",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            cancelAppointment(appointmentDate);
                            ((DefaultTableModel) appTable.getModel()).removeRow(selectedRow);
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Appointment cancelled successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                        appTable.clearSelection();
                    }
                }
            }
        });
        
        JLabel title1 = new JLabel("UPCOMING APPOINTMENTS");
        title1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        title1.setBounds(135, 45, 285, 30);
        mainPanel.add(title1);
        
        JLabel title2 = new JLabel("ORDERS");
        title2.setFont(new Font("Times New Roman", Font.BOLD, 25));
        title2.setBounds(590, 45, 140, 30);
        mainPanel.add(title2);
        
        JTable vaccineTable = new JTable();
        vaccineTable.setModel(userVaccineModel);
        vaccineTable.setRowHeight(25);
        vaccineTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane3 = new JScrollPane(vaccineTable);
        scrollPane3.setBounds(28, 316, 490, 177);
        mainPanel.add(scrollPane3);
        
        JLabel title3 = new JLabel("NEED VACCINE");
        title3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        title3.setBounds(180, 276, 175, 30);
        mainPanel.add(title3);
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < vaccineTable.getColumnModel().getColumnCount(); i++) {
        	vaccineTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        


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
        petsTable.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < petsTable.getColumnModel().getColumnCount(); i++) {
            petsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JLabel lblYourPets = new JLabel("Your Pets", SwingConstants.CENTER);
        lblYourPets.setFont(new Font("Arial", Font.BOLD, 30));
        lblYourPets.setBounds(305, 4, 170, 54);
        petsPanel.add(lblYourPets);
        
        RoundedButton backButton = new RoundedButton("BACK", 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	loadVaccines(userVaccineModel);
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

            String selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
            HashMap<String, Boolean> availabilityMap = checkAllTimeSlots(selectedDate, hours);
            availabilityMapPerDay.put(selectedDate, availabilityMap);

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
                                    if (isDateTimeAvailable) {
                                        boolean canAddAppointment = checkAppointmentLimit(petId, selectedType);
                                        if (canAddAppointment) {
                                            insertAppointment(petId, selectedDate, selectedHour, selectedType);
                                            loadAppointments(appointmentModel);
                                            availabilityMap.put(selectedHour, false);
                                            JOptionPane.showMessageDialog(
                                                null,
                                                "Appointment created successfully!\nDate: " + selectedDate + "\nTime: " + selectedHour + "\nType: " + selectedType,
                                                "Appointment Confirmation",
                                                JOptionPane.INFORMATION_MESSAGE
                                            );
                                        } else {
                                            JOptionPane.showMessageDialog(
                                                null,
                                             
                                                "Trigger tetiklendi. This pet already has a " + selectedType + " appointment.",
                                                "Appointment Limit Reached",
                                                JOptionPane.ERROR_MESSAGE
                                            );
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(
                                            null,
                                            "The selected date and time is unavailable.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE
                                        );
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
    
    private JPanel createStorePanel() {
        JPanel storePanel = new JPanel();
        storePanel.setLayout(null);

        JLabel storeLabel = new JLabel("STORE", JLabel.CENTER);
        storeLabel.setBounds(263, 5, 255, 55);
        storeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        storePanel.add(storeLabel);

        JTable storeTable = new JTable();
        storeTable.setFont(new Font("Arial", Font.PLAIN, 20));
        storeTable.setModel(storeModel);
        storeTable.setRowHeight(100);
        JScrollPane scrollPane = new JScrollPane(storeTable);
        scrollPane.setBounds(0, 65, 781, 427);
        storePanel.add(scrollPane);
        storeTable.getTableHeader().setReorderingAllowed(false);
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
                    } else {
                    	updateStockInDatabase();
                        processOrder(storePanel,totalPrice);
                        loadOrders(orderModel);
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
    
    private ArrayList<String> getAnimalNamesFromDatabase() {
    	ArrayList<String> animalNames = new ArrayList<>();
        String query = "SELECT species_name FROM species";
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                animalNames.add(rs.getString("species_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return animalNames;
    }
    
    private ArrayList<String> getBreedsForSpecies(String speciesName) {
        ArrayList<String> breedOptions = new ArrayList<>();
        String query = "SELECT breed_name FROM breeds WHERE species_name = ?";
        
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, speciesName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    breedOptions.add(rs.getString("breed_name"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return breedOptions;
    }

    private void addAnimalToDatabase(String species, String breed, String birthdate, String name) {
        String insertQuery = "INSERT INTO pets (owner_tc, pname, species, breed, birthdate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement insertStmt = Database.getConnection().prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            java.sql.Date sqlDate = java.sql.Date.valueOf(birthdate);
            insertStmt.setString(1, currentUserTc);
            insertStmt.setString(2, name);
            insertStmt.setString(3, species);
            insertStmt.setString(4, breed);
            insertStmt.setDate(5, sqlDate);

            int rowsAffected = insertStmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int petId = generatedKeys.getInt(1);

                        if (isVaccineCardCreated(petId)) {
                            JOptionPane.showMessageDialog(null, "The trigger worked and the vaccination card was created.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Hayvan eklendi ancak aşı kartı oluşturulamadı.");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata: Hayvan eklenirken bir sorun oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isVaccineCardCreated(int petId) throws SQLException {
        String checkVaccineQuery = "SELECT 1 FROM vaccine WHERE pet_id = ?";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(checkVaccineQuery)) {
            stmt.setInt(1, petId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
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
    
    private void showOrderDetails(int orderId) {
        String query = "SELECT oi.product_id, p.product_name, oi.quantity, oi.unit_price " +
                       "FROM order_items oi " +
                       "JOIN store p ON oi.product_id = p.product_id " +
                       "WHERE oi.order_id = ?";

        StringBuilder orderDetails = new StringBuilder("Order Details:\n\n");
        double totalPrice = 0;

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");

                double itemTotal = quantity * unitPrice;
                totalPrice += itemTotal;

                orderDetails.append(productName)
                            .append(" x").append(quantity)
                            .append(" = ").append(itemTotal)
                            .append(" TL\n");
            }

            orderDetails.append("\nTotal Price: ").append(totalPrice).append(" TL");

            // Format order date to exclude milliseconds
            String dateQuery = "SELECT order_date FROM orders WHERE order_id = ?";
            try (PreparedStatement dateStmt = Database.getConnection().prepareStatement(dateQuery)) {
                dateStmt.setInt(1, orderId);
                ResultSet dateRs = dateStmt.executeQuery();
                if (dateRs.next()) {
                    Timestamp orderDate = dateRs.getTimestamp("order_date");

                    // Format the date to exclude milliseconds
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = dateFormat.format(orderDate);
                    orderDetails.append("\nOrder Date: ").append(formattedDate);
                }
            }

            JOptionPane.showMessageDialog(null, orderDetails.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading order details: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
    
    private boolean processOrder(JPanel storePanel, double totalPrice) {
        boolean orderProcessed = false;

        String orderQuery = "INSERT INTO orders (tc, total_price, order_date) VALUES (?, ?, NOW()) RETURNING order_id";
        String itemQuery = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (
            PreparedStatement orderStmt = Database.getConnection().prepareStatement(orderQuery);
            PreparedStatement itemStmt = Database.getConnection().prepareStatement(itemQuery)
        ) {
            orderStmt.setString(1, currentUserTc);
            orderStmt.setDouble(2, totalPrice);
            
            try (ResultSet rs = orderStmt.executeQuery()) {
                int orderId = 0;
                if (rs.next()) {
                    orderId = rs.getInt("order_id");
                }
                for (BasketItem item : basket) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.productId);
                    itemStmt.setInt(3, item.quantity);
                    itemStmt.setDouble(4, item.unitPrice);
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
                orderProcessed = true;
                JOptionPane.showMessageDialog(
                    storePanel,
                    "Sipariş başarıyla işlendi!",
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                storePanel,
                "Satın alma işlemi sırasında hata oluştu: " + ex.getMessage(),
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }

        return orderProcessed;
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
    
    private boolean checkAppointmentLimit(int petId, String appointmentType) {
        String query = "SELECT COUNT(*) AS count " +
                       "FROM appointments " +
                       "WHERE pet_id = ? AND appointment_type = ? AND appointment_date > ?";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setInt(1, petId);
            stmt.setString(2, appointmentType);

            java.sql.Timestamp currentDate = new java.sql.Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(3, currentDate);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count == 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error while checking appointment limit: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return false;
    }

    
    private void insertAppointment(int petId, String selectedDate, String selectedTime, String type) {
        boolean canAddAppointment = checkAppointmentLimit(petId, type);
        if (!canAddAppointment) {
            JOptionPane.showMessageDialog(
                    null,
                    "This pet already has a " + type + " appointment.",
                    "Appointment Limit Reached",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String dateTime = selectedDate + " " + selectedTime + ":00";
        Timestamp appointmentTimestamp = Timestamp.valueOf(dateTime);
        String query = "INSERT INTO appointments (pet_id, appointment_date, appointment_type) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setInt(1, petId);
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
            JOptionPane.showMessageDialog(
                    null,
                    "Error while adding appointment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void cancelAppointment(String appointmentDate) {
        String query = "DELETE FROM appointments WHERE appointment_date = ?";
        String selectedHour = "";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            if (appointmentDate.length() == 10) {
                appointmentDate += " 00:00:00";
            }
            Timestamp timestamp = Timestamp.valueOf(appointmentDate);
            stmt.setTimestamp(1, timestamp);
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            selectedHour = localDateTime.toLocalTime().toString();
            selectedHour = selectedHour.substring(0, 5);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                String dateKey = appointmentDate.substring(0, 10);
                if (availabilityMapPerDay.containsKey(dateKey)) {
                    HashMap<String, Boolean> dayMap = availabilityMapPerDay.get(dateKey);
                    if (dayMap.containsKey(selectedHour)) {
                        dayMap.put(selectedHour, true);
                    } else {
                        System.out.println("Hour not found in availability map: " + selectedHour);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to cancel appointment. Appointment not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error while cancelling appointment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean isTimeSlotAvailable(String date, String hour) {
        String timestamp = date + " " + hour + ":00";
        String query = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(timestamp));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
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
    
    private void loadOrders(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT order_id, total_price, order_date FROM user_orders_view ";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                double totalPrice = rs.getDouble("total_price");
                Timestamp orderDate = rs.getTimestamp("order_date");
                String formattedDate = dateFormat.format(orderDate);
                model.addRow(new Object[]{orderId, totalPrice, formattedDate});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading orders: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadAppointments(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT pname, species, breed, appointment_date, appointment_type " +
                       "FROM user_appointments_view ";


        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                String petname = rs.getString("pname");
                String animalType = rs.getString("species");
                String breed = rs.getString("breed");
                Timestamp appointmentDate = rs.getTimestamp("appointment_date");
                String type = rs.getString("appointment_type");
                String formattedDate = dateFormat.format(appointmentDate);
                model.addRow(new Object[]{
                    petname,
                    animalType,
                    breed,
                    formattedDate,
                    type
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading appointments: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void addSpeciesToDatabase(String speciesName) {
        String query = "INSERT INTO species (species_name) VALUES (?)";
        
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, speciesName);
            stmt.executeUpdate();
            adminSpeciesModel.addRow(new Object[]{speciesName});
            JOptionPane.showMessageDialog(null, "Species added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding species: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addBreedToDatabase(String speciesName, String breedName) {
        String query = "INSERT INTO breeds (breed_name,species_name) VALUES (?,?)";
        
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, breedName);
            stmt.setString(2, speciesName);
            stmt.executeUpdate();
            adminBreedModel.addRow(new Object[]{breedName});
            JOptionPane.showMessageDialog(null, "Breed added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding breed: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadBreedsData(String speciesName, DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT breed_name FROM breeds WHERE species_name = ?";
        
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, speciesName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String breedName = rs.getString("breed_name");
                    model.addRow(new Object[]{breedName});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadSpeciesData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT species_name FROM species";
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String speciesName = rs.getString("species_name");
                model.addRow(new Object[]{speciesName});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadAllPets(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT pet_id, pname, owner_tc, species, breed, birthDate FROM pets";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("pet_id");
                String ownertc = rs.getString("owner_tc");
                String name = rs.getString("pname");
                String species = rs.getString("species");
                String type = rs.getString("breed");
                Date birthdate = rs.getDate("birthDate");

                model.addRow(new Object[]{id, ownertc, name, species, type, birthdate});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "Database Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void loadAllUser(DefaultTableModel model) {
        model.setRowCount(0);
        String query = """
                SELECT 
                    u.tc, 
                    u.fname, 
                    u.lname, 
                    u.phonenr, 
                    u.address,
                    COUNT(p.owner_tc) AS pet_count
                FROM users u
                LEFT JOIN pets p ON u.tc = p.owner_tc
                GROUP BY u.tc, u.fname, u.lname, u.phonenr, u.address
                HAVING COUNT(p.owner_tc) > 0
               """;

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tc = rs.getString("tc");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String phone = rs.getString("phonenr");
                String address = rs.getString("address");
                int petCount = rs.getInt("pet_count");
                model.addRow(new Object[]{tc, fname, lname, phone, address, petCount});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "Database Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void deletePet(int petId) {
        String deleteQuery = "DELETE FROM pets WHERE pet_id = ?";
        
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(deleteQuery)) {
            stmt.setInt(1, petId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pet deleted successfully.");
            } else {
                System.out.println("Pet not found or could not be deleted.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error deleting pet: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void deleteSpecies(String speciesName) {
        String deleteSpeciesQuery = "DELETE FROM species WHERE species_name = ?";
        speciesName = formatInput(speciesName);
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(deleteSpeciesQuery)) {
            stmt.setString(1, speciesName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
            	
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata: Tür silinirken bir sorun oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatInput(String input) {
        if (input == null || input.isEmpty()) return "";

        String[] words = input.split("\\s+");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formatted.append(word.substring(0, 1).toUpperCase())
                         .append(word.substring(1).toLowerCase())
                         .append(" ");
            }
        }

        return formatted.toString().trim();
    }
    
    private void deleteBreed(String speciesName, String breedName) {
        String deleteBreedQuery = "DELETE FROM breeds WHERE breed_name = ?";
        breedName = formatInput(breedName);

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(deleteBreedQuery)) {
            stmt.setString(1, breedName);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                if (isSpeciesDeleted(speciesName)) {
                    JOptionPane.showMessageDialog(null, "Trigger çalıştı ve tür başarıyla silindi: " + speciesName);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seçilen breed silinemedi: " + breedName, "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata: Breed silinirken bir sorun oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isSpeciesDeleted(String speciesName) throws SQLException {
        String checkSpeciesQuery = "SELECT 1 FROM species WHERE species_name = ?";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(checkSpeciesQuery)) {
            stmt.setString(1, speciesName);
            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next();
            }
        }
    }
    
    private void searchPets(String searchText, DefaultTableModel model) {
        model.setRowCount(0);
        String query = """
                SELECT pet_id, pname, owner_tc, species, breed, birthDate
                FROM pets
                WHERE LOWER(CAST(pet_id AS CHAR)) LIKE LOWER(?) OR
                      LOWER(owner_tc) LIKE LOWER(?) OR
                      LOWER(pname) LIKE LOWER(?) OR
                      LOWER(species) LIKE LOWER(?) OR
                      LOWER(breed) LIKE LOWER(?) OR
                      LOWER(CAST(birthDate AS CHAR)) LIKE LOWER(?)
            """;

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            String likeText = "%" + searchText + "%";
            for (int i = 1; i <= 6; i++) {
                stmt.setString(i, likeText);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("pet_id");
                String ownerTc = rs.getString("owner_tc");
                String name = rs.getString("pname");
                String species = rs.getString("species");
                String breed = rs.getString("breed");
                Date birthDate = rs.getDate("birthDate");

                model.addRow(new Object[]{id, ownerTc, name, species, breed, birthDate});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error searching pets: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void searchUser(String searchText, DefaultTableModel model) {
        model.setRowCount(0);
        String query = """
        	  SELECT 
                tc, 
                fname, 
                lname, 
                phonenr, 
                address,
                (SELECT COUNT(*) FROM pets p WHERE p.owner_tc = u.tc) AS pet_count
              FROM users u
              WHERE tc ILIKE ? OR 
                  fname ILIKE ? OR 
                  lname ILIKE ? OR 
                  phonenr ILIKE ? OR 
                  address ILIKE ?
        """;

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            String likeText = "%" + searchText + "%";
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, likeText);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tc = rs.getString("tc");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String phone = rs.getString("phonenr");
                String address = rs.getString("address");
                int petCount = rs.getInt("pet_count");
                model.addRow(new Object[]{tc, fname, lname, phone, address, petCount});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "Database Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void searchOrders(String searchText, DefaultTableModel model) {
        model.setRowCount(0);

        String query = """
                SELECT order_id, tc, order_date, total_price
                FROM orders
                WHERE LOWER(CAST(order_id AS CHAR)) LIKE LOWER(?) OR
                      LOWER(tc) LIKE LOWER(?) OR
                      LOWER(CAST(order_date AS CHAR)) LIKE LOWER(?) OR
                      LOWER(CAST(total_price AS CHAR)) LIKE LOWER(?)
            """;

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            String likeText = "%" + searchText + "%";
            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, likeText);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String tc = rs.getString("tc");
                Date orderDate = rs.getDate("order_date");
                double totalPrice = rs.getDouble("total_price");
                model.addRow(new Object[]{tc, orderId, totalPrice, orderDate});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error searching orders: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
        	
	private void loadVaccines(DefaultTableModel model) {
	    model.setRowCount(0);
	    String query = "SELECT pname, species, breed, vaccine_date " +
	                   "FROM user_vaccines_view v ";

	    try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
	        ResultSet rs = stmt.executeQuery();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	        while (rs.next()) {
	            String petName = rs.getString("pname");
	            String species = rs.getString("species");
	            String breed = rs.getString("breed");
	            Timestamp vaccineDate = rs.getTimestamp("vaccine_date");
	            String formattedDate = (vaccineDate != null) ? dateFormat.format(vaccineDate) : "N/A";
	            model.addRow(new Object[]{petName, species, breed, formattedDate});
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(
	                null,
	                "Error loading vaccine records: " + ex.getMessage(),
	                "Error",
	                JOptionPane.ERROR_MESSAGE);
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
    
    private String getUserInfo(String ownerTc) {
        String query = "SELECT fname, lname, phonenr, address FROM users WHERE tc = ?";
        StringBuilder userInfo = new StringBuilder();

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, ownerTc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("fname");
                String surname = rs.getString("lname");
                String phonenr = rs.getString("phonenr");
                String address = rs.getString("address");

                userInfo.append("Name: ").append(name).append(" ").append(surname).append("\n")
                        .append("Phone: ").append(phonenr).append("\n")
                        .append("Address: ").append(address);
            } else {
                userInfo.append("No user information found for owner TC: ").append(ownerTc);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            userInfo.append("Error retrieving user information: ").append(ex.getMessage());
        }

        return userInfo.toString();
    }
    
    private String getVaccinationDetails(int petId) {
        String query = "SELECT vaccine_date FROM vaccine WHERE pet_id = ?";
        StringBuilder vaccinationDetails = new StringBuilder();

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                vaccinationDetails.append("No vaccination records found for Pet ID: ").append(petId);
            } else {
                while (rs.next()) {
                    Timestamp vaccinationDate = rs.getTimestamp("vaccine_date");
                    vaccinationDetails.append("-- VACCINATION -- \nVaccination Date : ").append(vaccinationDate).append("\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            vaccinationDetails.append("Error retrieving vaccination details: ").append(ex.getMessage());
        }

        return vaccinationDetails.toString();
    }
    
    private void searchPets1(String searchText, DefaultTableModel model) {
        model.setRowCount(0); // Mevcut satırları temizle
        String query = "SELECT * FROM search_pets_array(?) AS (pet_id INTEGER, pname VARCHAR(20), owner_tc VARCHAR(20), species VARCHAR(20), breed VARCHAR(20), birthDate DATE)";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, searchText); // Arama metnini parametre olarak ekle

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("pet_id");
                String ownerTc = rs.getString("owner_tc");
                String name = rs.getString("pname");
                String species = rs.getString("species");
                String breed = rs.getString("breed");
                Date birthDate = rs.getDate("birthDate");

                // Sonuçları tabloya ekle
                model.addRow(new Object[]{id, ownerTc, name, species, breed, birthDate});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error searching pets: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchOrders1(String searchText, DefaultTableModel model) {
        model.setRowCount(0); // Mevcut satırları temizle
        String query = "SELECT * FROM unnest(search_orders_array(?)) AS (order_id INTEGER, tc VARCHAR(20), order_date DATE, total_price NUMERIC)";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, searchText); // Arama metnini parametre olarak ekle

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String tc = rs.getString("tc");
                Date orderDate = rs.getDate("order_date");
                double totalPrice = rs.getDouble("total_price");

                // Sonuçları tabloya ekle
                model.addRow(new Object[]{orderId, tc, orderDate, totalPrice});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error searching orders: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void searchUser1(String searchText, DefaultTableModel model) {
        model.setRowCount(0); // Mevcut satırları temizle
        String query = "SELECT * FROM unnest(search_users_array(?)) AS (tc VARCHAR(20), fname VARCHAR(20), lname VARCHAR(20), phonenr VARCHAR(20), address TEXT, pet_count INTEGER)";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, searchText); // Arama metnini parametre olarak ekle

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tc = rs.getString("tc");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String phone = rs.getString("phonenr");
                String address = rs.getString("address");
                int petCount = rs.getInt("pet_count");

                // Sonuçları tabloya ekle
                model.addRow(new Object[]{tc, fname, lname, phone, address, petCount});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "Database Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }



}
