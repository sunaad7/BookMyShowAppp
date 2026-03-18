public class Main {import java.util.HashMap;
import java.util.Map;

    /**
     * RoomInventory class encapsulates all inventory-related logic.
     */
    class RoomInventory {

        // HashMap to store room type and available count
        private Map<String, Integer> inventory;

        /**
         * Constructor - initializes the inventory
         */
        public RoomInventory() {
            inventory = new HashMap<>();

            // Initialize room types with counts
            inventory.put("Single", 5);
            inventory.put("Double", 3);
            inventory.put("Suite", 2);
        }

        /**
         * Get availability of a specific room type
         */
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        /**
         * Update availability (increase/decrease)
         */
        public void updateAvailability(String roomType, int countChange) {
            int current = inventory.getOrDefault(roomType, 0);
            inventory.put(roomType, current + countChange);
        }

        /**
         * Display full inventory
         */
        public void displayInventory() {
            System.out.println("Current Room Inventory:");
            System.out.println("--------------------------");

            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
            }
        }
    }

    /**
     * Main class - entry point
     */
    public class UseCase3InventorySetup {

        public static void main(String[] args) {

            // Initialize inventory
            RoomInventory inventory = new RoomInventory();

            // Display initial inventory
            inventory.displayInventory();

            // Simulate booking (reduce availability)
            System.out.println("\nBooking 1 Single Room...");
            inventory.updateAvailability("Single", -1);

            // Simulate cancellation (increase availability)
            System.out.println("Cancelling 1 Suite Room...");
            inventory.updateAvailability("Suite", 1);

            // Display updated inventory
            System.out.println("\nUpdated Inventory:");
            inventory.displayInventory();
        }
    }