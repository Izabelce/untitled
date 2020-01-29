package Database_Connectivity_Module;


import java.sql.ResultSet;

public class Database_Connectivity_Interface {
    private Database_Connector dbCon;
    private Database_Controller myController;

    public Database_Connectivity_Interface(Database_Connector con, Database_Controller dbController) {
        dbCon = con;
        myController = dbController;
    }

    public static int getPuffer() {
        return 50;
    }

    public int heute() {
        return myController.getTodayID();
    }



    public ResultSet getQueryresult(String sql) {
        return dbCon.doQuery(sql);
    }

    public Bestellung getBestellung(int bestellID) {
        return myController.getBestellungOnID(bestellID);
    }



    public String getToday() {
        return myController.getToday();
    }




    public void changeTable(String executeSQL) {
        dbCon.changeTable(executeSQL);
    }

    public void comnmit() {
        myController.commit();
    }

    public void setToday(String heute) {
        myController.setToday(heute);
    }
}
