package sample;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyInterface {

     interfaceDatum datum ;

    public MyInterface(interfaceDatum datum){
        this.datum = datum;
    }

    public void setDatum(String datumNeu) {


    }

    public String getDatum() {
        return datum.getHeute();
    }


}
