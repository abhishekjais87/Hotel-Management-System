import java.util.*;

abstract class Room {
    int roomNumber;
    boolean isBooked;
    double price;

    public Room(int roomNumber, double price) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.isBooked = false;
    }

    abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Room Number: " + roomNumber + ", Type: " + getRoomType() + ", Price: ₹" + price + ", Booked: " + isBooked);
    }
}

class StandardRoom extends Room {
    public StandardRoom(int roomNumber) {
        super(roomNumber, 2000);
    }

    String getRoomType() {
        return "Standard";
    }
}

class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber) {
        super(roomNumber, 3500);
    }

    String getRoomType() {
        return "Deluxe";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) {
        super(roomNumber, 5000);
    }

    String getRoomType() {
        return "Suite";
    }
}

class Customer {
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

    public void display() {
        System.out.println("Customer: " + name + ", Contact: " + contact + ", Room: " + roomNumber + ", Days Stayed: " + daysStayed);
    }
}

public class HotelManagementSystem {
    static Map<Integer, Room> rooms = new HashMap<>();
    static Map<Integer, Customer> bookings = new HashMap<>();
    static Map<Integer, Integer> foodBills = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        setupRooms();
        while (true) {
            System.out.println("\n--- Hotel Management System ---");
            System.out.println("1. Check-in");
            System.out.println("2. Check-out");
            System.out.println("3. View All Rooms");
            System.out.println("4. View Bookings");
            System.out.println("5. Room Service");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> checkIn();
                case 2 -> checkOut();
                case 3 -> viewRooms();
                case 4 -> viewBookings();
                case 5 -> roomService();
                case 6 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void setupRooms() {
        for (int i = 101; i <= 105; i++) rooms.put(i, new StandardRoom(i));
        for (int i = 201; i <= 205; i++) rooms.put(i, new DeluxeRoom(i));
        for (int i = 301; i <= 303; i++) rooms.put(i, new SuiteRoom(i));
    }

    static void checkIn() {
        List<Integer> availableRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (!room.isBooked) {
                room.displayDetails();
                availableRooms.add(room.roomNumber);
            }
        }

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms.");
            return;
        }

        System.out.print("Enter the room number you want to check into: ");
        int selectedRoom = Integer.parseInt(scanner.nextLine());

        if (!rooms.containsKey(selectedRoom)) {
            System.out.println("Invalid room number.");
            return;
        }

        Room room = rooms.get(selectedRoom);
        if (room.isBooked) {
            System.out.println("Room already booked.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine();
        System.out.print("Enter number of days to stay: ");
        int days = Integer.parseInt(scanner.nextLine());

        room.isBooked = true;
        bookings.put(selectedRoom, new Customer(name, contact, selectedRoom, days));
        foodBills.put(selectedRoom, 0);

        System.out.println("Welcome to our Hotel, " + name + "!");
        System.out.println("Room " + selectedRoom + " (" + room.getRoomType() + ") booked successfully.");

        switch (room.getRoomType()) {
            case "Deluxe" -> System.out.println("You get free breakfast with this room.");
            case "Suite" -> {
                System.out.println("You get free breakfast.");
                System.out.println("You also have access to the swimming pool.");
            }
            default -> System.out.println("No extra services with this room.");
        }
    }

    static void checkOut() {
        System.out.print("Enter room number: ");
        int roomNum = Integer.parseInt(scanner.nextLine());

        if (bookings.containsKey(roomNum)) {
            Customer customer = bookings.get(roomNum);
            Room room = rooms.get(roomNum);
            double roomBill = room.price * customer.daysStayed;
            int foodBill = foodBills.getOrDefault(roomNum, 0);
            double total = roomBill + foodBill;

            System.out.println("Room charges: ₹" + roomBill);
            System.out.println("Food charges: ₹" + foodBill);
            System.out.println("Total bill: ₹" + total);

            System.out.println("Select payment method:");
            System.out.println("1. UPI\n2. Credit Card\n3. Debit Card\n4. Cash");
            System.out.print("Enter choice: ");
            int paymentChoice = Integer.parseInt(scanner.nextLine());

            String paymentMethod = switch (paymentChoice) {
                case 1 -> "UPI";
                case 2 -> "Credit Card";
                case 3 -> "Debit Card";
                case 4 -> "Cash";
                default -> "Unknown";
            };

            if (paymentMethod.equals("Unknown")) {
                System.out.println("Invalid payment method selected.");
                return;
            }

            bookings.remove(roomNum);
            foodBills.remove(roomNum);
            room.isBooked = false;

            System.out.println("Check-out successful.");
            System.out.println("Payment of ₹" + total + " received via " + paymentMethod + ".");
        } else {
            System.out.println("No booking found for this room.");
        }
    }

    static void viewRooms() {
        for (Room room : rooms.values()) {
            room.displayDetails();
        }
    }

    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Customer customer : bookings.values()) {
                customer.display();
            }
        }
    }

    static void roomService() {
        System.out.print("Enter room number: ");
        int roomNum = Integer.parseInt(scanner.nextLine());

        if (!rooms.containsKey(roomNum) || !rooms.get(roomNum).isBooked) {
            System.out.println("Room not found or not occupied.");
            return;
        }

        System.out.println("Select room service option:");
        System.out.println("1. Cleaning\n2. Food\n3. Emergency");
        System.out.print("Enter choice: ");
        int serviceChoice = Integer.parseInt(scanner.nextLine());

        switch (serviceChoice) {
            case 1 -> System.out.println("Cleaning service has been requested for Room " + roomNum + ".");
            case 2 -> {
                Map<Integer, String> foodMenu = Map.of(
                        1, "Pizza - ₹250",
                        2, "Sandwich - ₹150",
                        3, "Tea - ₹30",
                        4, "Coffee - ₹50",
                        5, "Biryani - ₹200"
                );

                Map<Integer, Integer> foodPrices = Map.of(
                        1, 250,
                        2, 150,
                        3, 30,
                        4, 50,
                        5, 200
                );

                int total = 0;
                while (true) {
                    System.out.println("\nFood Menu:");
                    for (int key : foodMenu.keySet()) {
                        System.out.println(key + ". " + foodMenu.get(key));
                    }
                    System.out.println("0. Done Ordering");
                    System.out.print("Select item number: ");
                    int item = Integer.parseInt(scanner.nextLine());

                    if (item == 0) break;
                    if (foodMenu.containsKey(item)) {
                        total += foodPrices.get(item);
                        System.out.println(foodMenu.get(item).split("-")[0].trim() + " added to order.");
                    } else {
                        System.out.println("Invalid item number.");
                    }
                }

                int currentBill = foodBills.getOrDefault(roomNum, 0);
                foodBills.put(roomNum, currentBill + total);
                System.out.println("Food ordered. Total added to room " + roomNum + ": ₹" + total);
            }
            case 3 -> System.out.println("Emergency assistance has been requested for Room " + roomNum + ".");
            default -> System.out.println("Invalid service option.");
        }
    }
}
