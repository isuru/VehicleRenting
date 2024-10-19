
package controllers;
import models.Vehicle;
import java.sql.*;
import java.util.HashMap;

public class VehicleController {
    public static String ERROR_MESSAGE;
    
    public static ResultSet loadAllVehicles() {
        ResultSet rs = Vehicle.all();
        return rs;
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
    
    public static boolean deleteVehicle(String vehicleNo) {
        if (Vehicle.delete(vehicleNo)) {
            return true;
            
        } else {
           ERROR_MESSAGE = Vehicle.getErrorMessage();
        }
        
        return false;
    }
}
