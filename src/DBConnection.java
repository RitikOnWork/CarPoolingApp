import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {



    public static Connection getConnection() {
        String URL ="jdbc:postgresql://localhost:5432/car_pooling";
        String USER = "postgres";
        String PASSWORD = "ritik2006";
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
