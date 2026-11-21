import java.sql.*;
import java.util.Scanner;

public class VehicleEnquiryApp {

    static final String URL  = "jdbc:mysql://localhost:3306/vehicle_db";
    static final String USER = "root";
    static final String PASS = "your_password";   // change this
    static Scanner sc = new Scanner(System.in);

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    static int readInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(sc.nextLine().trim());
    }

    static String readStr(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    // ---------------- VEHICLE CRUD ----------------

    static void addVehicle() {
        String sql = "INSERT INTO Vehicle(brand, model, year, price, km_driven, fuel_type) VALUES (?,?,?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, readStr("Brand: "));
            ps.setString(2, readStr("Model: "));
            ps.setInt(3, readInt("Year: "));
            ps.setDouble(4, Double.parseDouble(readStr("Price: ")));
            ps.setInt(5, readInt("KM Driven: "));
            ps.setString(6, readStr("Fuel Type: "));
            System.out.println(ps.executeUpdate() > 0 ? "Vehicle added successfully." : "Insert failed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewVehicles() {
        String sql = "SELECT * FROM Vehicle";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.printf("%-4s %-12s %-12s %-6s %-10s %-8s %-8s %-10s%n",
                    "ID", "Brand", "Model", "Year", "Price", "KM", "Fuel", "Status");
            System.out.println("-----------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-4d %-12s %-12s %-6d %-10.2f %-8d %-8s %-10s%n",
                        rs.getInt("vehicle_id"), rs.getString("brand"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("price"), rs.getInt("km_driven"),
                        rs.getString("fuel_type"), rs.getString("status"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateVehicle() {
        String sql = "UPDATE Vehicle SET price=?, status=? WHERE vehicle_id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int id = readInt("Vehicle ID to update: ");
            ps.setDouble(1, Double.parseDouble(readStr("New Price: ")));
            ps.setString(2, readStr("New Status (Available/Sold): "));
            ps.setInt(3, id);
            System.out.println(ps.executeUpdate() > 0 ? "Vehicle updated." : "Vehicle ID not found.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void deleteVehicle() {
        String sql = "DELETE FROM Vehicle WHERE vehicle_id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readInt("Vehicle ID to delete: "));
            System.out.println(ps.executeUpdate() > 0 ? "Vehicle deleted." : "Vehicle ID not found.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------- ENQUIRY CRUD ----------------

    static void addEnquiry() {
        String sql = "INSERT INTO Enquiry(vehicle_id, customer_name, phone, message) VALUES (?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readInt("Vehicle ID: "));
            ps.setString(2, readStr("Customer Name: "));
            ps.setString(3, readStr("Phone: "));
            ps.setString(4, readStr("Message: "));
            System.out.println(ps.executeUpdate() > 0 ? "Enquiry added successfully." : "Insert failed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewEnquiries() {
        String sql = "SELECT e.enquiry_id, v.brand, v.model, e.customer_name, e.phone, "
                   + "e.message, e.enquiry_date, e.status "
                   + "FROM Enquiry e JOIN Vehicle v ON e.vehicle_id = v.vehicle_id";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.printf("%-4s %-18s %-12s %-12s %-22s %-11s %-10s%n",
                    "ID", "Vehicle", "Customer", "Phone", "Message", "Date", "Status");
            System.out.println("--------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-4d %-18s %-12s %-12s %-22s %-11s %-10s%n",
                        rs.getInt("enquiry_id"),
                        rs.getString("brand") + " " + rs.getString("model"),
                        rs.getString("customer_name"), rs.getString("phone"),
                        rs.getString("message"), rs.getDate("enquiry_date"),
                        rs.getString("status"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateEnquiry() {
        String sql = "UPDATE Enquiry SET status=? WHERE enquiry_id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int id = readInt("Enquiry ID to update: ");
            ps.setString(1, readStr("New Status (Pending/Contacted/Closed): "));
            ps.setInt(2, id);
            System.out.println(ps.executeUpdate() > 0 ? "Enquiry updated." : "Enquiry ID not found.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void deleteEnquiry() {
        String sql = "DELETE FROM Enquiry WHERE enquiry_id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readInt("Enquiry ID to delete: "));
            System.out.println(ps.executeUpdate() > 0 ? "Enquiry deleted." : "Enquiry ID not found.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------- MENUS ----------------

    static void vehicleMenu() {
        while (true) {
            System.out.println("\n--- VEHICLE MENU ---");
            System.out.println("1. Add  2. View  3. Update  4. Delete  5. Back");
            switch (readInt("Choice: ")) {
                case 1 -> addVehicle();
                case 2 -> viewVehicles();
                case 3 -> updateVehicle();
                case 4 -> deleteVehicle();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void enquiryMenu() {
        while (true) {
            System.out.println("\n--- ENQUIRY MENU ---");
            System.out.println("1. Add  2. View  3. Update Status  4. Delete  5. Back");
            switch (readInt("Choice: ")) {
                case 1 -> addEnquiry();
                case 2 -> viewEnquiries();
                case 3 -> updateEnquiry();
                case 4 -> deleteEnquiry();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== SECOND HAND VEHICLE SYSTEM =====");
            System.out.println("1. Vehicle Management");
            System.out.println("2. Enquiry Management");
            System.out.println("3. Exit");
            switch (readInt("Choice: ")) {
                case 1 -> vehicleMenu();
                case 2 -> enquiryMenu();
                case 3 -> { System.out.println("Thank you!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
