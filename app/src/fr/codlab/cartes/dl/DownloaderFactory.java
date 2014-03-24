package fr.codlab.cartes.dl;

import java.util.Random;

import android.app.Activity;

/**
 * Create the different Downloaders
 * 
 * @author kevin le perf
 *
 */
final public class DownloaderFactory {
	private static Random _rand = new Random();
	
	public static Downloader downloadFR(Activity parent, String intitule){
		return new Downloader(parent, intitule, "http://www.pkmndb.net/images/"+intitule+".zip");
	}
	
	public static Downloader downloadUS(Activity parent, String intitule){
		return new Downloader(parent, intitule, "http://www.pkmndb.net/images/"+intitule+"_us.zip");
	}
}
