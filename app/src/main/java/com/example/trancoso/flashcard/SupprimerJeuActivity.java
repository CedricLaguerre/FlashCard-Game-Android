package com.example.trancoso.flashcard;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.ListView;

import java.util.ArrayList;


public class SupprimerJeuActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private  String authority = "com.example.trancoso.flashcard";
    private SimpleCursorAdapter adapterList;
    private ListView list;
    private Toolbar toolbar;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        long[] ids = list.getCheckedItemIds();
        if (ids.length == 0)
            return;
        ArrayList<Integer> idesArray = new ArrayList<>();
        for (long id : ids) {
            idesArray.add(new Integer((int) id));

        }
        outState.putIntegerArrayList("ids", idesArray);
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

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Integer> idesArray = savedInstanceState.getIntegerArrayList("ids");
        if(idesArray == null)
            return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_jeu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) findViewById(R.id.list);
        adapterList = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_checked, null,
                new String[]{"theme"},
                new int[]{android.R.id.text1}, 0);
        list.setAdapter(adapterList);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, this);

    }

    public void supprimer(View view){
        long[] ids = list.getCheckedItemIds();
        for(long id : ids) {
            ContentResolver resolver = getContentResolver();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content")
                    .authority(authority)
                    .appendPath("jeu_table");
            ContentUris.appendId(builder, id);
            Uri uri = builder.build();

            int res = resolver.delete(uri, null, null);
            Log.d("result of delete=", res + "");
        }
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri.Builder builder = (new Uri.Builder()).scheme("content")
                .authority(authority)
                .appendPath("jeu_table");
        return new CursorLoader(SupprimerJeuActivity.this, builder.build(),
                new String[]{"_id", "theme"},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapterList.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("loader reset", "ok");
        adapterList.swapCursor(null);
    }
}
