package Data_Manipulation_Module;

import Database_Connectivity_Module.Bestellung;
import Database_Connectivity_Module.Kalenderwoche;
import Database_Connectivity_Module.Lieferung;
import Database_Connectivity_Module.Schichtarbeitstag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class QueryAssembler {
    private Data_Manipulation_Controller myController;
    private int bestellID;
    public QueryAssembler() {
        bestellID = 1;
    }


    public void enlistController(Data_Manipulation_Controller con) {
        myController = con;
    }


    /**
     * returns the dayID of a given date
     *
     * @param day day as String
     * @return the ID of the Day according to the database. Fraudulent Strings return -1
     */
    public int getIDDay(String day) {

        return 0;
    }


    public int queryPartStock(int partID, int tagID) {

        String sql = "SELECT Anzahl FROM Lagerbestand WHERE Tag_ID = " + tagID + " AND Lager_ID = 1 AND Lagerobjekt_ID =" + partID;

        int stock = -1;
        try (ResultSet rs = myController.getQueryResult(sql)) {
            rs.next();
            stock = rs.getInt(1);
        } catch (SQLException ex) {
            System.err.println("Exception in queryPartStock : " + sql + "\n" + ex.getMessage());
        }
        return stock;
    }

    public boolean isHoliday(int tagID, String land) {
        String sql = "SELECT COUNT(*) FROM ist_feiertag_in WHERE Landname = '" + land + "' AND tag_ID = " + tagID;

        try (ResultSet rs = myController.getQueryResult(sql)) {
            rs.next();
            int result = rs.getInt(1);
            rs.close();
            return result == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getKomponentID(int fahrradID, Komponenttyp kompname) {
        String sql =
                "SELECT fahrradkomponente.komponent_id FROM besteht_aus JOIN fahrradkomponente ON fahrradkomponente.komponent_id = besteht_aus.komponent_id" +
                        " WHERE fahrradkomponente.art = '" + kompname.toString() + "' And Modelltyp_ID = " + fahrradID;

        int komponentID = -1;
        try (ResultSet rs = myController.getQueryResult(sql)) {
            rs.next();
            komponentID = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return komponentID;
    }

    public int getZielvorgabe(int monat, int fahrrad) {
        String sqlGetMonatsErwartung = String.format("SELECT Anzahl FROM Zielvorgabe WHERE Zielvorgaben_Monats_ID = %d AND Modelltyp_ID = %d", monat, fahrrad);
        int returnvalue = -1;
        try (ResultSet rs = myController.getQueryResult(sqlGetMonatsErwartung)) {
            rs.next();
            returnvalue = rs.getInt(1);
        } catch (SQLException sqle) {
            System.err.println("Exception in getZielvorgabe : " + sqlGetMonatsErwartung + "\n" + sqle.getMessage());
        }
        return returnvalue;
    }

    public Integer[] getVorgabeMonatTage(int monat) {
        Integer[] returnvalue = new Integer[2];
        String sqlGetStart = String.format("SELECT ersterTag_ID, LetzterTag_ID FROM Zielvorgaben_Monat WHERE Zielvorgaben_Monats_ID = %d", monat);
        try (ResultSet rs = myController.getQueryResult(sqlGetStart)) {
            rs.next();
            returnvalue[0] = rs.getInt(1);
            returnvalue[1] = rs.getInt(2);
        } catch (SQLException sqle) {
            System.err.println("Exception in getVorgabeMonatTage : " + sqlGetStart + "\n" + sqle.getMessage());
        }
        return returnvalue;
    }

    public void produktionsvolumenAnlegen(Schichtarbeitstag[] schichtarbeitstage) {
        List<String> queries = new LinkedList<String>();
        for (int i = 0; i < schichtarbeitstage.length; i++) {
            for (int j = 0; j < 8; j++) {
                queries.add(String.format("UPDATE Produktionsvolumen SET Anzahl = %d WHERE Tag_ID = %d AND Fahrrad_ID = %d\n", schichtarbeitstage[i].getVerteilung()[j], schichtarbeitstage[i].getArbeitstag_ID(), j + 1));
            }
        }
        for (String query : queries) {
            myController.changeTable(query);
        }
    }

    public Integer[] getAllArbeitstageInRange(int ersterTag, int letzterTag, Land l) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        String sql = "SELECT Tag_ID FROM Kalendertag where Tag_ID not in(" +
                "SELECT Kalendertag.Tag_ID FROM Kalendertag" +
                " JOIN ist_feiertag_in On(Kalendertag.tag_ID = ist_feiertag_in.tag_id)" +
                " WHERE ist_feiertag_in.landname = '" + l.toString().replace("_", "-") +
                "') AND tag_ID >= " + ersterTag + " AND tag_ID <= " + letzterTag;
        try (ResultSet rs = myController.getQueryResult(sql)) {
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
            rs.close();
        } catch (SQLException sqle) {
            System.err.println("Exception in getAllArbeitstageInRange : " + sql + "\n" + sqle.getMessage());
        }
        return list.toArray(new Integer[list.size()]);
    }

    public int sumProdVolumeMont(int start, int ende, int fahrrad) {
        String sql = String.format("SELECT SUM(Anzahl) FROM Produktionsvolumen WHERE tag_ID <= %d AND tag_ID >= %d AND Fahrrad_ID = %d", ende, start, fahrrad);

        int returnvalue = -1;
        try (ResultSet rs = myController.getQueryResult(sql)) {
            rs.next();
            returnvalue = rs.getInt(1);
        } catch (SQLException sqle) {
            System.err.println("Exception in sumProdVolumeMont : " + sql + "\n" + sqle.getMessage());
        }
        return returnvalue;
    }

    public int lastWorkingDayWeek(int kwID, Land ort) {
        int returnvalue = -1;
        String sql = String.format("SELECT tag_ID FROM(\n" +
                "SELECT tag_id FROM Kalendertag where Tag_ID not in(\n" +
                "SELECT Kalendertag.Tag_ID FROM\n" +
                "kalendertag LEFT JOIN ist_feiertag_in\n" +
                "On(Kalendertag.Tag_ID = ist_feiertag_in.tag_id)\n" +
                "WHERE ist_feiertag_in.landname = '%s' AND kalendertag.kw_id= %d \n" +
                ")\n" +
                "AND KW_ID = %d\n" +
                "ORDER BY tag_ID DESC\n" +
                ") WHERE ROWNUM = 1", ort.toString().replace("_", "-"), kwID, kwID);

        try (ResultSet rs = myController.getQueryResult(sql)) {
            rs.next();
            returnvalue = rs.getInt(1);
        } catch (SQLException sqle) {
            System.err.println("Exception in lastWorkingDayWeek : " + sql + "\n" + sqle.getMessage());
        }
        return returnvalue;
    }

    public Schichtarbeitstag getSchicht(int arbeitstag_ID) {

        //for (int arbeitstag_ID : arbeitstage) {
        //query holt daten aus einer anzahl von tables
        String sql = String.format("SELECT alleInfos.kw_id, alleInfos.tag_ID, alleInfos.MAX_OUTPUT, alleInfos.Schicht_ID, alleInfos.FAHRRAD_ID, alleInfos.ANZAHL, ist_feiertag_in.LANDNAME NULLABLE, alleInfos.Datum FROM ist_feiertag_in RIGHT OUTER JOIN\n" +
                "(SELECT kalendertag.Datum, kalendertag.kw_id, auflistung.tag_ID, auflistung.MAX_OUTPUT, auflistung.SCHICHT_ID, auflistung.FAHRRAD_ID, auflistung.ANZAHL FROM \n" +
                "Kalendertag join (SELECT tagesschicht.tag_ID, MAX_OUTPUT, SCHICHT_ID, FAHRRAD_ID, ANZAHL FROM( SELECT tag_ID, Max_Output,  schichtarbeit.Schicht_ID FROM Schichtarbeit JOIN \n" +
                "nutzt on (schichtarbeit.schicht_ID = nutzt.schicht_ID)) tagesschicht JOIN (SELECT  * FROM Produktionsvolumen WHERE Tag_ID = %s)tagesverteilung On(tagesverteilung.tag_ID = tagesschicht.tag_ID))auflistung \n" +
                "ON (kalendertag.tag_ID = auflistung.tag_ID) ORDER BY auflistung.Fahrrad_ID ASC)alleInfos  on (alleInfos.tag_ID = ist_feiertag_in.tag_ID)", arbeitstag_ID);
System.out.println(sql);
//default werte setzen
        int schicht_ID = 0;
        int max_output = 0;
        int kw_ID = 0;
        int[] fahrradanzahl = new int[8];
        String datum = "FEHLER IN QUERYASSEMLBER, GETSCHICHT";
        HashSet<Land> holidays = new HashSet<Land>();
        try {
            ResultSet rs = myController.getQueryResult(sql);
            rs.next();

            schicht_ID = rs.getInt(4);
            max_output = rs.getInt(3);
            kw_ID = rs.getInt(1);
            holidays = new HashSet<Land>();
            do {
                fahrradanzahl[rs.getInt(5) - 1] = rs.getInt(6);
                String holiday = rs.getString(7);
                datum = rs.getString(8);
                if (holiday != null) {
                    if (holiday == Land.Nordrhein_Westfalen.toString().replace("_", "-")) {
                        schicht_ID = 0;
                        max_output = 0;
                        fahrradanzahl[rs.getInt(5) - 1] = 0;
                    }
                    holidays.add(Land.valueOf(holiday.replace("-", "_")));
                }
            } while (rs.next());
            rs.close();
        } catch (SQLException sqle) {
            System.err.println("Exception in getSchicht- tag : " + sql + "\n" + sqle.getMessage());
        }
        Schichtarbeitstag tag = new Schichtarbeitstag(schicht_ID, max_output, fahrradanzahl, kw_ID, arbeitstag_ID, holidays, datum);

        String sql2 = String.format("Select Lagerobjekt_ID, Anzahl From Lagerbestand Where tag_ID = %d ORDER BY Lagerobjekt_ID ASC", arbeitstag_ID);
        try (ResultSet lagerRS = myController.getQueryResult(sql2)) {
            while(lagerRS.next()){
                tag.setLagerbestand(lagerRS.getInt(1), lagerRS.getInt(2));
            }

        }catch (SQLException sqleLager){
            System.err.println("Exception in getSchicht-Lager : " + sql + "\n" + sqleLager.getMessage());
        }

        return tag;
    }

    public Integer[] holidays(Land land) {
        String sql = String.format("SELECT TAG_ID FROM ist_Feiertag_In WHERE landname = '%s'", land.toString().replace("_", "-"));
        ArrayList<Integer> holidays = new ArrayList<Integer>();

        try (ResultSet rs = myController.getQueryResult(sql)) {
            while (rs.next()) {
                holidays.add(rs.getInt(1));
            }
        } catch (SQLException sqle) {
            System.err.println("Exception in holidays : " + sql + "\n" + sqle.getMessage());
        }
        return holidays.toArray(new Integer[holidays.size()]);
    }

    public void setSchicht(int tag_ID, int schicht) {
        String sql = String.format("UPDATE nutzt SET Schicht_ID = %d WHERE Tag_ID =%d", schicht, tag_ID);
        myController.changeTable(sql);
    }



    public int getKW_IDFromDay(int tag_Id) {
        String sql = String.format("SELECT KW_ID FROM KALENDERTAG WHERE TAG_ID = %s", tag_Id);
        int returnvalue = -1;
        try (ResultSet rs = myController.getQueryResult(sql)) {
            rs.next();
            returnvalue = rs.getInt(1);
        } catch (SQLException sqle) {
            System.err.println("Exception in get KW_IDFromDay : " + sql + "\n" + sqle.getMessage());
        }
        return returnvalue;
    }

    public void setVorgabetag(Kalenderwoche kalenderwoche) {
        List<String> queries = new LinkedList<String>();
        while (kalenderwoche.next()) {
            Schichtarbeitstag s = kalenderwoche.get();
            for (int i = 0; i < s.getVerteilung().length; i++) {
                queries.add(String.format("UPDATE Produktionsvolumen Set Anzahl = %d WHERE tag_ID = %d AND fahrrad_ID = %d", s.getVerteilung()[i], s.getArbeitstag_ID(), i + 1));
            }
        }
        kalenderwoche.reset();

        String query = "";
        for (String querypart : queries) {
            query = query + querypart;
            myController.changeTable(querypart);
        }

    }

    public void setSchichten(Kalenderwoche kalenderwoche) {
        List<String> queries = new LinkedList<String>();
        while (kalenderwoche.next()) {
            Schichtarbeitstag s = kalenderwoche.get();
            queries.add(String.format("UPDATE nutzt Set Schicht_ID = %d WHERE tag_ID = %d", s.getSchicht_ID(), s.getArbeitstag_ID()));
        }
        kalenderwoche.reset();
        for (String s : queries) {

            myController.changeTable(s);

        }

    }


    public void setLagerbestand(Schichtarbeitstag schichtarbeitstag) {
        List<String> queries = new LinkedList<String>();
        int arbeitstagID = schichtarbeitstag.getArbeitstag_ID();
        for (int i = 1; i <=22; i++) {
            queries.add(String.format("UPDATE Lagerbestand SET ANZAHL = %d WHERE Tag_ID = %d AND Lagerobjekt_ID = %d and Lager_ID =1", schichtarbeitstag.getLagerbestand(i), arbeitstagID, i));
        }
        for (String query : queries) {
            myController.changeTable(query);
        }
    }

    public void lieferungfestschrieben(Lieferung l) {
        //Zulieferer_ID Komponent_ID Anzahl  Ankunftstag_ID Tg_der_Lieferanfrage
        int zulieferer = Komponentenzuordnung.getZulieferer(l.getKomponenttyp_ID());
        int komponente = l.getKomponenttyp_ID();
        int anzahl = l.getAnzahl();
        int ankunftstag = l.getAnkunftstag_ID();
        int tag_lieferanfrage = l.getStarttag_ID();
        String sqlString = String.format("INSERT INTO Lieferung VALUES(%d, %d, %d, %d, %d)", zulieferer, komponente, anzahl, ankunftstag, tag_lieferanfrage);
        myController.changeTable(sqlString);
    }

    public void bestellungenfestschreiben(Bestellung b){
        String sqlString = String.format("INSERT INTO BESTELLUNG VALUES(%d, %d, %d)",bestellID, b.getBestelldatum_ID(), b.getLieferdatum_ID());
        myController.changeTable(sqlString);
        sqlString = String.format("INSERT INTO BESTELLT VALUES(%d, %d, %d)",bestellID, b.getModelltyp_ID(), b.getAnzahl());
        myController.changeTable(sqlString);
        this.bestellID++;
    }
}
