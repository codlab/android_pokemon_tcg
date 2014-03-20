package fr.codlab.cartes.util;

import java.io.IOException;

public class FileMover {
	public static void Move(String parent_dir, String extension){
        String mkdir_card_images = "/system/bin/mkdir "+parent_dir+"/card_images";
		String mkdir = "/system/bin/mkdir "+parent_dir+"/card_images/"+extension;
		String touch = "/system/bin/touch "+parent_dir+"/card_images/"+extension+"/.nomedia";
		String move = "/system/bin/mv "+parent_dir+"/card_images/*"+extension+"* "+parent_dir+"/card_images/"+extension+"/";
		try {
			Runtime.getRuntime().exec(mkdir_card_images+" ; "+mkdir+" ; "+touch+" ; "+move);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
