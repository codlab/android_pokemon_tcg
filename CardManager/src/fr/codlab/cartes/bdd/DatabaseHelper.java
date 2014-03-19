package fr.codlab.cartes.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {
	DatabaseHelper(Context context) 
	{
		super(context, SGBD.DATABASE_NAME, null, SGBD.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(SGBD.CREATE_POSSESSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, 
			int newVersion) 
	{
		db.execSQL("ALTER TABLE "+SGBD.TABLE_POSSESSIONS+" ADD quantite_holo integer");
		db.execSQL("ALTER TABLE "+SGBD.TABLE_POSSESSIONS+" ADD quantite_reverse integer");

		onCreate(db);
	}
}
