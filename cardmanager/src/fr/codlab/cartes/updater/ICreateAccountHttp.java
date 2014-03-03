package fr.codlab.cartes.updater;
/*
 * Interface created to manage upload
 * 
 * @author Kevin Le Perf
 * 
 */
interface ICreateAccountHttp {
	public void onCreateAccountSuccess(final String password);

	public void onCreateAccountFailure(final String text);
}
