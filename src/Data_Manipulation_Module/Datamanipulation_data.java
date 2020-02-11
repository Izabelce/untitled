package Data_Manipulation_Module;

import Database_Connectivity_Module.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Datamanipulation_data {
    private Data_Manipulation_Controller dmc;

    public Datamanipulation_data(Data_Manipulation_Controller dmc) {
        this.dmc = dmc;
    }

    public void writeInCSV() {
        System.out.println("        Werte werden für spätere Verwendung in Text geschrieben...");
        String schichtartbeitstage = "CSVDATA/loadSchichtarbeitstage.txt";
        String kalenderwochen = "CSVDATA/loadKalenderwoche.txt";
        String bestellungen = "CSVDATA/loadBestellungen.txt";
        String lieferungen = "CSVDATA/loadLieferungen.txt";
        String fahrplaene = "CSVDATA/loadFahrplaene.txt";
        String zulieferer = "CSVDATA/loadZulieferer.txt";
        String stammdaten = "CSVDATA/loadStammdaten.txt";


        writeSchichtarbeitstage(schichtartbeitstage);
        writeKalenderwochen(kalenderwochen);
        writeBestellungen(bestellungen);
        writeLieferungen(lieferungen);
        writeFahrplaene(fahrplaene);
        writeZulieferer(zulieferer);
        writeStammdaten(stammdaten);
    }

    private void writeStammdaten(String path) {
        System.out.println("        Stammdaten...");
        BufferedWriter writer = getWriter(path);
        try {
            writer.append(Integer.toString(Stammdatenmanager.getAnteilJanuar()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilFebruar()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilMaerz()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilApril()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilMai()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilJuni()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilJuli()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilAugust()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilSeptember()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilOktober()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilNovember()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilDezember()) + ", ");

            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell1()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell2()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell3()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell4()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell5()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell5()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell6()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell7()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getAnteilModell8()) + ", ");

            writer.append(Integer.toString(Stammdatenmanager.getVorlaufzeitChina()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getVorlaufzeitSpanien()) + ", ");
            writer.append(Integer.toString(Stammdatenmanager.getVorlaufZeitBW())+ ", ");

            writer.append(Integer.toString(Stammdatenmanager.getGesamtProduktion()));

        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

    }

    public void writeZulieferer(String path) {
        System.out.println("        Zulieferer...");
        BufferedWriter writer = getWriter(path);
        //TODO

    }

    public void writeFahrplaene(String path) {
        System.out.println("        Fahrpläne...");
        BufferedWriter writer = getWriter(path);
        for (int i = 0; i < dmc.getAbfahrtstage().length; i++) {
            try {
                writer.append("" + dmc.getAbfahrtstage()[i] + "\n");
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void writeLieferungen(String path) {
        System.out.println("        Lieferungen...");
        BufferedWriter writer = getWriter(path);
        for (int i = 0; i < dmc.getAlleTage().length; i++) {
            Schichtarbeitstag tag = dmc.getAlleTage()[i];
            for (Lieferung lieferung : tag.getLieferungen()) {
                String outputString = "";
                outputString = outputString + lieferung.getKomponenttyp_ID() + ", ";
                outputString = outputString + lieferung.getAnzahl() + ", ";
                outputString = outputString + lieferung.getAnkunftstag_ID() + ", ";
                outputString = outputString + lieferung.getAnkunftstag_datum() + ", ";
                outputString = outputString + lieferung.getStarttag_ID() + ", ";
                outputString = outputString + lieferung.getStarttag_datum() + "\n";
                try {
                    writer.append(outputString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void writeBestellungen(String path) {
        System.out.println("        Bestellungen...");
        BufferedWriter writer = getWriter(path);
        for (int i = 0; i < dmc.getAlleTage().length; i++) {
            Schichtarbeitstag tag = dmc.getAlleTage()[i];
            for (Bestellung bestellung : tag.getBestellungen()) {
                String outputString = "";
                outputString = outputString + bestellung.getBestell_ID() + ", ";
                outputString = outputString + bestellung.getBestellungstag() + ", ";
                outputString = outputString + bestellung.getLiefertag() + ", ";
                outputString = outputString + bestellung.getBestelldatum_ID() + ", ";
                outputString = outputString + bestellung.getLieferdatum_ID() + ", ";
                outputString = outputString + bestellung.getModelltyp_ID() + ", ";
                outputString = outputString + bestellung.getAnzahl() + "\n";
                try {
                    writer.append(outputString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void writeKalenderwochen(String path) {
        System.out.println("        Kalenderwochen...");
        BufferedWriter writer = getWriter(path);
        //erstellen einen writer auf dem outputfile
        //jeden tag der kw auslesen und mit id festschreiben
        for (Kalenderwoche k : dmc.getKwList()) {
            String outputString = "";
            while (k.next()) {
                Schichtarbeitstag tag = k.get();
                outputString = outputString + tag.getArbeitstag_ID();
                if (tag.getArbeitstag_ID() != k.getLast().getArbeitstag_ID()) {
                    outputString = outputString + ", ";
                }
            }
            outputString = outputString + "\n";
            try {
                writer.append(outputString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    private void writeSchichtarbeitstage(String path) {
        System.out.println("        Schichtarbeitstage...");
        BufferedWriter writer = getWriter(path);

        //pro schichtarbeitstag eine zeile
        for (int i = 0; i < dmc.getAlleTage().length; i++) {
            String outputstring = "";
            Schichtarbeitstag tag = dmc.getAlleTage()[i];

            outputstring = outputstring + tag.getSchicht_ID() + ", ";
            outputstring = outputstring + tag.getMax_output() + ", ";
            outputstring = outputstring + tag.getSchicht_ID() + ", ";
            for (int x = 0; x < tag.getFahrradplan().length; x++) {
                outputstring = outputstring + tag.getFahrradplan()[x] + ", ";
            }
            outputstring = outputstring + tag.getKwID() + ", ";
            outputstring = outputstring + tag.getArbeitstag_ID() + ", ";
            if (tag.isHoldayIn(Land.China)) {
                outputstring = outputstring + "t, ";
            } else {
                outputstring = outputstring + "f, ";
            }
            if (tag.isHoldayIn(Land.Spanien)) {
                outputstring = outputstring + "t, ";
            } else {
                outputstring = outputstring + "f, ";
            }
            if (tag.isHoldayIn(Land.Baden_Württemberg)) {
                outputstring = outputstring + "t, ";
            } else {
                outputstring = outputstring + "f, ";
            }
            if (tag.isHoldayIn(Land.Nordrhein_Westfalen)) {
                outputstring = outputstring + "t, ";

            } else {
                outputstring = outputstring + "f, ";
            }
            outputstring = outputstring + tag.getDatum() + ", ";
            for (int l = 0; l < tag.getLager2().length; l++) {
                if (l == 0) {
                    outputstring = outputstring + tag.getLager2()[l];
                } else {
                    outputstring = outputstring + ", " + tag.getLager2()[l];
                }
            }
            outputstring = outputstring + "\n";
            try {
                writer.append(outputstring);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //fertig, writer schliessen
        try {
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void loadFromCSV() {
        System.out.println("        Werte werden aus früheren Zeiten geladen...");
        String schichtartbeitstage = "CSVDATA/loadSchichtarbeitstage.txt";
        String kalenderwochen = "CSVDATA/loadKalenderwoche.txt";
        String bestellungen = "CSVDATA/loadBestellungen.txt";
        String lieferungen = "CSVDATA/loadLieferungen.txt";
        String fahrplaene = "CSVDATA/loadFahrplaene.txt";
        String zulieferer = "CSVDATA/loadZulieferer.txt";
        String stammdaten = "CSVDATA/loadStammdaten.txt";

        loadSchichtarbeitstage(schichtartbeitstage);
        loadKalenderwoche(kalenderwochen);
        loadBestellungen(bestellungen);
        loadLieferungen(lieferungen);
        loadFahrplaene(fahrplaene);
        loadZulieferer(zulieferer);
        loadStammdaten(stammdaten);
        dmc.candidatesherstellen();
    }

    private void loadStammdaten(String stammdaten) {
        List<String> strings = Database_Helper.fullFileToStringList(stammdaten);
        for (String s : strings) {
            String[] results = s.split(", ", 0);
            Stammdatenmanager.setAnteilJanuar(Integer.parseInt(results[0]));
            Stammdatenmanager.setAnteilFebruar(Integer.parseInt(results[1]));
            Stammdatenmanager.setAnteilMaerz(Integer.parseInt(results[2]));
            Stammdatenmanager.setAnteilApril(Integer.parseInt(results[3]));
            Stammdatenmanager.setAnteilMai(Integer.parseInt(results[4]));
            Stammdatenmanager.setAnteilJuni(Integer.parseInt(results[5]));
            Stammdatenmanager.setAnteilJuli(Integer.parseInt(results[6]));
            Stammdatenmanager.setAnteilAugust(Integer.parseInt(results[7]));
            Stammdatenmanager.setAnteilSeptember(Integer.parseInt(results[8]));
            Stammdatenmanager.setAnteilOktober(Integer.parseInt(results[9]));
            Stammdatenmanager.setAnteilNovember(Integer.parseInt(results[10]));
            Stammdatenmanager.setAnteilDezember(Integer.parseInt(results[11]));

            Stammdatenmanager.setAnteilModell1(Integer.parseInt(results[12]));
            Stammdatenmanager.setAnteilModell2(Integer.parseInt(results[13]));
            Stammdatenmanager.setAnteilModell3(Integer.parseInt(results[14]));
            Stammdatenmanager.setAnteilModell4(Integer.parseInt(results[15]));
            Stammdatenmanager.setAnteilModell5(Integer.parseInt(results[16]));
            Stammdatenmanager.setAnteilModell6(Integer.parseInt(results[17]));
            Stammdatenmanager.setAnteilModell7(Integer.parseInt(results[18]));
            Stammdatenmanager.setAnteilModell8(Integer.parseInt(results[19]));

            Stammdatenmanager.setVorlaufzeitChina(Integer.parseInt(results[20]));
            Stammdatenmanager.setVorlaufzeitSpanien(Integer.parseInt(results[21]));
            Stammdatenmanager.setVorlaufZeitBW(Integer.parseInt(results[22]));

            Stammdatenmanager.setGesamtProduktion(Integer.parseInt(results[23]));
        }
    }

    private void loadZulieferer(String zulieferer) {
        List<String> strings = Database_Helper.fullFileToStringList(zulieferer);
        //TODO
    }

    private void loadFahrplaene(String fahrplaene) {
        List<String> strings = Database_Helper.fullFileToStringList(fahrplaene);
        int[] abfahrtstage = new int[strings.size()];
        int index = 0;
        for (String s : strings) {
            abfahrtstage[index] = Integer.parseInt(s);
            index++;
        }
        dmc.setAbfahrtstage(abfahrtstage);
    }

    private void loadLieferungen(String lieferungen) {
        List<String> strings = Database_Helper.fullFileToStringList(lieferungen);
        for (String s : strings) {
            String[] results = s.split(", ", 0);
            int kompID = Integer.parseInt(results[0]);
            int anzahl = Integer.parseInt(results[1]);
            int ankunftstagID = Integer.parseInt(results[2]);
            String ankunftstag = results[3];
            int starttagID = Integer.parseInt(results[4]);
            String starttag = results[5];
            Lieferung l = new Lieferung(kompID, anzahl, ankunftstagID, starttagID, ankunftstag, starttag);
            dmc.getAlleTage()[ankunftstagID].addLieferung(l);
        }
    }

    private void loadBestellungen(String bestellungen) {
        List<String> strings = Database_Helper.fullFileToStringList(bestellungen);
        for (String s : strings) {
            String[] results = s.split(", ", 0);
            int bestellID = Integer.parseInt(results[0]);
            String bestellungstag = results[1];
            String liefertag = results[2];
            int bestelldaumID = Integer.parseInt(results[3]);
            int lieferdatumID = Integer.parseInt(results[4]);
            int modelltypID = Integer.parseInt(results[5]);
            int anzahl = Integer.parseInt(results[6]);
            Bestellung b = new Bestellung(bestelldaumID, lieferdatumID, modelltypID, anzahl, bestellungstag, liefertag);
            dmc.getAlleTage()[lieferdatumID].addBestellung(b);
        }
    }

    private void loadKalenderwoche(String kalenderwochen) {
        List<String> strings = Database_Helper.fullFileToStringList(kalenderwochen);
        Kalenderwoche[] kwList = new Kalenderwoche[dmc.getAlleTage()[dmc.getAlleTage().length-1].getKwID()];
        int index =0;
        for (String s : strings) {
            String[] results = s.split(", ", 0);
            int kwID = dmc.getAlleTage()[Integer.parseInt(results[0])].getKwID();
            Kalenderwoche k = new Kalenderwoche(kwID);
            for (int i = 0; i < results.length; i++) {
                k.add(dmc.getAlleTage()[Integer.parseInt(results[i])]);
            }
           kwList[index] = k;
            index++;
        }
        dmc.setKwList(kwList);
    }

    private void loadSchichtarbeitstage(String schichtartbeitstage) {
        List<String> strings = Database_Helper.fullFileToStringList(schichtartbeitstage);
        Schichtarbeitstag[] alleTage = new Schichtarbeitstag[strings.size()];
        for (String s : strings) {
            String[] results = s.split(", ", 0);
            int schichtID = Integer.parseInt(results[0]);
            int max_output = Integer.parseInt(results[1]);
            int[] fahrradplan = new int[8];
            fahrradplan[0] = Integer.parseInt(results[2]);
            fahrradplan[1] = Integer.parseInt(results[3]);
            fahrradplan[2] = Integer.parseInt(results[4]);
            fahrradplan[3] = Integer.parseInt(results[5]);
            fahrradplan[4] = Integer.parseInt(results[6]);
            fahrradplan[5] = Integer.parseInt(results[7]);
            fahrradplan[6] = Integer.parseInt(results[8]);
            fahrradplan[7] = Integer.parseInt(results[9]);
            fahrradplan[7] = Integer.parseInt(results[10]);
            int kwID = Integer.parseInt(results[11]);
            int tagID = Integer.parseInt(results[12]);
            HashSet<Land> holidays = new HashSet<Land>();
            if (results[13] == "t") {
                holidays.add(Land.China);
            }
            if (results[14] == "t") {
                holidays.add(Land.Spanien);
            }
            if (results[15] == "t") {
                holidays.add(Land.Baden_Württemberg);
            }
            if (results[16] == "t") {
                holidays.add(Land.Nordrhein_Westfalen);
            }
            String datum = results[17];
            int[] lager = new int[22];
            lager[0] = Integer.parseInt(results[18]);
            lager[1] = Integer.parseInt(results[19]);
            lager[2] = Integer.parseInt(results[20]);
            lager[3] = Integer.parseInt(results[21]);
            lager[4] = Integer.parseInt(results[22]);
            lager[5] = Integer.parseInt(results[23]);
            lager[6] = Integer.parseInt(results[24]);
            lager[7] = Integer.parseInt(results[25]);
            lager[8] = Integer.parseInt(results[26]);
            lager[9] = Integer.parseInt(results[27]);
            lager[10] = Integer.parseInt(results[28]);
            lager[11] = Integer.parseInt(results[29]);
            lager[12] = Integer.parseInt(results[30]);
            lager[13] = Integer.parseInt(results[31]);
            lager[14] = Integer.parseInt(results[32]);
            lager[15] = Integer.parseInt(results[33]);
            lager[16] = Integer.parseInt(results[34]);
            lager[17] = Integer.parseInt(results[35]);
            lager[18] = Integer.parseInt(results[36]);
            lager[19] = Integer.parseInt(results[37]);
            lager[20] = Integer.parseInt(results[38]);
            lager[21] = Integer.parseInt(results[39]);

            Schichtarbeitstag tag = new Schichtarbeitstag(schichtID, max_output, fahrradplan, kwID, tagID, holidays, datum);
            for (int i = 1; i <= lager.length; i++) {
                tag.setLagerbestand(i, lager[i - 1]);
            }
            alleTage[tagID] = tag;
        }
        for (int i = alleTage.length - 1; i > 0; i--) {
            alleTage[i].setVortag(alleTage[i - 1]);
        }
        dmc.setAlleTage(alleTage);
    }


    private BufferedWriter getWriter(String path) {
        File file = new File(path);
        file.delete();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path, true));
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return writer;
    }
}
