package Database_Connectivity_Module;


import Data_Manipulation_Module.Land;

import java.sql.ResultSet;

public class Database_Connectivity_Interface {
    private Database_Connector dbCon;
    private Database_Controller myController;

    public Database_Connectivity_Interface(Database_Connector con, Database_Controller dbController) {
        dbCon = con;
        myController = dbController;
    }

    public static int getPuffer(Land l){
       return 0;
    }

    public int heute() {
        return myController.getTodayID();
    }



    public ResultSet getQueryresult(String sql) {
        return dbCon.doQuery(sql);
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
