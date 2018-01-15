package com.example.trancoso.flashcard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button bJouer;
    Button bParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bJouer = (Button)findViewById(R.id.jouerB);
        bParam = (Button)findViewById(R.id.paramB);
        Intent intent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);


        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("FLASH CARD")
                        .setContentText("Application en cours...")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.icone)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                R.drawable.icone))
                        .build();
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(1, notification);
    }

    public void onClick(View view) {
        Intent i;
        switch(view.getId()){
            case R.id.jouerB:
                i = new Intent(this, JouerActivity.class);
                this.startActivity(i);
                break;
            case R.id.paramB:
                i = new Intent(this, ParamActivity.class);
                this.startActivity(i);
                break;
            case R.id.downloadB:
                i = new Intent(this, DownloadManagerActivity.class);
                this.startActivity(i);
                break;
        }
    }
}
