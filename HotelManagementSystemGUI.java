import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class HotelManagementSystemGUI {
    private static Map<Integer, Room> rooms = new HashMap<>();
    private static Map<Integer, Customer> bookings = new HashMap<>();
    private static Map<Integer, Integer> foodBills = new HashMap<>();
    
    private static JFrame mainFrame;
    private static JTextArea outputArea;
    private static JPanel cardPanel;
    private static CardLayout cardLayout;
    
    public static void main(String[] args) {
        setupRooms();
        createAndShowGUI();
    }
    
    private static void setupRooms() {
        for (int i = 101; i <= 105; i++) rooms.put(i, new StandardRoom(i));
        for (int i = 201; i <= 205; i++) rooms.put(i, new DeluxeRoom(i));
        for (int i = 301; i <= 303; i++) rooms.put(i, new SuiteRoom(i));
    }
    
    private static void createAndShowGUI() {
        mainFrame = new JFrame("Hotel Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new BorderLayout());
        
        // Create menu panel
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        String[] buttonLabels = {"Check-in", "Check-out", "View Rooms", "View Bookings", "Room Service", "Exit"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new MenuButtonListener());
            menuPanel.add(button);
        }
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        // Create card panel for different views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(scrollPane, "Main");
        cardPanel.add(createCheckInPanel(), "CheckIn");
        cardPanel.add(createCheckOutPanel(), "CheckOut");
        cardPanel.add(createRoomServicePanel(), "RoomService");
        
        mainFrame.add(menuPanel, BorderLayout.WEST);
        mainFrame.add(cardPanel, BorderLayout.CENTER);
        
        mainFrame.setVisible(true);
    }
    
    private static JPanel createCheckInPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Available rooms list
        DefaultListModel<String> roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        updateAvailableRoomsList(roomListModel);
        
        JScrollPane listScrollPane = new JScrollPane(roomList);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField daysField = new JTextField();
        
        formPanel.add(new JLabel("Customer Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Days to Stay:"));
        formPanel.add(daysField);
        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        JButton bookButton = new JButton("Book Room");
        
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        bookButton.addActionListener(e -> {
            int selectedIndex = roomList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a room", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String selectedRoom = roomListModel.getElementAt(selectedIndex);
            int roomNumber = Integer.parseInt(selectedRoom.split(" ")[0]);
            
            try {
                String name = nameField.getText();
                String contact = contactField.getText();
                int days = Integer.parseInt(daysField.getText());
                
                if (name.isEmpty() || contact.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Room room = rooms.get(roomNumber);
                room.isBooked = true;
                bookings.put(roomNumber, new Customer(name, contact, roomNumber, days));
                foodBills.put(roomNumber, 0);
                
                String message = "Welcome to our Hotel, " + name + "!\n";
                message += "Room " + roomNumber + " (" + room.getRoomType() + ") booked successfully.\n";
                
                switch (room.getRoomType()) {
                    case "Deluxe" -> message += "You get free breakfast with this room.";
                    case "Suite" -> message += "You get free breakfast and access to the swimming pool.";
                    default -> message += "No extra services with this room.";
                }
                
                outputArea.append(message + "\n");
                cardLayout.show(cardPanel, "Main");
                updateAvailableRoomsList(roomListModel);
                nameField.setText("");
                contactField.setText("");
                daysField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter valid number of days", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);
        
        panel.add(listScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static JPanel createCheckOutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Booked rooms list
        DefaultListModel<String> bookedListModel = new DefaultListModel<>();
        JList<String> bookedList = new JList<>(bookedListModel);
        updateBookedRoomsList(bookedListModel);
        
        JScrollPane listScrollPane = new JScrollPane(bookedList);
        
        // Payment panel
        JPanel paymentPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        ButtonGroup paymentGroup = new ButtonGroup();
        JRadioButton upiButton = new JRadioButton("UPI");
        JRadioButton creditButton = new JRadioButton("Credit Card");
        JRadioButton debitButton = new JRadioButton("Debit Card");
        JRadioButton cashButton = new JRadioButton("Cash");
        
        paymentGroup.add(upiButton);
        paymentGroup.add(creditButton);
        paymentGroup.add(debitButton);
        paymentGroup.add(cashButton);
        
        JPanel radioPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        radioPanel.add(upiButton);
        radioPanel.add(creditButton);
        radioPanel.add(debitButton);
        radioPanel.add(cashButton);
        
        paymentPanel.add(new JLabel("Select Payment Method:"));
        paymentPanel.add(radioPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        JButton checkoutButton = new JButton("Check Out");
        
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        checkoutButton.addActionListener(e -> {
            int selectedIndex = bookedList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a room", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String selectedRoom = bookedListModel.getElementAt(selectedIndex);
            int roomNumber = Integer.parseInt(selectedRoom.split(" ")[0]);
            
            String paymentMethod = "";
            if (upiButton.isSelected()) paymentMethod = "UPI";
            else if (creditButton.isSelected()) paymentMethod = "Credit Card";
            else if (debitButton.isSelected()) paymentMethod = "Debit Card";
            else if (cashButton.isSelected()) paymentMethod = "Cash";
            else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a payment method", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Customer customer = bookings.get(roomNumber);
            Room room = rooms.get(roomNumber);
            double roomBill = room.price * customer.daysStayed;
            int foodBill = foodBills.getOrDefault(roomNumber, 0);
            double total = roomBill + foodBill;
            
            String message = "Check-out details for Room " + roomNumber + ":\n";
            message += "Customer: " + customer.name + "\n";
            message += "Room charges: ₹" + roomBill + "\n";
            message += "Food charges: ₹" + foodBill + "\n";
            message += "Total bill: ₹" + total + "\n";
            message += "Payment received via " + paymentMethod + ".\n";
            message += "Thank you for staying with us!";
            
            bookings.remove(roomNumber);
            foodBills.remove(roomNumber);
            room.isBooked = false;
            
            outputArea.append(message + "\n");
            cardLayout.show(cardPanel, "Main");
            updateBookedRoomsList(bookedListModel);
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(checkoutButton);
        
        panel.add(listScrollPane, BorderLayout.CENTER);
        panel.add(paymentPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static JPanel createRoomServicePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Booked rooms list
        DefaultListModel<String> bookedListModel = new DefaultListModel<>();
        JList<String> bookedList = new JList<>(bookedListModel);
        updateBookedRoomsList(bookedListModel);
        
        JScrollPane listScrollPane = new JScrollPane(bookedList);
        
        // Service options
        JPanel servicePanel = new JPanel(new GridLayout(4, 1, 5, 5));
        ButtonGroup serviceGroup = new ButtonGroup();
        JRadioButton cleaningButton = new JRadioButton("Cleaning Service");
        JRadioButton foodButton = new JRadioButton("Food Service");
        JRadioButton emergencyButton = new JRadioButton("Emergency");
        
        serviceGroup.add(cleaningButton);
        serviceGroup.add(foodButton);
        serviceGroup.add(emergencyButton);
        
        JPanel radioPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        radioPanel.add(cleaningButton);
        radioPanel.add(foodButton);
        radioPanel.add(emergencyButton);
        
        servicePanel.add(new JLabel("Select Service Type:"));
        servicePanel.add(radioPanel);
        
        // Food menu panel (initially hidden)
        JPanel foodMenuPanel = new JPanel(new BorderLayout());
        foodMenuPanel.setVisible(false);
        
        DefaultListModel<String> foodListModel = new DefaultListModel<>();
        Map<Integer, Integer> foodPrices = Map.of(
                0, 250, // Pizza
                1, 150, // Sandwich
                2, 30,  // Tea
                3, 50,  // Coffee
                4, 200  // Biryani
        );
        
        foodListModel.addElement("Pizza - ₹250");
        foodListModel.addElement("Sandwich - ₹150");
        foodListModel.addElement("Tea - ₹30");
        foodListModel.addElement("Coffee - ₹50");
        foodListModel.addElement("Biryani - ₹200");
        
        JList<String> foodList = new JList<>(foodListModel);
        foodList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane foodScrollPane = new JScrollPane(foodList);
        
        JButton addFoodButton = new JButton("Add to Order");
        JLabel foodTotalLabel = new JLabel("Total: ₹0");
        
        addFoodButton.addActionListener(e -> {
            List<String> selectedItems = foodList.getSelectedValuesList();
            int total = 0;
            for (String item : selectedItems) {
                int index = foodListModel.indexOf(item);
                total += foodPrices.get(index);
            }
            foodTotalLabel.setText("Total: ₹" + total);
        });
        
        JPanel foodButtonPanel = new JPanel(new BorderLayout());
        foodButtonPanel.add(addFoodButton, BorderLayout.WEST);
        foodButtonPanel.add(foodTotalLabel, BorderLayout.EAST);
        
        foodMenuPanel.add(foodScrollPane, BorderLayout.CENTER);
        foodMenuPanel.add(foodButtonPanel, BorderLayout.SOUTH);
        
        // Show/hide food menu based on selection
        foodButton.addActionListener(e -> foodMenuPanel.setVisible(true));
        cleaningButton.addActionListener(e -> foodMenuPanel.setVisible(false));
        emergencyButton.addActionListener(e -> foodMenuPanel.setVisible(false));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        JButton requestButton = new JButton("Request Service");
        
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));
        requestButton.addActionListener(e -> {
            int selectedIndex = bookedList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a room", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String selectedRoom = bookedListModel.getElementAt(selectedIndex);
            int roomNumber = Integer.parseInt(selectedRoom.split(" ")[0]);
            
            if (cleaningButton.isSelected()) {
                outputArea.append("Cleaning service requested for Room " + roomNumber + "\n");
                cardLayout.show(cardPanel, "Main");
            } 
            else if (foodButton.isSelected()) {
                List<String> selectedItems = foodList.getSelectedValuesList();
                if (selectedItems.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "Please select food items", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int total = 0;
                for (String item : selectedItems) {
                    int index = foodListModel.indexOf(item);
                    total += foodPrices.get(index);
                }
                
                int currentBill = foodBills.getOrDefault(roomNumber, 0);
                foodBills.put(roomNumber, currentBill + total);
                
                outputArea.append("Food order placed for Room " + roomNumber + ". Total: ₹" + total + "\n");
                cardLayout.show(cardPanel, "Main");
            } 
            else if (emergencyButton.isSelected()) {
                outputArea.append("Emergency assistance requested for Room " + roomNumber + "\n");
                cardLayout.show(cardPanel, "Main");
            } 
            else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a service type", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(requestButton);
        
        panel.add(listScrollPane, BorderLayout.WEST);
        panel.add(servicePanel, BorderLayout.NORTH);
        panel.add(foodMenuPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static void updateAvailableRoomsList(DefaultListModel<String> model) {
        model.clear();
        for (Room room : rooms.values()) {
            if (!room.isBooked) {
                model.addElement(room.roomNumber + " - " + room.getRoomType() + " (₹" + room.price + "/night)");
            }
        }
    }
    
    private static void updateBookedRoomsList(DefaultListModel<String> model) {
        model.clear();
        for (Map.Entry<Integer, Customer> entry : bookings.entrySet()) {
            int roomNumber = entry.getKey();
            Customer customer = entry.getValue();
            Room room = rooms.get(roomNumber);
            model.addElement(roomNumber + " - " + customer.name + " (" + room.getRoomType() + ")");
        }
    }
    
    private static class MenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "Check-in" -> cardLayout.show(cardPanel, "CheckIn");
                case "Check-out" -> cardLayout.show(cardPanel, "CheckOut");
                case "View Rooms" -> {
                    outputArea.setText("");
                    for (Room room : rooms.values()) {
                        outputArea.append("Room " + room.roomNumber + " - " + room.getRoomType() + 
                                        " (₹" + room.price + "/night) - " + 
                                        (room.isBooked ? "Booked" : "Available") + "\n");
                    }
                    cardLayout.show(cardPanel, "Main");
                }
                case "View Bookings" -> {
                    outputArea.setText("");
                    if (bookings.isEmpty()) {
                        outputArea.append("No current bookings\n");
                    } else {
                        for (Map.Entry<Integer, Customer> entry : bookings.entrySet()) {
                            Customer customer = entry.getValue();
                            outputArea.append("Room " + entry.getKey() + ": " + customer.name + 
                                            " (" + customer.contact + ") - " + 
                                            customer.daysStayed + " days\n");
                        }
                    }
                    cardLayout.show(cardPanel, "Main");
                }
                case "Room Service" -> cardLayout.show(cardPanel, "RoomService");
                case "Exit" -> System.exit(0);
            }
        }
    }
    
    // Room and Customer classes remain the same as in your original code
    abstract static class Room {
        int roomNumber;
        boolean isBooked;
        double price;

        public Room(int roomNumber, double price) {
            this.roomNumber = roomNumber;
            this.price = price;
            this.isBooked = false;
        }

        abstract String getRoomType();
    }

    static class StandardRoom extends Room {
        public StandardRoom(int roomNumber) {
            super(roomNumber, 2000);
        }

        String getRoomType() {
            return "Standard";
        }
    }

    static class DeluxeRoom extends Room {
        public DeluxeRoom(int roomNumber) {
            super(roomNumber, 3500);
        }

        String getRoomType() {
            return "Deluxe";
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom(int roomNumber) {
            super(roomNumber, 5000);
        }

        String getRoomType() {
            return "Suite";
        }
    }

    static class Customer {
        String name;
        String contact;
        int roomNumber;
        int daysStayed;

        public Customer(String name, String contact, int roomNumber, int daysStayed) {
            this.name = name;
            this.contact = contact;
            this.roomNumber = roomNumber;
            this.daysStayed = daysStayed;
        }
    }
}