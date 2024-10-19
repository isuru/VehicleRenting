
package dbpkg;
import java.sql.*;


public class DB {
    private static Connection connection;
    
    public static Connection createConnection() {
        String dbloc = "jdbc:mysql://localhost/vehicle_rent";

        try {
            connection = DriverManager.getConnection(dbloc, "root", "");
            
        } catch (SQLException e) {
            connection = null;
            System.err.println(e.getMessage());
        }
        
        return connection;
    }
    
    public static void closeConnection() {
        try {        
            if (null != connection && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            connection = null;
            System.err.println(e.getMessage());
        }        
    }

}
