package bank.management.system;

import java.sql.*;

public class Conn implements AutoCloseable {  // Implement AutoCloseable
    Connection con;
    Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con =DriverManager.getConnection("jdbc:mysql:///bankmanagementsystem","root","Avismita@1");
            s = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con;
    }

    @Override
    public void close() {
        try {
            if (s != null) s.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
