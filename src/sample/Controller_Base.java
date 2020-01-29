package sample;

public abstract class Controller_Base {
    Backend_Interface backendInterface;
    public void setBackendInterface(Backend_Interface backendinterface) {
        this.backendInterface = backendinterface;
    }

    public String getHeute() {
        return heute;
    }

    public void setDatum(String datumNew) {

        heute = datumNew;

    }

    private String heute;

    public Controller_Base(){
        this("01.01.2010");
    }

    public Controller_Base(String datum){
        heute = datum;
    }
}
