package de.adesso.softwarepico.configuration;

import java.util.List;

public class HardwarePicoUtils {

    public static int hpIdFromUri(String uri){
        if(uri.contains("/")) {
            return Integer.parseInt(uri.split("/")[0]);
        }
        return -1;
    }

    public static String hpIpFromUri(String uri){
        if(uri.contains("/")){
            return uri.split("/")[1];
        }
        return "....";
    }

}
