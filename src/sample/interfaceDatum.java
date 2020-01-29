package sample;

public class interfaceDatum {

    public String getHeute() {
        return heute;
    }

    public void setDatum(String datumNew) {

        heute = datumNew;

    }

    private String heute;

    public interfaceDatum(String datum){
        heute = datum;
    }


}
