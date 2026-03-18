import java.util.*;


class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType;
    }
}

/**
 * Booking History - stores confirmed reservations
 */
class BookingHistory {

    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    /**
     * Add a confirmed reservation to history
     */
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    /**
     * Retrieve all bookings
     */
    public List<Reservation> getAllBookings() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}

/**
 * Booking Report Service - generates summaries
 */
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    /**
     * Display all confirmed bookings
     */
    public void displayAllBookings() {
        System.out.println("\nBooking History Report:");
        System.out.println("----------------------------");

        for (Reservation r : history.getAllBookings()) {
            System.out.println(r);
        }
    }

    /**
     * Display summary by room type
     */
    public void displaySummaryByRoomType() {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getAllBookings()) {
            summary.put(r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nBooking Summary by Room Type:");
        System.out.println("----------------------------");
        for (String type : summary.keySet()) {
            System.out.println(type + ": " + summary.get(type));
        }
    }
}

/**
 * Main Class
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Initialize booking history
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed reservations
        history.addReservation(new Reservation("S1", "Alice", "Single"));
        history.addReservation(new Reservation("S2", "Bob", "Single"));
        history.addReservation(new Reservation("D1", "Eve", "Double"));
        history.addReservation(new Reservation("SU1", "Diana", "Suite"));

        // Step 3: Initialize reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Step 4: Generate reports
        reportService.displayAllBookings();
        reportService.displaySummaryByRoomType();
    }
}