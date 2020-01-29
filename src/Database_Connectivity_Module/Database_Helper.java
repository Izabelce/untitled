package Database_Connectivity_Module;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Database_Helper {


    public static List<String> fullFileToStringList(String filepath){
        String str = "";
        FileReader fr = null;
        StringBuilder contentBuilder = new StringBuilder();
        List<String> list = null;
        try{ list= Files.readAllLines(Paths.get(filepath));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return list;
    }



}
