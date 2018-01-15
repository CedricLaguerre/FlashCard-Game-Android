package com.example.trancoso.flashcard;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class AjouterJeuActivity extends AppCompatActivity
        implements  LoaderManager.LoaderCallbacks<Cursor>{
    private static String authority = "com.example.trancoso.flashcard";
    SimpleCursorAdapter adapter;
    private EditText theme;
    private Toolbar toolbar;

    private final static String DB_NAME = "base_jeu";

    private final static String TABLE_JEU = "jeu_table";
    private final static String COLONNE_THEME = "theme";

    private final static String TABLE_CARTE = "carte_table";
    private final static String COLONNE_RECTO = "recto";
    private final static String COLONNE_VERSO = "verso";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_jeu);
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                new String[]{"theme"},
                new int[]{android.R.id.text1}, 0);
        theme = (EditText)findViewById(R.id.theme);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.jouer:
                intent = new Intent(this, JouerActivity.class);
                startActivity(intent);
                return true;
            case R.id.affJeu:
                intent = new Intent(this, ListeJeuActivity.class);
                startActivity(intent);
                return true;
            case R.id.param:
                intent = new Intent(this, ParamActivity.class);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void afficher(View view){
        Intent intent = new Intent(this, AfficherJeuActivity.class);
        startActivity(intent);
    }

    public void ajouter(View view){
        Editable editable = theme.getText();
        String title = editable.toString();
        if(title.contentEquals("")){
            Toast.makeText(this,"theme vide",Toast.LENGTH_SHORT).show();
            return;
        }

        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put("theme",theme.getText().toString());

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_JEU);
        Uri uri = builder.build();
        uri = resolver.insert(uri,values);
        editable.clear();
        Toast.makeText(this,"theme ajouté",Toast.LENGTH_SHORT).show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content")
                .authority(authority)
                .appendPath(TABLE_JEU)
                .build();
        return new CursorLoader(this, uri, new String[]{"_id", "theme"},
                null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
        Button ajouter = (Button) findViewById(R.id.ajouter);
        ajouter.setActivated(true);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
