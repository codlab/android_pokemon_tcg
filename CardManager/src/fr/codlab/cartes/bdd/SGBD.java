package fr.codlab.cartes.bdd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import fr.codlab.cartes.manageui.AccountUi;
import fr.codlab.cartes.util.Code;
import fr.codlab.cartes.util.Language;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class SGBD 
{    
	static final String DATABASE_NAME = "cardmanager";
	static final String TABLE_POSSESSIONS = "possession";
	static final String TABLE_POSSESSIONS_FR = "possession_fr";
	static final String TABLE_POSSESSIONS_ES = "possession_es";
	static final String TABLE_POSSESSIONS_IT = "possession_it";
	static final String TABLE_POSSESSIONS_DE = "possession_de";
	static final String TABLE_CODES = "codes";
	static final int DATABASE_VERSION = 3;

	/* 
	 * EXTENSION
	 * id INT primary key
	 * nom VARCHAR 30
	 * 
	 * CARTE
	 * 
	 */
	static final String CREATE_POSSESSION = "create table if not exists "+TABLE_POSSESSIONS+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer, quantite_reverse integer, quantite_holo integer)";
	static final String CREATE_POSSESSION_FR = "create table if not exists "+TABLE_POSSESSIONS_FR+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer, quantite_reverse integer, quantite_holo integer)";
	static final String CREATE_POSSESSION_ES = "create table if not exists "+TABLE_POSSESSIONS_ES+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer, quantite_reverse integer, quantite_holo integer)";
	static final String CREATE_POSSESSION_IT = "create table if not exists "+TABLE_POSSESSIONS_IT+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer, quantite_reverse integer, quantite_holo integer)";
	static final String CREATE_POSSESSION_DE = "create table if not exists "+TABLE_POSSESSIONS_DE+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer, quantite_reverse integer, quantite_holo integer)";
	static final String CREATE_CODES = "create table if not exists "+TABLE_CODES+" (_id integer primary key autoincrement,code varchar(14), status integer)";
	//private static final String CREATE_POSSESSION_HOLO = "create table if not exists "+TABLE_POSSESSIONS_HOLO+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer)";
	//private static final String CREATE_POSSESSION_HOLO = "create table if not exists "+TABLE_POSSESSIONS_HOLO+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer)";


	private final Context context; 

	private static DatabaseHelper DBHelper;
	private static SQLiteDatabase db;

	public SGBD(Context ctx) 
	{
		this.context = ctx;
		if(DBHelper == null)
			DBHelper = new DatabaseHelper(context);
	}

	//---opens the database---
	public SGBD open() throws SQLException{
		if(db == null || !db.isOpen()){
			db = DBHelper.getWritableDatabase();
			db.execSQL(CREATE_POSSESSION);
			db.execSQL(CREATE_POSSESSION_FR);
			db.execSQL(CREATE_POSSESSION_ES);
			db.execSQL(CREATE_POSSESSION_IT);
			db.execSQL(CREATE_POSSESSION_DE);
			db.execSQL(CREATE_CODES);
		}

		return this;
	}

	//---closes the database---    
	public void close() 
	{
		DBHelper.close();
		db = null;
	}

	public long addCarteExtension(long extension, long carte, Language lang){
		return addCarteExtension(extension, carte, lang, 0, 0, 0);
	}

	public long addCode(String code){
		ContentValues initialValues = new ContentValues();
		initialValues.put("code", code);
		initialValues.put("status", 0);
		return db.insert(TABLE_CODES, null, initialValues);
	}

	private void updateCode(long id, int status){
		ContentValues initialValues = new ContentValues();
		if(status<0)
			status=0;
		initialValues.put("status", status);

		db.update(TABLE_CODES, initialValues, "_id="+id, null);

	}
	public Code [] getCodes(){
		Cursor mCursor = db.query(true, TABLE_CODES, new String[] {
				"_id",
				"code",
		"status"},
		null, 
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		Code [] codes = null;
		if(mCursor.getCount()>0){
			codes = new Code[mCursor.getCount()];
			int val=0;
			long id=0;
			String code = "";
			int index = 0;
			while(!mCursor.isAfterLast()){
				id = mCursor.getLong(mCursor.getColumnIndex("_id"));
				val = mCursor.getInt(mCursor.getColumnIndex("status"));
				code =mCursor.getString(mCursor.getColumnIndex("code"));
				mCursor.moveToNext();
				codes[index] = new Code(id, val, code);
				index++;
			}
		}
		mCursor.close();
		
		
		return codes;
	}
	public void deleteCodes(long id){
		db.delete(TABLE_CODES, "_id="+id, null);
	}
	public long addCarteExtension(long extension, long carte, Language lang, int q, int qh, int qr){
		ContentValues initialValues = new ContentValues();
		initialValues.put("extension", extension);
		initialValues.put("carte", carte);
		initialValues.put("quantite", q);
		initialValues.put("quantite_holo", qh);
		initialValues.put("quantite_reverse", qr);
		switch(lang){
		case FR:
			return db.insert(TABLE_POSSESSIONS_FR, null, initialValues);
		case DE:
			return db.insert(TABLE_POSSESSIONS_DE, null, initialValues);
		case ES:
			return db.insert(TABLE_POSSESSIONS_ES, null, initialValues);
		case IT:
			return db.insert(TABLE_POSSESSIONS_IT, null, initialValues);
		default:
			return db.insert(TABLE_POSSESSIONS, null, initialValues);
		}
	}

	private int updateCarteExtensionNormal(long extension, long carte, Language lang, long valeur){
		ContentValues initialValues = new ContentValues();
		if(valeur<0)
			valeur=0;
		initialValues.put("quantite", valeur);
		switch(lang){
		case FR:
			return db.update(TABLE_POSSESSIONS_FR, initialValues, "carte="+carte+" AND extension="+extension, null);
		case DE:
			return db.update(TABLE_POSSESSIONS_DE, initialValues, "carte="+carte+" AND extension="+extension, null);
		case ES:
			return db.update(TABLE_POSSESSIONS_ES, initialValues, "carte="+carte+" AND extension="+extension, null);
		case IT:
			return db.update(TABLE_POSSESSIONS_IT, initialValues, "carte="+carte+" AND extension="+extension, null);
		default:
			return db.update(TABLE_POSSESSIONS, initialValues, "carte="+carte+" AND extension="+extension, null);
		}
	}
	private int updateCarteExtensionAll(long extension, long carte, Language lang, long quantite, long quantite_reverse, long quantite_holo){
		if(quantite<0)
			quantite=0;
		if(quantite_reverse<0)
			quantite_reverse=0;
		if(quantite_holo<0)
			quantite_holo=0;
		ContentValues initialValues = new ContentValues();
		initialValues.put("quantite", quantite);
		initialValues.put("quantite_holo", quantite_reverse);
		initialValues.put("quantite_reverse", quantite_holo);
		switch(lang){
		case FR:
			return db.update(TABLE_POSSESSIONS_FR, initialValues, "carte="+carte+" AND extension="+extension, null);
		case DE:
			return db.update(TABLE_POSSESSIONS_DE, initialValues, "carte="+carte+" AND extension="+extension, null);
		case ES:
			return db.update(TABLE_POSSESSIONS_ES, initialValues, "carte="+carte+" AND extension="+extension, null);
		case IT:
			return db.update(TABLE_POSSESSIONS_IT, initialValues, "carte="+carte+" AND extension="+extension, null);
		default:
			return db.update(TABLE_POSSESSIONS, initialValues, "carte="+carte+" AND extension="+extension, null);
		}
	}

	private int updateCarteExtensionHolo(long extension, long carte, Language lang, long valeur){
		ContentValues initialValues = new ContentValues();
		if(valeur<0)
			valeur=0;
		initialValues.put("quantite_holo", valeur);
		switch(lang){
		case FR:
			return db.update(TABLE_POSSESSIONS_FR, initialValues, "carte="+carte+" AND extension="+extension, null);
		case DE:
			return db.update(TABLE_POSSESSIONS_DE, initialValues, "carte="+carte+" AND extension="+extension, null);
		case ES:
			return db.update(TABLE_POSSESSIONS_ES, initialValues, "carte="+carte+" AND extension="+extension, null);
		case IT:
			return db.update(TABLE_POSSESSIONS_IT, initialValues, "carte="+carte+" AND extension="+extension, null);
		default:
			return db.update(TABLE_POSSESSIONS, initialValues, "carte="+carte+" AND extension="+extension, null);
		}
	}

	private int updateCarteExtensionReverse(long extension, long carte, Language lang, long valeur){
		ContentValues initialValues = new ContentValues();
		if(valeur<0)
			valeur=0;
		initialValues.put("quantite_reverse", valeur);
		switch(lang){
		case FR:
			return db.update(TABLE_POSSESSIONS_FR, initialValues, "carte="+carte+" AND extension="+extension, null);
		case DE:
			return db.update(TABLE_POSSESSIONS_DE, initialValues, "carte="+carte+" AND extension="+extension, null);
		case ES:
			return db.update(TABLE_POSSESSIONS_ES, initialValues, "carte="+carte+" AND extension="+extension, null);
		case IT:
			return db.update(TABLE_POSSESSIONS_IT, initialValues, "carte="+carte+" AND extension="+extension, null);
		default:
			return db.update(TABLE_POSSESSIONS, initialValues, "carte="+carte+" AND extension="+extension, null);
		}
	}

	public int getPossessionExtension(long extension, Language lang) throws SQLException{ 
		Cursor mCursor = null;
		switch(lang){
		case FR:
			mCursor = db.query(true, TABLE_POSSESSIONS_FR, new String[] {
					"SUM(quantite) as q",
					"SUM(quantite_holo) as qh",
					"SUM(quantite_reverse) as qr"
			}, 
			"extension=" + extension, 
			null,null,null,null,null);
			break;
		case DE:
			mCursor = db.query(true, TABLE_POSSESSIONS_DE, new String[] {
					"SUM(quantite) as q",
					"SUM(quantite_holo) as qh",
					"SUM(quantite_reverse) as qr"
			}, 
			"extension=" + extension, 
			null,null,null,null,null);
			break;
		case ES:
			mCursor = db.query(true, TABLE_POSSESSIONS_ES, new String[] {
					"SUM(quantite) as q",
					"SUM(quantite_holo) as qh",
					"SUM(quantite_reverse) as qr"
			}, 
			"extension=" + extension, 
			null,null,null,null,null);
			break;
		case IT:
			mCursor = db.query(true, TABLE_POSSESSIONS_IT, new String[] {
					"SUM(quantite) as q",
					"SUM(quantite_holo) as qh",
					"SUM(quantite_reverse) as qr"
			}, 
			"extension=" + extension, 
			null,null,null,null,null);
			break;
		default:
			mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
					"SUM(quantite) as q",
					"SUM(quantite_holo) as qh",
					"SUM(quantite_reverse) as qr"
			}, 
			"extension=" + extension, 
			null,null,null,null,null);
			break;
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int val = 0;
		if(mCursor.getCount()>0){
			val = mCursor.getInt(mCursor.getColumnIndex("q"))+
					mCursor.getInt(mCursor.getColumnIndex("qh"))+
					mCursor.getInt(mCursor.getColumnIndex("qr"));
		}
		mCursor.close();
		return val;
	}

	private Cursor getPossessions(Language lang){
		Cursor cursor = null;
		switch(lang){
		case FR:
			cursor = db.query(true, TABLE_POSSESSIONS_FR, new String[]{
					"extension as e",
					"carte as c",
					"quantite as q",
					"quantite_holo as qh",
					"quantite_reverse as qr"
			},null,
			null,null,null,"e",null);
			break;
		case DE:
			cursor = db.query(true, TABLE_POSSESSIONS_DE, new String[]{
					"extension as e",
					"carte as c",
					"quantite as q",
					"quantite_holo as qh",
					"quantite_reverse as qr"
			},null,
			null,null,null,"e",null);
			break;
		case ES:
			cursor = db.query(true, TABLE_POSSESSIONS_ES, new String[]{
					"extension as e",
					"carte as c",
					"quantite as q",
					"quantite_holo as qh",
					"quantite_reverse as qr"
			},null,
			null,null,null,"e",null);
			break;
		case IT:
			cursor = db.query(true, TABLE_POSSESSIONS_IT, new String[]{
					"extension as e",
					"carte as c",
					"quantite as q",
					"quantite_holo as qh",
					"quantite_reverse as qr"
			},null,
			null,null,null,"e",null);
			break;
		default:
			cursor = db.query(true, TABLE_POSSESSIONS, new String[]{
					"extension as e",
					"carte as c",
					"quantite as q",
					"quantite_holo as qh",
					"quantite_reverse as qr"
			},null,
			null,null,null,"e",null);
		}

		if(cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public int getPossessionsNumber(Language lang) throws SQLException{ 
		Cursor mCursor = null;
		switch(lang){
		case FR:
			mCursor = db.query(true, TABLE_POSSESSIONS_FR, new String[] {"COUNT(quantite) as q"},null,null,null,null,null,null);
			break;
		case DE:
			mCursor = db.query(true, TABLE_POSSESSIONS_DE, new String[] {"COUNT(quantite) as q"},null,null,null,null,null,null);
			break;
		case ES:
			mCursor = db.query(true, TABLE_POSSESSIONS_ES, new String[] {"COUNT(quantite) as q"},null,null,null,null,null,null);
			break;
		case IT:
			mCursor = db.query(true, TABLE_POSSESSIONS_IT, new String[] {"COUNT(quantite) as q"},null,null,null,null,null,null);
			break;
		default:
			mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {"COUNT(quantite) as q"},null,null,null,null,null,null);
			break;
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int val = 0;
		if(mCursor.getCount()>0)
			val = mCursor.getInt(mCursor.getColumnIndex("q"));
		mCursor.close();
		return val;
	}
	/**
	 * Create Possession file
	 * 
	 * Create xml
	 * <possessions>
	 * 	<carte e='' id='' qn='' qh='' qr='' />
	 * </possessions>
	 * 
	 * Create csv
	 * extension;carte;quantite;quantite_holo;quantite_reverse
	 * 
	 * Create json
	 * {possessions:{[
	 * 	{carte:{
	 * 		e:'',
	 * 		id:'',
	 * 		qn:'',
	 * 		qh:'',
	 * 		qr:''
	 * 		}
	 * 	},
	 * 	{carte:{
	 * 		e:'',
	 * 		id:'',
	 * 		qn:'',
	 * 		qh:'',
	 * 		qr:''
	 * 		}
	 * 	}
	 * 	]}
	 * }
	 * @param output
	 * @param mode
	 * @throws IOException
	 */
	private void writePossessions(OutputStreamWriter output, Output mode) throws IOException{
		if(output != null){
			if(mode == Output.XML){
				output.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
				output.write("<possessions>\n");
			}else if(mode == Output.CSV){
				output.write("extension"+
						";carte"+
						";quantite"+
						";quantite_holo"+
						";quantite_reverse\n");
			}else if(mode == Output.JSON){
				output.write("{possessions:[");
			}

			//TODO implement FR,ES,IT...
			Cursor cursor = getPossessions(Language.US);
			boolean f=false;
			if(cursor != null){
				switch(mode){
				case XML:
					output.write("	<carte e=\""+cursor.getColumnIndex("e")+
							"\" id=\""+cursor.getColumnIndex("c")+
							"\" qn=\""+cursor.getColumnIndex("q")+
							"\" qh=\""+cursor.getColumnIndex("qh")+
							"\" qr=\""+cursor.getColumnIndex("qr")+
							"\" />\n");
					break;
				case JSON:
					if(f)
						output.write(",\n");
					output.write("{" +
							"carte:{" +
							"e:'"+cursor.getColumnIndex("e")+"',"+
							"id:'"+cursor.getColumnIndex("c")+"',"+
							"qn:'"+cursor.getColumnIndex("q")+"',"+
							"qh:'"+cursor.getColumnIndex("qh")+"',"+
							"qr:'"+cursor.getColumnIndex("qr")+"'"+
							"}");
					f=true;
					break;
				case CSV:
					output.write(cursor.getColumnIndex("e")+
							";"+cursor.getColumnIndex("c")+
							";"+cursor.getColumnIndex("q")+
							";"+cursor.getColumnIndex("qh")+
							";"+cursor.getColumnIndex("qr")+"\n");
					break;
				default:

				}
				cursor.moveToNext();
			}
			cursor.close();
			if(mode == Output.XML)
				output.write("</possessions>\n");
			else if(mode == Output.JSON){
				if(f)
					output.write("\n");
				output.write("]}");
			}
		}
	}

	private String getEncodedPossessionsSubLanguage(Language lang){
		String json_envoi="";
		switch(lang){
		case FR:
			json_envoi="data_fr:[";break;
		case DE:
			json_envoi="data_de:[";break;
		case ES:
			json_envoi="data_es:[";break;
		case IT:
			json_envoi="data_it:[";break;
		default:
			json_envoi="data:[";break;
		}
		short last = 0;
		int nb_extension = 0;
		int nb_carte = 0;
		int nb_cartes =0;
		Cursor cursor = getPossessions(lang);
		if(cursor != null){
			while(!cursor.isAfterLast()){
				nb_carte = 0;
				last = cursor.getShort(cursor.getColumnIndex("e"));
				if(nb_extension != 0)
					json_envoi+=",";
				nb_extension++;

				json_envoi+="{e:'"+last+"',c:[";
				while(!cursor.isAfterLast() && cursor.getShort(cursor.getColumnIndex("e")) == last){
					if(cursor.getShort(cursor.getColumnIndex("c")) >= 0){
						if(nb_carte != 0)
							json_envoi+=",";
						nb_carte++;
						json_envoi+="{c:'"+cursor.getShort(cursor.getColumnIndex("c"));
						if(cursor.getShort(cursor.getColumnIndex("q")) > 0)
							json_envoi+="',q:'"+cursor.getShort(cursor.getColumnIndex("q"));
						if(cursor.getShort(cursor.getColumnIndex("qh")) > 0)
							json_envoi+="',qh:'"+cursor.getShort(cursor.getColumnIndex("qh"));
						if(cursor.getShort(cursor.getColumnIndex("qr")) > 0)
							json_envoi+="',qr:'"+cursor.getShort(cursor.getColumnIndex("qr"));
						json_envoi+="'}";
						Log.d("trouve carte", "e: "+cursor.getShort(cursor.getColumnIndex("e"))+" c:"+cursor.getShort(cursor.getColumnIndex("c"))
								+" q:"+cursor.getShort(cursor.getColumnIndex("q"))+
								" qh:"+cursor.getShort(cursor.getColumnIndex("qh"))+
								" qr:"+cursor.getShort(cursor.getColumnIndex("qr")));
						nb_cartes++;
					}
					cursor.moveToNext();
				}
				json_envoi+="]}";
			}
			cursor.close();
		}
		switch(lang){
		case FR:
			json_envoi+="],nb_fr:'"+nb_cartes+"'";break;
		case DE:
			json_envoi+="],nb_de:'"+nb_cartes+"'";break;
		case ES:
			json_envoi+="],nb_es:'"+nb_cartes+"'";break;
		case IT:
			json_envoi+="],nb_it:'"+nb_cartes+"'";break;
		default:
			json_envoi+="],nb:'"+nb_cartes+"'";break;
		}
		return json_envoi;
	}
	public String getEncodedPossessions(){
		String json_envoi = "";
		json_envoi+="{";
		json_envoi+=this.getEncodedPossessionsSubLanguage(Language.US);
		json_envoi+=", "+this.getEncodedPossessionsSubLanguage(Language.FR);
		json_envoi+=", "+this.getEncodedPossessionsSubLanguage(Language.ES);
		json_envoi+=", "+this.getEncodedPossessionsSubLanguage(Language.IT);
		json_envoi+=", "+this.getEncodedPossessionsSubLanguage(Language.DE);
		json_envoi+="}";
		byte [] array = json_envoi.getBytes();
		Log.d("b64",Base64.encodeToString(array,0, array.length, Base64.DEFAULT));
		return Base64.encodeToString(array,0, array.length, Base64.DEFAULT);
	}

	final private class DownloadUpdater extends AsyncTask<String, Integer, Long>{
		private AccountUi context;
		private int max = 0;
		private byte [] data;
		private JSONObject toto;
		boolean fini = false;
		DownloadUpdater(AccountUi context, JSONObject main){
			toto = main;
			try {
				max=0;
				if(main.has("nb"))
					max += main.getInt("nb");
				if(main.has("nb_fr"))
					max += main.getInt("nb_fr");
				if(main.has("nb_es"))
					max += main.getInt("nb_es");
				if(main.has("nb_it"))
					max += main.getInt("nb_it");
				if(main.has("nb_de"))
					max += main.getInt("nb_de");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setAccountUi(context);
		}

		void setAccountUi(AccountUi context){
			fini = false;
			this.context = context;
			context.createWaiter(max);
		}

		@Override
		protected Long doInBackground(String... arg0) {
			fini = false;
			try {
				open();
				JSONArray _extensions=null;
				if(toto.has("data")){
					_extensions = toto.getJSONArray("data");
					manageData(_extensions, Language.US);
				}
				if(toto.has("data_fr")){
					_extensions = toto.getJSONArray("data_fr");
					manageData(_extensions, Language.FR);
				}
				if(toto.has("data_de")){
					_extensions = toto.getJSONArray("data_de");
					manageData(_extensions, Language.DE);
				}
				if(toto.has("data_es")){
					_extensions = toto.getJSONArray("data_es");
					manageData(_extensions, Language.ES);
				}
				if(toto.has("data_it")){
					_extensions = toto.getJSONArray("data_it");
					manageData(_extensions, Language.IT);
				}
				close();
			}catch(Exception e){
				e.printStackTrace();
			}
			fini = true;
			return null;
		}

		private void manageData(JSONArray _extensions, Language lang) throws JSONException{
			JSONObject _extension = null;
			JSONArray _cartes=null;
			JSONObject _carte = null;
			int q = 0;
			int qh = 0;
			int qr = 0;
			//long total = toto.getLong("nb");
			int actuel = 0;
			for(int i=0;i<_extensions.length();i++){
				_extension = _extensions.getJSONObject(i);
				_cartes = _extension.getJSONArray("c");
				Log.d("new extension", "extension :"+_extension.getInt("e"));
				for(int j = 0;j<_cartes.length();j++){
					_carte = _cartes.getJSONObject(j);
					q = _carte.has("q") ? _carte.getInt("q") : 0;
					qh = _carte.has("qh") ? _carte.getInt("qh") : 0;
					qr = _carte.has("qr") ? _carte.getInt("qr") : 0;
					//Log.d("trouve carte", "e: "+_extension.getInt("e")+" c:"+_carte.getInt("c")+" q:"+q+" qh:"+qh+" qr:"+qr);
					updatePossessionCarteExtensionAll(_extension.getInt("e"), _carte.getInt("c"), lang, q, qr, qh);
					//updatePossessionCarteExtensionNormal(_extension.getInt("e"), _carte.getInt("c"), lang, q);
					//updatePossessionCarteExtensionHolo(_extension.getInt("e"), _carte.getInt("c"), lang, qh);
					//updatePossessionCarteExtensionReverse(_extension.getInt("e"), _carte.getInt("c"), lang, qr);					

					actuel++;
					this.publishProgress(actuel);
				}
			}
		}
		public void onProgressUpdate(Integer... args){
			if(context != null)
				context.incremente(args[0]);
		}

		protected void onPostExecute(Long result) {
			if(context != null && fini)
				context.onCreateFinish();
			else if(context != null)
				context.onDLNotFinish();
		}
	}

	static DownloadUpdater dl;
	public void createfromEncodedPossessions(String encoded, AccountUi accountUi){
		Log.d("encoded", encoded);
		byte [] d = Base64.decode(encoded, Base64.DEFAULT);
		String dd = new String(d);
		JSONObject json;
		try {
			json = new JSONObject(dd);
			if(dl == null){
				dl = new DownloadUpdater(accountUi, json);
				dl.execute("e");
			}else{
				dl.setAccountUi(accountUi);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writePossessionJSON(OutputStreamWriter output) throws IOException{
		writePossessions(output, Output.JSON);
	}
	public void writePossessionXML(OutputStreamWriter output) throws IOException{
		writePossessions(output, Output.XML);
	}
	public void writePossessionCSV(OutputStreamWriter output) throws IOException{
		writePossessions(output, Output.CSV);
	}
	private String readPossessionToString(InputStreamReader input) throws IOException{
		StringWriter res = new StringWriter();
		BufferedReader buffer=new BufferedReader(input);
		String line="";
		while ( null!=(line=buffer.readLine())){
			res.write(line); 
		}
		return res.toString();
	}
	private void readPossession(InputStreamReader input, Output mode) throws IOException, JSONException, XmlPullParserException{
		if(mode == Output.JSON){
			String res = readPossessionToString(input);			
			JSONObject obj = new JSONObject(res);
		}else if(mode == Output.XML){
			XmlPullParser p = XmlPullParserFactory.newInstance().newPullParser();
			p.setInput(input);
		}
	}

	public int getExtensionProgression(long extension, Language lang) throws SQLException{ 
		Cursor mCursor = null;
		switch(lang){
		case FR:
			mCursor = db.query(true, TABLE_POSSESSIONS_FR, new String[] {"count(*) as total"}, 
					"extension=" + extension+" AND (quantite>0 OR quantite_holo>0 OR quantite_reverse>0)",
					null,null,null,null,null);
			break;
		case DE:
			mCursor = db.query(true, TABLE_POSSESSIONS_DE, new String[] {"count(*) as total"}, 
					"extension=" + extension+" AND (quantite>0 OR quantite_holo>0 OR quantite_reverse>0)",
					null,null,null,null,null);
			break;
		case ES:
			mCursor = db.query(true, TABLE_POSSESSIONS_ES, new String[] {"count(*) as total"}, 
					"extension=" + extension+" AND (quantite>0 OR quantite_holo>0 OR quantite_reverse>0)",
					null,null,null,null,null);
			break;
		case IT:
			mCursor = db.query(true, TABLE_POSSESSIONS_IT, new String[] {"count(*) as total"}, 
					"extension=" + extension+" AND (quantite>0 OR quantite_holo>0 OR quantite_reverse>0)",
					null,null,null,null,null);
			break;
		default:
			mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {"count(*) as total"}, 
					"extension=" + extension+" AND (quantite>0 OR quantite_holo>0 OR quantite_reverse>0)",
					null,null,null,null,null);
		}

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int val = 0;
		if(mCursor.getCount()>0)
			val = mCursor.getInt(mCursor.getColumnIndex("total"));
		mCursor.close();
		return val;
	}

	private int getPossessionCarteExtension(long extension, long carte, Language lang, int type) throws SQLException{ 
		Cursor mCursor = null;
		switch(lang){
		case FR:
			mCursor = db.query(true, TABLE_POSSESSIONS_FR, new String[] {
					"quantite",
					"quantite_reverse",
					"quantite_holo"
			}, 
			"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case DE:
			mCursor = db.query(true, TABLE_POSSESSIONS_DE, new String[] {
					"quantite",
					"quantite_reverse",
					"quantite_holo"
			}, 
			"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case ES:
			mCursor = db.query(true, TABLE_POSSESSIONS_ES, new String[] {
					"quantite",
					"quantite_reverse",
					"quantite_holo"
			}, 
			"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case IT:
			mCursor = db.query(true, TABLE_POSSESSIONS_IT, new String[] {
					"quantite",
					"quantite_reverse",
					"quantite_holo"
			}, 
			"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		default:
			mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
					"quantite",
					"quantite_reverse",
					"quantite_holo"
			}, 
			"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int val = 0;
		if(mCursor.getCount()>0){
			if(type == NORMAL)
				val = mCursor.getInt(mCursor.getColumnIndex("quantite"));
			else if(type == REVERSE)
				val = mCursor.getInt(mCursor.getColumnIndex("quantite_reverse"));
			else if(type == HOLO)
				val = mCursor.getInt(mCursor.getColumnIndex("quantite_holo"));
		}
		mCursor.close();
		return val;
	}


	private final static int NORMAL=0;
	private final static int REVERSE=1;
	private final static int HOLO=2;

	private int updatePossessionCarteExtensionGenerique(long extension, long carte, Language lang, int quantite, int type) throws SQLException{ 			
		Cursor mCursor=null;
		switch(lang){
		case FR:
			mCursor = db.query(true, TABLE_POSSESSIONS_FR, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case DE:
			mCursor = db.query(true, TABLE_POSSESSIONS_DE, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case ES:
			mCursor = db.query(true, TABLE_POSSESSIONS_ES, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case IT:
			mCursor = db.query(true, TABLE_POSSESSIONS_IT, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		default:
			mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if(mCursor.getCount()<=0)
			this.addCarteExtension(extension, carte, lang);
		mCursor.close();

		switch(type){
		case NORMAL:
			return this.updateCarteExtensionNormal(extension, carte, lang, quantite);
		case REVERSE:
			return this.updateCarteExtensionReverse(extension, carte, lang, quantite);
		case HOLO:
			return this.updateCarteExtensionHolo(extension, carte, lang, quantite);
		default:
			return 0;

		}		
	}
	private int updatePossessionCarteExtensionAllGenerique(long extension, long carte, Language lang, int quantite, int quantite_reverse, int quantite_holo) throws SQLException{ 			
		Cursor mCursor=null;
		switch(lang){
		case FR:
			mCursor = db.query(true, TABLE_POSSESSIONS_FR, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case DE:
			mCursor = db.query(true, TABLE_POSSESSIONS_DE, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case ES:
			mCursor = db.query(true, TABLE_POSSESSIONS_ES, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		case IT:
			mCursor = db.query(true, TABLE_POSSESSIONS_IT, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		default:
			mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
			"quantite"},"carte="+carte+" AND extension=" + extension, 
			null,null,null,null,null);break;
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if(mCursor.getCount()<=0){
			this.addCarteExtension(extension, carte, lang, quantite, quantite_holo,quantite_reverse);
			mCursor.close();
			return 0;
		}else{
			mCursor.close();
			return this.updateCarteExtensionAll(extension, carte, lang, quantite, quantite_reverse, quantite_holo);
		}		
	}

	public int updatePossessionCarteExtensionNormal(long extension, long carte, Language lang, int quantite){
		return updatePossessionCarteExtensionGenerique(extension, carte, lang, quantite, NORMAL);
	}
	public int updatePossessionCarteExtensionReverse(long extension, long carte, Language lang, int quantite){
		return updatePossessionCarteExtensionGenerique(extension, carte, lang, quantite, REVERSE);
	}
	public int updatePossessionCarteExtensionHolo(long extension, long carte, Language lang, int quantite){
		return updatePossessionCarteExtensionGenerique(extension, carte, lang, quantite, HOLO);
	}

	public int updatePossessionCarteExtensionAll(long extension, long carte, Language lang, int quantite, int quantite_reverse, int quantite_holo){
		return updatePossessionCarteExtensionAllGenerique(extension, carte, lang, quantite, quantite_reverse, quantite_holo);
	}

	public int getPossessionCarteExtensionNormal(long extension, Language lang, long carte){
		return getPossessionCarteExtension(extension, carte, lang, NORMAL);
	}
	public int getPossessionCarteExtensionReverse(long extension, Language lang, long carte){
		return getPossessionCarteExtension(extension, carte, lang, REVERSE);
	}
	public int getPossessionCarteExtensionHolo(long extension, Language lang, long carte){
		return getPossessionCarteExtension(extension, carte, lang, HOLO);
	}
}
