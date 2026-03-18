import java.io.*;
import java.util.*;

/**
 * Reservation class (Serializable)
 */
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + " | Guest: " + guestName + " | Room Type: " + roomType;
    }
}

/**
 * Room Inventory class (Serializable)
 */
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }

    public Map<String, Integer> getInventoryMap() {
        return inventory;
    }

    public void setInventoryMap(Map<String, Integer> map) {
        inventory = map;
    }
}

/**
 * Booking Service with persistence
 */
class BookingService implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Reservation> confirmedBookings; // reservationId -> Reservation
    private transient RoomInventory inventory; // transient because inventory serialized separately

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        confirmedBookings = new LinkedHashMap<>();
    }

    public void setInventory(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public boolean bookRoom(Reservation reservation) {
        if (inventory.allocateRoom(reservation.getRoomType())) {
            confirmedBookings.put(reservation.getReservationId(), reservation);
            System.out.println("Booking confirmed: " + reservation);
            return true;
        } else {
            System.out.println("Booking failed (no availability): " + reservation);
            return false;
        }
    }

    public void cancelBooking(String reservationId) {
        Reservation r = confirmedBookings.remove(reservationId);
        if (r != null) {
            inventory.releaseRoom(r.getRoomType());
            System.out.println("Booking cancelled: " + r);
        } else {
            System.out.println("Cannot cancel. Reservation not found: " + reservationId);
        }
    }

    public void displayConfirmedBookings() {
        System.out.println("\nConfirmed Bookings:");
        for (Reservation r : confirmedBookings.values()) {
            System.out.println(r);
        }
    }

    public Map<String, Reservation> getConfirmedBookings() {
        return confirmedBookings;
    }

    public void setConfirmedBookings(Map<String, Reservation> bookings) {
        confirmedBookings = bookings;
    }
}

/**
 * Persistence Service
 */
class PersistenceService {

    private static final String INVENTORY_FILE = "inventory.ser";
    private static final String BOOKINGS_FILE = "bookings.ser";

    public static void save(RoomInventory inventory, BookingService bookingService) {
        try (ObjectOutputStream invOut = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE));
             ObjectOutputStream bookOut = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {

            invOut.writeObject(inventory.getInventoryMap());
            bookOut.writeObject(bookingService.getConfirmedBookings());

            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public static void restore(RoomInventory inventory, BookingService bookingService) {
        try (ObjectInputStream invIn = new ObjectInputStream(new FileInputStream(INVENTORY_FILE));
             ObjectInputStream bookIn = new ObjectInputStream(new FileInputStream(BOOKINGS_FILE))) {

            @SuppressWarnings("unchecked")
            Map<String, Integer> invMap = (Map<String, Integer>) invIn.readObject();
            inventory.setInventoryMap(invMap);

            @SuppressWarnings("unchecked")
            Map<String, Reservation> bookings = (Map<String, Reservation>) bookIn.readObject();
            bookingService.setConfirmedBookings(bookings);

            System.out.println("\nSystem state restored successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("\nNo previous saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error restoring state: " + e.getMessage());
        }
    }
}

/**
 * Main Class
 */
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Step 1: Initialize system
        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);
        bookingService.setInventory(inventory);

        // Step 2: Restore previous state
        PersistenceService.restore(inventory, bookingService);

        // Step 3: Perform some bookings
        bookingService.bookRoom(new Reservation("S1", "Alice", "Single"));
        bookingService.bookRoom(new Reservation("S2", "Bob", "Single"));
        bookingService.bookRoom(new Reservation("D1", "Charlie", "Double"));

        // Step 4: Display current state
        bookingService.displayConfirmedBookings();
        inventory.displayInventory();

        // Step 5: Save system state
        PersistenceService.save(inventory, bookingService);
    }
}