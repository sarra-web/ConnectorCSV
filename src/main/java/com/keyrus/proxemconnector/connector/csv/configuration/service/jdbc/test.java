package com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {

    public static void main(String[] args) {

        Map<String, List<Map<String, Integer>>> mapParam = new HashMap<>();
        mapParam.put("Identifiant", List.of(new HashMap<String, Integer>(Map.of("id", 2))));
        mapParam.put("title", List.of(new HashMap<String, Integer>(Map.of("title", 1))));
        mapParam.put("body", List.of(new HashMap<String, Integer>(Map.of("body", 4, "5", 6, "7", 8, "8", 9))));
        mapParam.put("meta", List.of(new HashMap<String, Integer>(Map.of("Customer", 7, "ocit_humeurName", 9))));
        // mapParam.put("date", List.of(20, 21, 23));
        mapParam.put("date", List.of(new HashMap<String, Integer>(Map.of("date", 10))));
//Rq on peut au lieu de faire ça q'on cré une class (l'objet qui par la suite recupere les données du frontend
        String jdbcUrl = "jdbc:mysql://localhost:3306/testdb_spring";
        String username = "root";
        String password = "123456";
        String className = "com.mysql.cj.jdbc.Driver";
        String tableName = "tutorials";
        int numCol = getNumCol(className, password, jdbcUrl, username, tableName);
        System.out.println(numCol);
        List<String> listCol = getNameColumns(className, password, jdbcUrl, username, tableName, numCol);
        System.out.println(listCol);
      System.out.println(readJDBC(jdbcUrl, username, password, className, tableName, numCol));
      /*insertVlues(jdbcUrl, username, password, "jj", "ttt1",10);
        insertVlues(jdbcUrl, username, password, "ej", "lle2",11);
        insertVlues(jdbcUrl, username, password, "ejj", "eee3",99);*/
        //JDBCToJSON(mapParam,jdbcUrl,username,password,className,numCol,tableName);
    }

    public static int getNumCol(String className, String password, String jdbcUrl, String username, String tableName) {
        int columnCount = 0;
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();
            columnCount = metaData.getColumnCount();
        } catch (Exception e) {
            System.out.println(e);
        }
        return columnCount;
    }

    public static List<String> getNameColumns(String className, String password, String jdbcUrl, String username, String tableName, int columnCount) {
        List<String> columnNames = new ArrayList<>();
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);


             int i=1;
            while ((resultSet.next())&&(i<=columnCount)) {
                i++;
                String columnName = resultSet.getString("COLUMN_NAME");
                columnNames.add(columnName);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return columnNames;
    }

    public static List<List<String>>  readJDBC(String jdbcUrl, String username, String password, String className, String tableName, int numCol) {
       List<List<String>> l=new ArrayList<List<String>>();
       l.add(getNameColumns( className,  password,  jdbcUrl, username, tableName,numCol));
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while (resultSet.next()) {
                List<String> values = new ArrayList<>();
                for (int i = 1; i <= numCol; i++) {
                    values.add(resultSet.getString(i));
                }
                l.add(values);
              //  System.out.println(values);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    return l;
    }

    public static void JDBCToJSON(Map<String, List<Map<String, Integer>>> mapParam, String jdbcUrl, String username, String password, String className, int numCol, String tableName) {

        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                List<String> values = new ArrayList<>();
                for (int i = 1; i <= numCol; i++) {
                    values.add(resultSet.getString(i));
                }
                System.out.println(values);
            }

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public static void insertVlues(String url, String user, String password, String val1, String val2,int valId) {
        // Configuration de la connexion à la base de données
        // Création de la requête SQL
        String query = "INSERT INTO tutorials (id,description, title) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query)) {

            // Définition des valeurs des paramètres de la requête
            pst.setInt(1, valId);
            pst.setString(2, val1);
            pst.setString(3, val2);

            // Exécution de la requête
            pst.executeUpdate();
            System.out.println("Données insérées avec succès");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
