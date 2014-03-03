package fr.codlab.cartes.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import fr.codlab.cartes.manageui.AccountUi;
import fr.codlab.cartes.util.Code;
import fr.codlab.cartes.util.Language;

public class SGBDPublic {
    private static SGBD __sgbd;

    private final Context context;
    public SGBDPublic(Context ctx) {
        this.context = ctx;
        if (__sgbd == null)
            __sgbd = new SGBD(context);
    }

    //---opens the database---
    public SGBDPublic open() {
        try{
            __sgbd.open();
        }catch(Exception e){

        }

        return this;
    }

    //---closes the database---
    public void close() {
        /*try{
            __sgbd.close();
        }catch(Exception e){

        }*/
        //TODO check with thread that every method returned
    }



    public long addCarteExtension(long extension, long carte, Language lang) {
        open();
        return __sgbd.addCarteExtension(extension, carte, lang);
    }

    public long addCode(String code) {
        open();
        return __sgbd.addCode(code);
    }

    public Code[] getCodes() {
        open();
        return __sgbd.getCodes();
    }

    public void deleteCodes(long id) {
        open();
        __sgbd.deleteCodes(id);
    }

    public long addCarteExtension(long extension, long carte, Language lang, int q, int qh, int qr) {
        open();
        return __sgbd.addCarteExtension(extension, carte, lang, q, qh, qr);
    }

    public int getPossessionExtension(long extension, Language lang) throws SQLException {
        open();
        return __sgbd.getPossessionExtension(extension, lang);
    }

    public int getPossessionsNumber(Language lang) throws SQLException {
        open();
        return __sgbd.getPossessionsNumber(lang);
    }


    public String getEncodedPossessions() {
        open();
        return __sgbd.getEncodedPossessions();
    }

    public void createfromEncodedPossessions(String encoded, AccountUi accountUi) {
        open();
        __sgbd.createfromEncodedPossessions(encoded, accountUi);
    }

    public void writePossessionJSON(OutputStreamWriter output) throws IOException {
        open();
        __sgbd.writePossessionJSON(output);
    }

    public void writePossessionXML(OutputStreamWriter output) throws IOException {
        open();
        __sgbd.writePossessionXML(output);
    }

    public void writePossessionCSV(OutputStreamWriter output) throws IOException {
        open();
        __sgbd.writePossessionCSV(output);
    }

    public int getExtensionProgression(long extension, Language lang) throws SQLException {
        open();
        return __sgbd.getExtensionProgression(extension, lang);
    }

    public int updatePossessionCarteExtensionNormal(long extension, long carte, Language lang, int quantite) {
        open();
        return __sgbd.updatePossessionCarteExtensionNormal(extension, carte, lang, quantite);
    }

    public int updatePossessionCarteExtensionReverse(long extension, long carte, Language lang, int quantite) {
        open();
        return __sgbd.updatePossessionCarteExtensionReverse(extension, carte, lang, quantite);
    }

    public int updatePossessionCarteExtensionHolo(long extension, long carte, Language lang, int quantite) {
        open();
        return __sgbd.updatePossessionCarteExtensionHolo(extension, carte, lang, quantite);
    }

    public int updatePossessionCarteExtensionAll(long extension, long carte, Language lang, int quantite, int quantite_reverse, int quantite_holo) {
        open();
        return __sgbd.updatePossessionCarteExtensionAll(extension, carte, lang, quantite, quantite_reverse, quantite_holo);
    }

    public int getPossessionCarteExtensionNormal(long extension, Language lang, long carte) {
        open();
        return __sgbd.getPossessionCarteExtensionNormal(extension, lang, carte);
    }

    public int getPossessionCarteExtensionReverse(long extension, Language lang, long carte) {
        open();
        return __sgbd.getPossessionCarteExtensionReverse(extension, lang, carte);
    }

    public int getPossessionCarteExtensionHolo(long extension, Language lang, long carte) {
        open();
        return __sgbd.getPossessionCarteExtensionNormal(extension, lang, carte);
    }
}
