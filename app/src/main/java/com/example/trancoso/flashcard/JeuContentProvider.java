package com.example.trancoso.flashcard;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class JeuContentProvider extends ContentProvider {
    private static String authority = "com.example.trancoso.flashcard";
    private BaseJeu helper;
    private static final int JEU = 1;
    private static final int CARTE = 2;
    private static final int CARTE_DUN_JEU = 3;
    private static final int UN_JEU = 4;


    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(authority, "jeu_table", JEU);
        matcher.addURI(authority, "carte_table", CARTE);
        matcher.addURI(authority, "carte_table/jeu/#", CARTE_DUN_JEU);
        matcher.addURI(authority, "jeu_table/#", UN_JEU);

    }

    public JeuContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        long id = 0;
        int i;
        Cursor cursor;
        Log.d("uri delete =", uri.toString());
        Log.d("code=", ""+ code);
        switch (code) {
            case UN_JEU:
                id = ContentUris.parseId(uri);
                cursor = db.query("carte_table", new String[]{"_id"}, "id_jeu=" + id, null, null, null, null);
                cursor.moveToFirst();
                while(cursor.moveToNext()) {
                    i = db.delete("carte_table", "id_jeu=" + id, null);
                }
                i=db.delete("jeu_table", "_id=" + id, null);
                break;
            case CARTE_DUN_JEU:
                id = ContentUris.parseId(uri);
                i=db.delete("carte_table", "_id=" + id, null);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return i;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        Log.d("Uri isnsert = ", uri.toString());
        long id = 0;
        Uri.Builder builder = new Uri.Builder();

        switch (code) {
            case JEU:
                id = db.insertOrThrow("jeu_table", null, values);
                if(id == -1) {
                    throw new RuntimeException(String.format(
                            "%s : Failed to insert [%s] for unknown reasons.", "jeu_table", values, uri));
                }
                builder.appendPath("jeu_table");
                break;
            case CARTE:
                id = db.insertOrThrow("carte_table", null, values);
                if(id == -1) {
                    throw new RuntimeException(String.format(
                            "%s : Failed to insert [%s] for unknown reasons.", "carte_table", values, uri));
                }
                builder.appendPath("carte_table");
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        builder.authority(authority);
        builder = ContentUris.appendId(builder, id);
        return builder.build();
    }


    @Override
    public boolean onCreate() {
        helper = com.example.trancoso.flashcard.BaseJeu.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor;
        Log.d("Uri query  =", uri.toString());
        switch (code) {
            case JEU:
                cursor = db.query("jeu_table", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CARTE:
                cursor = db.query("carte_table", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CARTE_DUN_JEU:
                long id = ContentUris.parseId(uri);
                cursor = db.query("carte_table", new String[]{"_id", "recto", "verso", "compartiment"},
                        "id_jeu = " + id , null, null, null,null);
                break;
            default:
                Log.d("code  =", "" + code);
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        long id = 0;
        int i;
        Cursor cursor;
        Log.d("uri update =", uri.toString());
        Log.d("code=", ""+ code);
        switch (code) {
            case CARTE_DUN_JEU:
                id = ContentUris.parseId(uri);
                //cursor = db.query("carte_table", new String[]{"_id", "compartiment"}, "id_jeu=" + id, null, null, null, null);
                i=db.update("carte_table", values, selection, null);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
