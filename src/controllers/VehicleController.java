
package controllers;
import models.Vehicle;
import dbpkg.DB;
import java.sql.*;
import java.util.HashMap;

public class VehicleController {
    public static String ERROR_MESSAGE;
    
    public static ResultSet loadAllVehicles(String status) {
        ResultSet rs = Vehicle.all(status);
        return rs;
    }
    
    public static HashMap<String,String> getVehicleDetais(String vehicleNo) {
        HashMap<String,String> details = new HashMap<>();
        
        ResultSet rs = Vehicle.find(vehicleNo);
        
        try {
            while(rs.next()) {
                details.put("vehicle_no", rs.getString("vehicle_no"));
                details.put("type", rs.getString("type"));
                details.put("year", String.valueOf(rs.getInt("year")));  
                details.put("mileage", String.valueOf(rs.getInt("mileage")));
                details.put("make", rs.getString("make"));
                details.put("model", rs.getString("model"));
                details.put("engine_no", rs.getString("engine_no"));
                details.put("chassis_no", rs.getString("chassis_no"));
                details.put("owner_name", rs.getString("owner_name"));
                details.put("owner_tel", rs.getString("owner_tel"));
                details.put("owner_address", rs.getString("owner_address"));
                details.put("daily_rent", String.valueOf(rs.getInt("daily_rent")));
                details.put("weekly_rent", String.valueOf(rs.getInt("weekly_rent")));
                details.put("monthly_rent", String.valueOf(rs.getInt("monthly_rent")));
            }

            return details;
        
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
            System.err.println(ERROR_MESSAGE);
            
        } finally {
            DB.closeConnection();
        }

        return details;
    }

    public static boolean addNewVechicle(HashMap<String,Object> params) {
        Vehicle vehicle = new Vehicle(params);
        
        if (vehicle.create()) {
           return true;
           
        } else {
           ERROR_MESSAGE = Vehicle.getErrorMessage();
        }
        
        return false;
    }
    
    public static boolean updateVechicle(HashMap<String,Object> params) {
        if (Vehicle.update(params)) {
           return true;
           
        } else {
           ERROR_MESSAGE = Vehicle.getErrorMessage();
        }
        
        return false;
    }
    
    public static boolean deleteVehicle(String vehicleNo) {
        if (Vehicle.delete(vehicleNo)) {
            return true;
            
        } else {
           ERROR_MESSAGE = Vehicle.getErrorMessage();
        }
        
        return false;
    }
}
