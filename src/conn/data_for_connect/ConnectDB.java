package conn.data_for_connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    public final String driver = "com.postgresql.jdbc.Driver";
    public static final String url = "jdbc:postgresql://localhost:5432/mydb";
    public static final String user = "postgres";
    public static final String password = "1234";



    public static Connection getConnection(){
        try {
            Connection con = DriverManager.getConnection(url, user, password);

            return con;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
