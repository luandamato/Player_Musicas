package com.example.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.os.Binder;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.tutorialsface.customnotification.R;

public class NotificationService extends Service {

    Notification status;
    private Boolean tocando = true;
    private RemoteViews views;
    private RemoteViews bigViews;
    private final String LOG_TAG = "NotificationService";
    MusicaActivity main;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;


    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        NotificationService getService() {
            // Return this instance of MyService so clients can call public methods
            return NotificationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onStartCommand(Intent intent, int flags, int startId, MusicaActivity activity) {
        main = activity;
        onStartCommand(intent, flags, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            preencherViews();
            montarNotificacao();
        }
        else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            serviceCallbacks.voltarN();
        }
        else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            if (tocando){
                Toast.makeText(this, "PAUSE", Toast.LENGTH_SHORT).show();
                views.setImageViewResource(R.id.status_bar_play, R.drawable.img_play);
                bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.img_play);
//                Intent oIntent = new Intent(NotificationService.this, MusicaActivity.class);
//                oIntent.putExtra("play", "play");
//                startActivity(oIntent);
                serviceCallbacks.pause();
            }else{
                Toast.makeText(this, "PLAY", Toast.LENGTH_SHORT).show();
                views.setImageViewResource(R.id.status_bar_play, R.drawable.img_pause);
                bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.img_pause);
                serviceCallbacks.play();
            }
            tocando = !tocando;
        }
        else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, "Clicked Next");
            serviceCallbacks.avancarN();
        }
        else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        else{
            Intent resultIntent = new Intent(this, MusicaActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
        }
        atualizarNotificacao();
        return START_STICKY;
    }

    public void stop(){
        stopForeground(true);
        stopSelf();
    }

    private void preencherViews(){
        views = new RemoteViews(getPackageName(), R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);
    }

    private void montarNotificacao() {

        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));
        views.setImageViewBitmap(R.id.status_bar_icon,
                Constants.getDefaultAlbumArt(this));

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);
        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);
        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);
        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        //NOTIFICACAO PEQUENA
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        views.setTextViewText(R.id.status_bar_track_name, "Song Title");

        //NOTIFICACAO GRANDE

        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setViewVisibility(R.id.status_bar_track_name, View.VISIBLE);
        bigViews.setTextViewText(R.id.status_bar_track_name, "Musica");
//        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
//        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");





    }


    private void atualizarNotificacao(){
        Intent notificationIntent = new Intent(this, MusicaActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.mantra_esferas;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

    public interface ServiceCallbacks {
        void pause();
        void play();
        void avancarN();
        void voltarN();
    }
}
