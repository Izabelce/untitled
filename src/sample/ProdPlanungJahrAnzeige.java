package sample;

import javafx.beans.property.SimpleStringProperty;

public class ProdPlanungJahrAnzeige {
    private SimpleStringProperty tag;

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

    private SimpleStringProperty Allrounder;
    private SimpleStringProperty Competition;
    private SimpleStringProperty Downhill;
    private SimpleStringProperty Extreme;
    private SimpleStringProperty Freeride;
    private SimpleStringProperty Marathon;
    private SimpleStringProperty Performance;
    private SimpleStringProperty Trail;


    public ProdPlanungJahrAnzeige(String tag, String Allrounder, String Competition, String Downhill, String Extreme,
                                  String Freeride, String Marathon, String Performance, String Trail) {

        this.tag = new SimpleStringProperty(tag);
        this.Allrounder = new SimpleStringProperty(Allrounder);
        this.Competition = new SimpleStringProperty(Competition);
        this.Downhill = new SimpleStringProperty(Downhill);
        this.Extreme = new SimpleStringProperty(Extreme);
        this.Freeride = new SimpleStringProperty(Freeride);
        this.Marathon = new SimpleStringProperty(Marathon);
        this.Performance = new SimpleStringProperty(Performance);
        this.Trail = new SimpleStringProperty(Trail);

    }



}
