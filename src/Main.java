public class Main {

    /**
     * Main method - entry point of the Java application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        // Initialize room data
        int room1Number = 101;
        String room1Type = "Single";
        double room1Price = 1000.0;
        boolean room1Available = true;

        int room2Number = 102;
        String room2Type = "Double";
        double room2Price = 1800.0;
        boolean room2Available = true;

        int room3Number = 103;
        String room3Type = "Suite";
        double room3Price = 3000.0;
        boolean room3Available = false;

        // Display room details
        System.out.println("Hotel Room Initialization");
        System.out.println("----------------------------");

        System.out.println("Room Number: " + room1Number);
        System.out.println("Type: " + room1Type);
        System.out.println("Price: " + room1Price);
        System.out.println("Available: " + room1Available);
        System.out.println();

        System.out.println("Room Number: " + room2Number);
        System.out.println("Type: " + room2Type);
        System.out.println("Price: " + room2Price);
        System.out.println("Available: " + room2Available);
        System.out.println();

        System.out.println("Room Number: " + room3Number);
        System.out.println("Type: " + room3Type);
        System.out.println("Price: " + room3Price);
        System.out.println("Available: " + room3Available);

        // End message
        System.out.println("\nRoom initialization completed successfully.");
    }
}