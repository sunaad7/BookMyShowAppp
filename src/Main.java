

import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * Inventory Service
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

/**
 * Booking Service (Core Allocation Logic)
 */
class BookingService {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    // Track allocated rooms
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomTypeToIds;

    private int idCounter = 1;

    public BookingService(Queue<Reservation> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.roomTypeToIds = new HashMap<>();
    }

    /**
     * Process all booking requests
     */
    public void processBookings() {

        System.out.println("\nProcessing Booking Requests...");
        System.out.println("--------------------------------");

        while (!queue.isEmpty()) {

            Reservation request = queue.poll();
            String type = request.getRoomType();

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId = type.substring(0, 1).toUpperCase() + idCounter++;

                // Ensure uniqueness
                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    // Map room type → assigned IDs
                    roomTypeToIds
                            .computeIfAbsent(type, k -> new HashSet<>())
                            .add(roomId);

                    // Update inventory immediately
                    inventory.reduceAvailability(type);

                    // Confirm booking
                    System.out.println("Booking Confirmed for " +
                            request.getGuestName() +
                            " | Room Type: " + type +
                            " | Room ID: " + roomId);

                }

            } else {
                System.out.println("Booking Failed for " +
                        request.getGuestName() +
                        " | Room Type: " + type +
                        " (No Availability)");
            }
        }
    }

    /**
     * Display allocated rooms
     */
    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        System.out.println("----------------------");

        for (String type : roomTypeToIds.keySet()) {
            System.out.println(type + " -> " + roomTypeToIds.get(type));
        }
    }
}

/**
 * Main Class
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Create booking queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single"));
        queue.offer(new Reservation("Bob", "Single"));
        queue.offer(new Reservation("Charlie", "Single")); // should fail
        queue.offer(new Reservation("Diana", "Suite"));
        queue.offer(new Reservation("Eve", "Double"));

        // Step 2: Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Step 3: Process bookings
        BookingService bookingService = new BookingService(queue, inventory);
        bookingService.processBookings();

        // Step 4: Show results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}