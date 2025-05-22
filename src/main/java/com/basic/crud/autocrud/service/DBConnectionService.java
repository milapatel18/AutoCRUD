package com.basic.crud.autocrud.service;

import com.basic.crud.autocrud.entity.DataUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DBConnectionService {
    private static Map<String, Connection> connectionMap = new HashMap<>();
    /**
     * db connection unique name as a key
     * Value contains a list of Tables with their relevant metadata
     * This object can be refreshed manually from UI or strategically while New Con created/Updated/Deleted #TODO
     */
    private static Map<String, Connection> dbMetadataDetails = new HashMap<>();


    public void createNewDatabaseConnection(String dbUniqueName, String dbType, String server, String port,
                                            String dbName, String user, String pass) {
        if (dbType.equals(DataUtils.MYSQL)) {
            String url = "jdbc:mysql://"+server+":"+port+"/"+dbName;
            try {
                Class.forName(DataUtils.MYSQL_CLASS);
                Connection con = DriverManager.getConnection(url, user, pass);
                connectionMap.put(dbUniqueName,con);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateNewDatabaseConnection(String dbUniqueName, String dbType, String server, String port,
                                            String dbName, String user, String pass) {
        if (dbType.equals(DataUtils.MYSQL)) {
            String url = "jdbc:mysql://"+server+":"+port+"/"+dbName;
            try {
                Class.forName(DataUtils.MYSQL_CLASS);
                Connection con = DriverManager.getConnection(url, user, pass);
                connectionMap.get(dbUniqueName).close();
                connectionMap.put(dbUniqueName,con);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeDatabaseConnection(String dbUniqueName) {
        try {
            connectionMap.get(dbUniqueName).close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connectionMap.remove(dbUniqueName);
    }

    public Connection getConnection(String dbUniqueName){
        return connectionMap.get(dbUniqueName);
    }
}
