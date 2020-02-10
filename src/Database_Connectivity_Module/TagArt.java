package Database_Connectivity_Module;

import Data_Manipulation_Module.Land;

public class TagArt {
    private final TagArtTyp myType;
    private final Land land;
    private int anzahlTage;

    public TagArt(TagArtTyp myType, Land land, int anzahlTage){
        this.myType = myType;
        this.land = land;
        this.anzahlTage = anzahlTage;
    }

    public boolean reduceDay(){
        if(anzahlTage ==0) return false;
        anzahlTage--;
        return true;
    }

    public boolean isFinished(){
        return anzahlTage==0;
    }

    public TagArtTyp gettype(){
        return  myType;
    }

    public int getAnzahlTage(){
        return anzahlTage;
    }


}
