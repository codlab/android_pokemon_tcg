package fr.codlab.cartes.updater;
/*
 * Interface created to manage upload
 * 
 * @author Kevin Le Perf
 * 
 */
interface IUploadHttp {
	/**
	 * onUploadSuccess method called when the upload has been done
	 * 
	 * @param text
	 *            a text describing the success
	 */
	public void onUploadSuccess(final String text);

	/**
	 * onUpdateFileFailure method called if the update has failed
	 * 
	 * @param project
	 *            the involved project
	 * @param file
	 *            the file name which has been saved
	 * @param text
	 *            a text describing the failure
	 * @param server_version
	 *            a string representing the file server or null
	 */
	public void onUploadFailure(final String text);
}
