package com.compression.db;

import com.compression.huffman.utils.FrequencyMap;

import java.io.*;
import java.sql.*;

public class SQLitePlug implements Database {
    private Connection conn;
    public SQLitePlug(String connection) {
        try {
            conn = DriverManager.getConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public SQLitePlug(String connection, String user, String pass) {
        try {
            conn = DriverManager.getConnection(connection, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Object model, Schema type) {
        String sql = "";
        if(type == Schema.TABLE) {

             sql = "CREATE TABLE IF NOT EXISTS " + model.getClass().getSimpleName() + "(\n"
                    + model.getClass().getFields()[0].getName() + " TEXT PRIMARY KEY,\n"
                    + model.getClass().getFields()[1].getName() + " BLOB"
                    + ");";
        }
        else if(type == Schema.DB){
            sql = "CREATE DATABASE IF NOT EXISTS HuffmanDB;";
        }
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object read(String hash) {
        String selectSQL = "SELECT frequencyMapObject FROM FrequencyHolder WHERE id = ?;";
        ResultSet rs = null;
        FileOutputStream fos = null;
        PreparedStatement pstmt = null;
        FrequencyMap frequencyMap = null;
        try {
            pstmt = conn.prepareStatement(selectSQL);
            pstmt.setString(1, hash);
            rs = pstmt.executeQuery();


            InputStream input = rs.getBinaryStream("frequencyMapObject");
            if(input == null) return null;
            ObjectInputStream obj = new ObjectInputStream(input);

            frequencyMap = (FrequencyMap) obj.readObject();
//            System.out.println(frequencyMap.size());

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (fos != null) {
                    fos.close();
                }

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return frequencyMap;
    }

    @Override
    public void insert(Object model) {
        String sql = "INSERT INTO " + model.getClass().getSimpleName() + " VALUES (\n"
                + "	?,\n"
                + " ?"
                + ");";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, (String) model.getClass().getFields()[0].get(model));
            ByteArrayOutputStream out = new ByteArrayOutputStream(10);

            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeObject(model.getClass().getFields()[1].get(model));


            pstmt.setBytes(2, out.toByteArray());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    public void executeQuery(Object model, Operation operation) {
        String sql = "";
        PreparedStatement pstmt;
        try {
            switch (operation) {
                case INSERT:
                    sql = "INSERT INTO " + model.getClass().toString() + " VALUES (\n"
                            + "	?,\n"
                            + " ?"
                            + ");";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, (String) model.getClass().getFields()[0].get(model));

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
