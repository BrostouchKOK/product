import java.sql.*;
import java.util.Scanner;

public class Product {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connect to database");

        System.out.print("Enter product name: ");
        String proName = scanner.nextLine();

        System.out.print("Enter product price: ");
        Double price = scanner.nextDouble();

        System.out.print("Active (Enter 1 for true, 0 for false): ");
        int activeInput = scanner.nextInt();
        boolean active;

        // Validate and convert activeInput to boolean
        if (activeInput == 1) {
            active = true;
        } else if (activeInput == 0) {
            active = false;
        } else {
            throw new IllegalArgumentException("Invalid input for Active. Enter 1 for true, 0 for false.");
        }

        try {
            insertData(proName, price, active);
            showAllProduct();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();  // Close scanner at the end
        }
    }

    static Connection connectionToDb() throws ClassNotFoundException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/products";
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(dbUrl, "postgres", "Kbtweb#11113333");
        return connection;
    }

    static void insertData(String productName, Double price, boolean active) throws SQLException, ClassNotFoundException {
        Connection con = connectionToDb();
        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO product(name, price_per_unit, active_for_sell) VALUES (?,?,?)");
        preparedStatement.setString(1, productName);
        preparedStatement.setDouble(2, price);
        preparedStatement.setBoolean(3, active);
        preparedStatement.executeUpdate();  // Use executeUpdate for INSERT statements
        con.close();  // Close connection after use
    }

    static public void showAllProduct() {
        try {
            Connection connection = connectionToDb();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM product";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");  // Correct column name
                double price = resultSet.getDouble("price_per_unit");  // Correct column name
                boolean active = resultSet.getBoolean("active_for_sell");  // Correct column name
                System.out.println("Product ID: " + id);
                System.out.println("Product Name: " + name);
                System.out.println("Product Price_per_unit: $" + price);
                System.out.println("Active_for_sell: " + active);
                System.out.println("==========================================");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
