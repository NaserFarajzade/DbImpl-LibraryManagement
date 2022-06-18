
package project;
import java.sql.*;

public class ConnectionProvider {

    public static Connection getCon(){
        try{
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1:5432/lms" , "postgres" , "123456");
            return con;
            
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    } 
    
}
