package Database_Connectivity_Module;


import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database_Connector {
    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_CONNECTION = "jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521:inf14";
    private static final String id = "ace621";
    private static final String pw = "Kuwe-3001";
    private Connection con;
    private Database_Controller controller;
    private Database_Helper helper;

    public Database_Connector() {
        if (!connectToDatabase()) {
            System.err.println("COULD NOT CONNECT TO DATABASE!");
        }
        controller = null;
        helper = null;
    }


    public void populate_Tablespace() {
        System.out.println("    Removing old tables...");
        deleteEverything();
        System.out.println("    Creating all tables...");
        createAllTables();
        System.out.println("    Writing data...");
        populateAllTables();
        System.out.println("    populating finished!");
    }




    private void populateAllTables() {
        String path = "CSVDATA/Insert_all_data.txt";
        List<String> strings = helper.fullFileToStringList(path);
        String sqlString = "";
        for (String str : strings) {
            sqlString = sqlString + "\n" + str;
            if (str.contains(";")) {
                try(  Statement stmt = con.createStatement()) {
                    sqlString = sqlString.replace(';', ' ');
                    stmt.executeUpdate(sqlString);
                } catch (SQLException sqle) {
                    System.err.println("Exception in populateAllTables: " + sqle.getMessage() + "\n query:" + sqlString);
                }
                sqlString = "";
            }
        }
    }

    private void populateProduktionsvolumen() {
        int anzahlTage = 0;
        String sqlAnzahlTage = "SELECT COUNT (Tag_ID) FROM Kalendertag";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlAnzahlTage);
            rs.next();
            anzahlTage = rs.getInt(1);
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Exception in populateProduktionsvolumen: " + ex.getMessage() + "\n query:" + sqlAnzahlTage);
        }
        String sqlBestellungtage = "SELECT Modelltyp_ID, Anzahl FROM bestellt WHERE ";
        for (int i = 1; i <= anzahlTage; i++) ;
    }

    private void createAllTables() {
        String path = "CSVDATA/table_definitons.txt";
        List<String> strings = helper.fullFileToStringList(path);
        String sqlString = "";
        for (String str : strings) {
            sqlString = sqlString + "\n" + str;
            if (str.contains(";")) {
                try {
                    sqlString = sqlString.replace(';', ' ');
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sqlString);
                    stmt.close();
                } catch (SQLException sqle) {
                    System.err.println("Exception in createAllTables: " + sqle.getMessage() + "\n query:" + sqlString);
                }
                sqlString = "";
            }
        }
    }


    public void assignController(Database_Controller ctrl) {
        this.controller = ctrl;
    }


    public void assignHelper(Database_Helper hlp) {
        this.helper = hlp;
    }


    public void deleteEverything() {
        String path = "CSVDATA/drop_all_tables.txt";
        List<String> strings = helper.fullFileToStringList(path);
        for (String str : strings) {
            try {
                Statement stmt = con.createStatement();
                stmt.executeUpdate(str);
                stmt.close();
            } catch (SQLException sqle) {
                System.err.println("Exception in delete everything: " + sqle.getMessage() + "\n query:" + str);
            }
        }
        // try {
        //   Statement stmt = con.createStatement();
        //   stmt.execute(sql);
        //   stmt.close();
        // } catch (SQLException sqle) {
        //     System.err.println("Exception in delete everything: " + sqle.getMessage() +"\n query:" + sql);
        // }
    }

    public int getKalendertagID(String tag) {
        String sql = "Select Tag_ID FROM Kalendertag WHERE Datum = '" + tag + "'";
        //System.out.println(sql);
        int returnvalue = -1;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // System.out.println(rs.getFetchSize());

            rs.next();
            returnvalue = rs.getInt(1);
            stmt.close();
        } catch (SQLException sqle) {
            System.err.println("Exception in getKalendertagID: " + sqle.getMessage() + "\n query:" + sql);
        }
        return returnvalue;
    }


    public void emptyAllTables() {
        String sql = "SELECT table_name FROM user_tables ORDER BY table_name";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            String tablename = "";
            while (rs.next()) {
                tablename = rs.getString(1);
                emptyTable(tablename);
            }
            stmt.close();
        } catch (SQLException sqle) {
            System.err.println("Exception in empty all tables: " + sqle.getMessage() + "\n query:" + sql);
        }
    }


    public void dropTable(String tablename) {
        if (tableExists(tablename)) {
            String sql = "DROP TABLE " + tablename + " CASCADE CONSTRAINTS";
            try {
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            } catch (SQLException sqle) {
                System.err.println("Exception in Drop Table: " + sqle.getMessage() + "\n query:" + sql);
            }
        }
    }


    public void emptyTable(String tablename) {
        if (tableExists(tablename)) {
            String sql = "DELETE FROM " + tablename;
            String sql_check_if_empty = "Select * From " + tablename + "where ROWID =1";
            System.out.println("Emptying Table " + tablename + " ...");
            try {
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            } catch (SQLException sqle) {
                System.err.println("Exception in empty Table: " + sqle.getMessage() + "\n query:" + sql);
            }
        }
    }


    public boolean tableExists(String tablename) {
        String sql = "SELECT table_name FROM  user_tables ORDER BY table_name";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                String tableinfo = rs.getString(1);
                if (tablename.equals(tableinfo)) {
                    stmt.close();
                    return true;
                }
            }
            stmt.close();
        } catch (SQLException sqle) {
            System.err.println("Exception in tableExists: " + sqle.getMessage() + "\n query:" + sql);
        }
        return false;
    }


    public boolean deleteEntry(String tablename, int target_ID) {
        if (!tableExists(tablename)) return false;
        //TODO
        return false;
    }


    public boolean writeEntry(String tablename, int target_ID) {
        if (!tableExists(tablename)) return false;
        switch (tablename) {
            case "Land":
                return true;
            case "Bestellung":
                return true;
            case "Transportmittel":
                return true;
            case "Lagerbestand":
                return true;
            case "bestellt":
                return true;

            default:
                return false;
        }
    }


    public boolean resetTable(String target_tablename) {
        if (!tableExists(target_tablename)) return false;
        return false;
    }

    public void printConnection() {
        String sql = "SELECT table_name FROM User_Tables";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
            stmt.close();
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }


    private boolean connectToDatabase() {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException var4) {
            System.out.println("ERROR:" + var4.getMessage());
            return false;
        }
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521:inf14", id, pw);
        } catch (SQLException var3) {
            System.out.println(var3.getMessage());
            return false;
        }
        return true;
    }

    public Bestellung getBestellungOnID(int bestell_ID, int bestelltag_ID, int liefertag_ID) {
        return new Bestellung(0, 0, 0, 0, "", "");
    }


    public ResultSet doQuery(String sql) {
        ResultSet rs = null;
        try{
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            System.err.println("Exception in doQuery: " + sqle.getMessage() + "\n query:" + sql);
        }
        return rs;
    }

    public int goBackWorkdays(int tagID, int amount, String country) {
        int returnID = tagID;
        int counter = 0;
        String sql = "SELECT COUNT(*) FROM ist_feiertag_in WHERE Landname = '" + country + "' AND tag_ID = ";
        while (counter != amount) {
            returnID = returnID--;
            try(Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql + returnID);
                rs.next();
                if (rs.getInt(0) == 0) {
                    counter = counter++;
                }
            } catch (SQLException ex) {
                System.err.println("Exception in goBackWorkdays: " + ex.getMessage() + "\n query:" + sql);
            }
        }
        return returnID;
    }

    public int goBackCalendarDays(int tagID, int amount) {
        return tagID - amount;
    }

    public void changeTable(String sqlChangeTableRequest) {
        try(Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sqlChangeTableRequest);
        } catch (SQLException sqle) {
            System.err.println("Exception in changeTable: " + sqle.getMessage() + "\n query:" + sqlChangeTableRequest);
        }
    }

    public void commit() {
        try {
            con.commit();
        } catch (
                SQLException err)

        {
            System.out.println("ERROR IN commit:" + err.getMessage());
        }
    }

    public void fillStammdatenmanager() {



    }
}
