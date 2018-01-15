package com.example.trancoso.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ParamActivity extends AppCompatActivity {

    Button bAjouterJeu, bAfficherJeu, bAjouterCarte, bSupprimerJeu;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);
        bAjouterJeu = (Button)findViewById(R.id.ajouterJeu);
        bAfficherJeu = (Button)findViewById(R.id.afficherJeu);
        bAjouterCarte = (Button)findViewById(R.id.ajouterCarte);
        bSupprimerJeu = (Button)findViewById(R.id.supprimerJeu);

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

    public void onClick(View view) {
        Intent i;
        switch(view.getId()){

            case R.id.ajouterJeu:
                i = new Intent(this, AjouterJeuActivity.class);
                this.startActivity(i);
                break;

            case R.id.afficherJeu:
                i = new Intent(this, ListeJeuActivity.class);
                this.startActivity(i);
                break;
            case R.id.ajouterCarte:
                i = new Intent(this, AjouterCarteActivity.class);
                this.startActivity(i);
                break;
            case R.id.supprimerJeu:
                i = new Intent(this, SupprimerJeuActivity.class);
                this.startActivity(i);
                break;

        }
    }

}
