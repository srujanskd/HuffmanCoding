package com.compression.db;

import com.compression.file.FileRead;
import com.compression.huffman.utils.FrequencyMap;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableOperation {
    private static Connection connect(String url) {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewTable(Connection conn) {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS FrequencyHolder (\n"
                + "	id text PRIMARY KEY,\n"
                + " frequencyMapObject blob"
                + ");";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertRow(Connection conn) {
        String sql = "INSERT INTO FrequencyHolder VALUES (\n"
                + "	?,\n"
                + " ?"
                + ");";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "11");
            FrequencyMap fm = new FrequencyMap();
            fm.increment("Hello");
            ByteArrayOutputStream out = new ByteArrayOutputStream(10);

            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeObject(fm);


            pstmt.setBytes(2, out.toByteArray());
            pstmt.executeUpdate();
            System.out.println("Stored the file in the BLOB column.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void readObj(String url) {
        // update sql
        String selectSQL = "SELECT frequencyMapObject FROM FrequencyHolder WHERE id=?";
        ResultSet rs = null;
        FileOutputStream fos = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connect(url);
            pstmt = conn.prepareStatement(selectSQL);
            pstmt.setString(1, "11");
            rs = pstmt.executeQuery();

            // write binary stream into file

            InputStream input = rs.getBinaryStream("frequencyMapObject");
            ObjectInputStream obj = new ObjectInputStream(input);

            FrequencyMap frequencyMap = (FrequencyMap) obj.readObject();
            System.out.println(frequencyMap.size());

        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
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

                if (conn != null) {
                    conn.close();
                }
                if (fos != null) {
                    fos.close();
                }

            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlite:/home/srujankashyap/Maven_Test/HuffmanCoding/frequency.db";
        Connection conn = connect(url);
        createNewTable(conn);
//        insertRow(conn);
        readObj(url);
    }


    public static void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
