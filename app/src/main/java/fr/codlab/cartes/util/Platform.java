package fr.codlab.cartes.util;

import fr.codlab.cartes.BuildConfig;

/**
 * Created by kevinleperf on 24/03/2014.
 */
public class Platform {
    public static int ANDROID  = 0;
    public static int BLACKBERRY = 1;
    public static int KINDLE = 2;
    /**
     * @return platform id
     * 1 Android
     * 2 Amazon
     * 3 Blackberry
     */
    public static int getPlatform(){
        if(android.os.Build.BRAND.toLowerCase().contains("blackberry")){
            return 1;
        }else if(android.os.Build.MODEL.toLowerCase().contains("kindle")){
            return 2;
        }else{
            return 0;
        }
    }

    public static boolean isBlackberry(){
        return getPlatform() == BLACKBERRY || BuildConfig.PLATFORM.equals("BB");
    }
}
