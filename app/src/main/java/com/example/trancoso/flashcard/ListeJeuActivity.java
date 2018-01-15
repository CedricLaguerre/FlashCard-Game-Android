package com.example.trancoso.flashcard;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ListeJeuActivity extends AppCompatActivity
        implements ListeJeuFragment.OnFragmentInteractionListener,
        CartesFragment.OnFragmentInteractionListener{

    private FragmentManager fManager;
    private ListeJeuFragment mJeuFragment;
    private Toolbar toolbar;
    private String mAuthority;
    private long mId = -1;
    private static final String LOG = "ListeJeuActivity";
    private static final String SAVE_ID = "saveId";
    private String mTagCartes = "cartes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_jeu);
        mAuthority = "com.example.trancoso.flashcard";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuthority = getResources().getString(R.string.authority);
        fManager = getSupportFragmentManager();
        Fragment fragment = fManager.findFragmentById(R.id.frame1);
        if (fragment == null) {
            fragment = ListeJeuFragment.newInstance(mAuthority,
                    "jeu_table", "theme");
            fManager.beginTransaction().add(R.id.frame1, fragment)
                    .commit();

        }

        /*if (savedInstanceState != null) {
            mId = savedInstanceState.getLong(SAVE_ID, -1L);
        }
        *//* si l'activite recreee alors enlever les anciens fragments et refaire tout *//*
        if (savedInstanceState != null) {
            FragmentManager fm = getSupportFragmentManager();
            getSupportFragmentManager()
                    .popBackStack("debut", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        mJeuFragment = ListeJeuFragment.newInstance(mAuthority,
                "jeu_table", "theme");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            transaction.add(R.id.activity_liste_jeu, mJeuFragment, mTagCartes);
        } else {
            transaction.add(R.id.frame1, mJeuFragment, mTagCartes);
        }
        transaction.addToBackStack("debut").commit();*/

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
    public void onBackPressed() {
        mId = -1;
        Log.d(LOG, "onBackPressed");
        super.onBackPressed();
        /* si BackStack vide terminer l'activite */
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mId >= 0) {
            outState.putLong(SAVE_ID, mId);
        }
        super.onSaveInstanceState(outState);
    }

    public void onIdSelection(long id) {
        mId = id;
        CartesFragment cartesFragment = CartesFragment.newInstance(mAuthority,
                new String[]{"carte_table", "jeu"}, "recto", "verso", "id_jeu", id);

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack("debut",0);
        FragmentTransaction transaction = manager.beginTransaction();


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        /* remplacer le fragment avec le tag mTagAuteurs par
         * le fragment supprimerFragment  si l'orientation portrait
                        */

            transaction.replace(R.id.frame1, cartesFragment, mTagCartes);

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            /* ajouter si l'orientation landscape */
            transaction.add(R.id.frame2, cartesFragment, mTagCartes);
        }
        transaction.addToBackStack(null).commit();
    }
}
