/**
 * UseCase5BookingRequestQueue
 *
 * This class demonstrates how booking requests are handled using
 * a Queue (FIFO - First Come First Served).
 *
 * It ensures fairness by preserving the order of incoming requests.
 * No inventory updates or room allocation happens at this stage.
 *
 * @author Piyasha Chakraborty
 * @version 5.0
 */

import java.util.*;

/**
 * Reservation class represents a booking request
 */
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
 * Booking Request Queue (FIFO)
 */
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    /**
     * Add booking request to queue
     */
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName() +
                " (" + reservation.getRoomType() + ")");
    }

    /**
     * Display all queued requests (without removing them)
     */
    public void displayQueue() {
        System.out.println("\nCurrent Booking Queue:");
        System.out.println("----------------------------");

        for (Reservation r : queue) {
            System.out.println("Guest: " + r.getGuestName() +
                    " | Room Type: " + r.getRoomType());
        }
    }
}

/**
 * Main class
 */
public class Main {

    public static void main(String[] args) {

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double"));
        bookingQueue.addRequest(new Reservation("Diana", "Single"));

        // Display queue (FIFO order preserved)
        bookingQueue.displayQueue();

        System.out.println("\nAll requests stored in arrival order (FIFO).");
        System.out.println("No rooms allocated yet.");
    }
}