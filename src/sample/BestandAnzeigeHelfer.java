package sample;

import javafx.beans.property.SimpleStringProperty;

public class BestandAnzeigeHelfer {


    private final SimpleStringProperty tag;
    private final SimpleStringProperty Allrounder;
    private final SimpleStringProperty Competition;
    private final SimpleStringProperty Downhill;
    private final SimpleStringProperty Extreme;
    private final SimpleStringProperty Freeride;
    private final SimpleStringProperty Marathon;
    private final SimpleStringProperty Performance;
    private final SimpleStringProperty Trail;
    private final SimpleStringProperty Tundra;
    private final SimpleStringProperty Raceline;
    private final SimpleStringProperty Spark;
    private final SimpleStringProperty Speedline;
    private final SimpleStringProperty a7005DB;
    private final SimpleStringProperty a7005TB;
    private final SimpleStringProperty Monocoque;
    private final SimpleStringProperty F100;
    private final SimpleStringProperty F80;
    private final SimpleStringProperty Talas;
    private final SimpleStringProperty Reba;
    private final SimpleStringProperty f351;
    private final SimpleStringProperty SL;
    private final SimpleStringProperty Raidon;

    public BestandAnzeigeHelfer(String tag, String Allrounder, String Competition, String Downhill, String Extreme,
                                String Freeride, String Marathon, String Performance, String Trail, String Tundra,
                                String Raceline, String Spark, String Speedline, String a7005DB, String a7005TB,
                                String Monocoque, String F100, String F80, String Talas, String Reba, String f351,
                                String SL, String Raidon) {
        this.tag = new SimpleStringProperty(tag);
        this.Allrounder = new SimpleStringProperty(Allrounder);
        this.Competition = new SimpleStringProperty(Competition);
        this.Downhill = new SimpleStringProperty(Downhill);
        this.Extreme = new SimpleStringProperty(Extreme);
        this.Freeride = new SimpleStringProperty(Freeride);
        this.Marathon = new SimpleStringProperty(Marathon);
        this.Performance = new SimpleStringProperty(Performance);
        this.Trail = new SimpleStringProperty(Trail);
        this.Tundra = new SimpleStringProperty(Tundra);
        this.Raceline = new SimpleStringProperty(Raceline);
        this.Spark = new SimpleStringProperty(Spark);
        this.Speedline = new SimpleStringProperty(Speedline);
        this.a7005DB = new SimpleStringProperty(a7005DB);
        this.a7005TB = new SimpleStringProperty(a7005TB);
        this.Monocoque = new SimpleStringProperty(Monocoque);
        this.F100 = new SimpleStringProperty(F100);
        this.F80 = new SimpleStringProperty(F80);
        this.Talas = new SimpleStringProperty(Talas);
        this.Reba = new SimpleStringProperty(Reba);
        this.f351 = new SimpleStringProperty(f351);
        this.SL = new SimpleStringProperty(SL);
        this.Raidon = new SimpleStringProperty(Raidon);
    }


    public String getTag() {
        return tag.get();
    }

    public SimpleStringProperty tagProperty() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public String getAllrounder() {
        return Allrounder.get();
    }

    public SimpleStringProperty allrounderProperty() {
        return Allrounder;
    }

    public void setAllrounder(String allrounder) {
        this.Allrounder.set(allrounder);
    }

    public String getCompetition() {
        return Competition.get();
    }

    public SimpleStringProperty competitionProperty() {
        return Competition;
    }

    public void setCompetition(String competition) {
        this.Competition.set(competition);
    }

    public String getDownhill() {
        return Downhill.get();
    }

    public SimpleStringProperty downhillProperty() {
        return Downhill;
    }

    public void setDownhill(String downhill) {
        this.Downhill.set(downhill);
    }

    public String getExtreme() {
        return Extreme.get();
    }

    public SimpleStringProperty extremeProperty() {
        return Extreme;
    }

    public void setExtreme(String extreme) {
        this.Extreme.set(extreme);
    }

    public String getFreeride() {
        return Freeride.get();
    }

    public SimpleStringProperty freerideProperty() {
        return Freeride;
    }

    public void setFreeride(String freeride) {
        this.Freeride.set(freeride);
    }

    public String getMarathon() {
        return Marathon.get();
    }

    public SimpleStringProperty marathonProperty() {
        return Marathon;
    }

    public void setMarathon(String marathon) {
        this.Marathon.set(marathon);
    }

    public String getPerformance() {
        return Performance.get();
    }

    public SimpleStringProperty performanceProperty() {
        return Performance;
    }

    public void setPerformance(String performance) {
        this.Performance.set(performance);
    }

    public String getTrail() {
        return Trail.get();
    }

    public SimpleStringProperty trailProperty() {
        return Trail;
    }

    public void setTrail(String trail) {
        this.Trail.set(trail);
    }

    public String getTundra() {
        return Tundra.get();
    }

    public SimpleStringProperty tundraProperty() {
        return Tundra;
    }

    public void setTundra(String tundra) {
        this.Tundra.set(tundra);
    }

    public String getRaceline() {
        return Raceline.get();
    }

    public SimpleStringProperty racelineProperty() {
        return Raceline;
    }

    public void setRaceline(String raceline) {
        this.Raceline.set(raceline);
    }

    public String getSpark() {
        return Spark.get();
    }

    public SimpleStringProperty sparkProperty() {
        return Spark;
    }

    public void setSpark(String spark) {
        this.Spark.set(spark);
    }

    public String getSpeedline() {
        return Speedline.get();
    }

    public SimpleStringProperty speedlineProperty() {
        return Speedline;
    }

    public void setSpeedline(String speedline) {
        this.Speedline.set(speedline);
    }

    public String getA7005DB() {
        return a7005DB.get();
    }

    public SimpleStringProperty a7005DBProperty() {
        return a7005DB;
    }

    public void setA7005DB(String a7005DB) {
        this.a7005DB.set(a7005DB);
    }

    public String getA7005TB() {
        return a7005TB.get();
    }

    public SimpleStringProperty a7005TBProperty() {
        return a7005TB;
    }

    public void setA7005TB(String a7005TB) {
        this.a7005TB.set(a7005TB);
    }

    public String getMonocoque() {
        return Monocoque.get();
    }

    public SimpleStringProperty monocoqueProperty() {
        return Monocoque;
    }

    public void setMonocoque(String monocoque) {
        this.Monocoque.set(monocoque);
    }

    public String getF100() {
        return F100.get();
    }

    public SimpleStringProperty f100Property() {
        return F100;
    }

    public void setF100(String f100) {
        this.F100.set(f100);
    }

    public String getF80() {
        return F80.get();
    }

    public SimpleStringProperty f80Property() {
        return F80;
    }

    public void setF80(String f80) {
        this.F80.set(f80);
    }

    public String getTalas() {
        return Talas.get();
    }

    public SimpleStringProperty talasProperty() {
        return Talas;
    }

    public void setTalas(String talas) {
        this.Talas.set(talas);
    }

    public String getReba() {
        return Reba.get();
    }

    public SimpleStringProperty rebaProperty() {
        return Reba;
    }

    public void setReba(String reba) {
        this.Reba.set(reba);
    }

    public String getF351() {
        return f351.get();
    }

    public SimpleStringProperty f351Property() {
        return f351;
    }

    public void setF351(String f351) {
        this.f351.set(f351);
    }

    public String getSL() {
        return SL.get();
    }

    public SimpleStringProperty SLProperty() {
        return SL;
    }

    public void setSL(String SL) {
        this.SL.set(SL);
    }

    public String getRaidon() {
        return Raidon.get();
    }

    public SimpleStringProperty raidonProperty() {
        return Raidon;
    }

    public void setRaidon(String raidon) {
        this.Raidon.set(raidon);
    }
}
