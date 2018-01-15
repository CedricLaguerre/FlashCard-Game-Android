package com.example.trancoso.flashcard;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class AfficherJeuActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private String authority;
    private Spinner spinner;
    private SimpleCursorAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_jeu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        authority = "com.example.trancoso.flashcard";
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new SimpleCursorAdapter(AfficherJeuActivity.this,
                android.R.layout.simple_spinner_item, null,
                new String[]{"theme"},
                new int[]{android.R.id.text1}, 0);
        spinner.setAdapter(adapter);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content")
                .authority(authority)
                .appendPath("jeu_table")
                .build();
        return new CursorLoader(this, uri, new String[]{"_id", "theme"},
                null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
