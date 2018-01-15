package com.example.trancoso.flashcard;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JouerActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private String authority;
    private Spinner spinner;
    private SimpleCursorAdapter adapter;
    private SimpleCursorAdapter textViewAdapter;
    private Button b_jouer, b_valider;
    private TextView question, compartiment;
    private EditText reponse;
    private long spinnerID;
    private boolean firstTime = true;
    private ArrayList<String> q = new ArrayList<>();
    private ArrayList<String> r = new ArrayList<>();
    private ArrayList<String> c = new ArrayList<>();
    private ArrayList<String> ids_carte = new ArrayList<>();
    private int i = 0;
    private String s_reponse;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authority = "com.example.trancoso.flashcard";
        b_jouer = (Button) findViewById(R.id.ajouter);
        b_valider = (Button) findViewById(R.id.valider);
        reponse = (EditText) findViewById(R.id.reponse);
        question = (TextView) findViewById(R.id.question);
        compartiment = (TextView) findViewById(R.id.compartiment);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spinnerID = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = new SimpleCursorAdapter(JouerActivity.this,
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

    public void jouer(View view){
        final long id ;
        final String QUESTION = "Question: ";
        final String COMPARTIMENT = "Compartiment: ";
        int position = spinner.getSelectedItemPosition();
        id = adapter.getItemId(position);
        Cursor cursor = adapter.getCursor();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content")
                .authority(authority)
                .appendPath("carte_table")
                .appendPath("jeu");
        builder = ContentUris.appendId(builder, id);
        final Uri uri = builder.build();

        LoaderManager manager = getLoaderManager();
        manager.restartLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(),
                        uri, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                data = getContentResolver().query(uri, new String[]{"_id","recto", "verso", "compartiment"},
                        "_id = " + id, null, null);
                while (data.moveToNext()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    StringBuilder stringBuilder1 = new StringBuilder();
                    StringBuilder stringBuilder2 = new StringBuilder();
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder.append(data.getString(data.getColumnIndex("recto")));
                    stringBuilder1.append(data.getString(data.getColumnIndex("verso")));
                    stringBuilder2.append(data.getString(data.getColumnIndex("compartiment")));
                    stringBuilder3.append(data.getString(data.getColumnIndex("_id")));
                    q.add(stringBuilder.toString());
                    r.add(stringBuilder1.toString());
                    c.add(stringBuilder2.toString());
                    ids_carte.add((stringBuilder3.toString()));
                }
                if(q.size() == i)
                    i = 0;
                question.setText(QUESTION + q.get(i));
                compartiment.setText(COMPARTIMENT + c.get(i));
                s_reponse = r.get(i);
                i++;
                //adapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);
            }
        });

    }

    public void valider(View view){
        final long id ;
        int position = spinner.getSelectedItemPosition();
        id = adapter.getItemId(position);
        Editable editableR = reponse.getText();
        String ed_q = editableR .toString();
        if(ed_q.contentEquals("")){
            Toast.makeText(this,"un champ est vide",Toast.LENGTH_SHORT).show();
            return;
        }
        if(s_reponse.equals(reponse.getText().toString())) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content")
                    .authority(authority)
                    .appendPath("carte_table")
                    .appendPath("jeu");
            builder = ContentUris.appendId(builder, id);
            final Uri uri = builder.build();
            Toast.makeText(this, "Bonne réponse", Toast.LENGTH_SHORT).show();
            LoaderManager manager = getLoaderManager();
            manager.restartLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    return new CursorLoader(getApplicationContext(),
                            uri, null, null, null, null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    data = getContentResolver().query(uri, new String[]{"_id", "id_jeu", "compartiment"},
                            "_id = " + id, null, null);
                    ContentValues values = new ContentValues();
                    values.put("compartiment", data.getColumnIndex("compartiment"));
                    getContentResolver().update(uri, values,"id_jeu = " + id +
                            " AND _id = " + ids_carte.get(i-1), null);

                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            });
        }
        else
            Toast.makeText(this, "Mauvaise réponse", Toast.LENGTH_SHORT).show();
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
