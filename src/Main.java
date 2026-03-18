import java.util.*;

/**
 * Room domain model
 */
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

/**
 * Centralized Inventory (read + write, but search will only read)
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 0);   // unavailable
        inventory.put("Suite", 2);
    }

    // Read-only method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Expose all room types
    public Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

/**
 * Search Service (READ-ONLY)
 */
class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomData;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;

        // Room details (domain data)
        roomData = new HashMap<>();
        roomData.put("Single", new Room("Single", 1000.0, "Bed, WiFi"));
        roomData.put("Double", new Room("Double", 1800.0, "2 Beds, WiFi, TV"));
        roomData.put("Suite", new Room("Suite", 3000.0, "Luxury Bed, WiFi, TV, AC"));
    }

    /**
     * Search available rooms (READ-ONLY)
     */
    public void searchAvailableRooms() {

        System.out.println("Available Rooms:");
        System.out.println("--------------------------");

        for (String type : inventory.getRoomTypes()) {

            int available = inventory.getAvailability(type);

            // Defensive check: only show available rooms
            if (available > 0) {
                Room room = roomData.get(type);

                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: " + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available Count: " + available);
                System.out.println();
            }
        }
    }
}

/**
 * Main class
 */
public class Main {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (READ-ONLY)
        searchService.searchAvailableRooms();

        // Verify inventory is unchanged
        System.out.println("Search completed without modifying inventory.");
    }
}