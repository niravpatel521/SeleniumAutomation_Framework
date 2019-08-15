package utilities.JDBC;

import utilities.PropertyManager;

import java.sql.*;

public class MySqlConnection {

    PropertyManager propertyManager = new PropertyManager();

    String dbUrl = propertyManager.getResourceBundle.getProperty("mysql.server.url");
    String username = propertyManager.getResourceBundle.getProperty("mysql.user.name");
    String password = propertyManager.getResourceBundle.getProperty("mysql.user.password");

    public Connection setupDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(dbUrl, username, password);
        return con;
    }

    public ResultSet executeQuery(String query) throws ClassNotFoundException, SQLException {
        Statement stmt = setupDB().createStatement();
        return stmt.executeQuery(query);
    }

    public void closeConnection() throws ClassNotFoundException, SQLException {
        setupDB().close();
    }
}
