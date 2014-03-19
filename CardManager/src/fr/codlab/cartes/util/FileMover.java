package fr.codlab.cartes.util;

import java.io.IOException;

public class FileMover {
	public static void Move(String parent_dir, String extension){
		String mkdir = "/system/bin/mkdir "+parent_dir+"/"+extension;
		String touch = "/system/bin/touch "+parent_dir+"/"+extension+"/.nomedia";
		String move = "/system/bin/mv "+parent_dir+"/*"+extension+"* "+parent_dir+"/"+extension+"/";
		try {
			Runtime.getRuntime().exec(mkdir+" ; "+touch+" ; "+move);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
