package fr.codlab.cartes.dl;

final public class NoSdCardException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6241407367421159242L;

	@Override
	public String getMessage(){
		return "No SD Card found ! /sdcard/";
	}
}
