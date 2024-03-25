package de.adesso.softwarepico.configuration;

import java.util.List;

public class HardwarePicoUtils {

    public static boolean idAlreadyInList(List<String> hardwarePicoUris, String hardwarePicoUri){
        for(String uri : hardwarePicoUris){
            if(uri.split("/")[0].equals(hardwarePicoUri.split("/")[0])){
                return true;
            }
        }
        return false;
    }

}
