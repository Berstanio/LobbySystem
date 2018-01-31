package de.berstanio.lobby.bukkit;

import java.sql.*;

public class MySqlConnection {

    private Connection connection;
    private String dbHost = "localhost";
    private String dbPort = "3306";
    private String database = "test";
    private String dbUser = "root";
    private String dbPassword = "";
    // TODO: 17.12.17 Richtige Sachen einfügen!

    public MySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + getDbHost() + ":" + getDbPort() + "/" + getDatabase() + "?" + "user=" + getDbUser() + "&" + "password=" + getDbPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getLobbyPlayer(String player) {
        try {
            String sql = "SELECT lobbyPlayer FROM player WHERE name = ?";
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            preparedStatement.setString(1, player);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void pushOrUpdateLobbyPlayer(String player, String myPlayer) {
        try {
            String sql = "SELECT COUNT(*) FROM player WHERE name = ?";
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            preparedStatement.setString(1, player);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            if (result.getString(1).equalsIgnoreCase("0")) {
                sql = "INSERT INTO player(name, lobbyPlayer)  VALUES(?, ?)";
                preparedStatement = getConnection().prepareStatement(sql);
                preparedStatement.setString(1, player);
                preparedStatement.setString(2, myPlayer);
                preparedStatement.executeUpdate();
            }else {
                sql = "UPDATE player SET lobbyPlayer = ? WHERE name = ?";
                preparedStatement = getConnection().prepareStatement(sql);
                preparedStatement.setString(1, myPlayer);
                preparedStatement.setString(2, player);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}