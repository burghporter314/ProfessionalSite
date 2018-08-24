package com.example.dylanporter.rssfeedbydylanporter;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dylan Porter on 8/16/2016.
 */
public class AppService extends Service {
    private int counter = 0, SENTINALSOUND = 0;
    @Override
    public void onCreate() {
        super.onCreate();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                counter++;
                if(counter > 15) {
                    try {
                        displayNotification();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    counter = 0;
                }
                //displayNotification(); IE TODO Late
            }
        },0,1000);
    }
    public void displayNotification() throws IOException {
        String[] urlName={"CNN","CNBC","FOX","YAHOO","CBS","The Hill",
                        "NASA","Wash. Times", "NY Times", "Reuters",
                        "WS Journal", "The Prof. Left"};
        String[] listURL = {"http://rss.cnn.com/rss/cnn_topstories.rss"
                ,"http://www.cnbc.com/id/100003114/device/rss/rss.html",
                "http://feeds.foxnews.com/foxnews/latest",
                "http://news.yahoo.com/rss/",
                "http://www.cbsnews.com/latest/rss/main",
                "http://thehill.com/rss/syndicator/19110",
                "http://www.nasa.gov/rss/dyn/breaking_news.rss",
                "http://www.washingtontimes.com/rss/headlines/news/inside-politics/",
                "http://rss.nytimes.com/services/xml/rss/nyt/Politics.xml",
                "http://feeds.reuters.com/Reuters/PoliticsNews",
                "http://www.wsj.com/xml/rss/3_7041.xml",
                "http://www.buzzsprout.com/229.rss"};
        int rand = 0 + (int)(Math.random() * 10), rand2;
        TimeStampURL objURL = new TimeStampURL(listURL[rand]);
        String[] arr = objURL.getStories();
        rand2 = 3 + (int)(Math.random()*(arr.length-3));
        String displayString = "Story " + (rand2+1) + " on " + urlName[rand] + ": ";
        arr[rand2] = arr[rand2].replaceAll("&#x2018;", "\'");
        arr[rand2] = arr[rand2].replaceAll("&#x2019;", "\'");
        arr[rand2] = arr[rand2].replaceAll("&amp;", "&");
        arr[rand2] = arr[rand2].replaceAll("&#039;", "\'");
        arr[rand2] = arr[rand2].replaceAll("&quot;", "\"");
        try {
            displayString += arr[rand2];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            displayString = "New Stories Available";
        }
        //Notifications Thanks to https://developer.android.com/training/notify-user/build-notification.html#action
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon).setContentTitle("Dylan's RSS Feed")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(displayString))
                .setContentText(displayString);
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this,
                        0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        //Alert Sound Thanks to http://stackoverflow.com/questions/18459122/play-sound-on-button-click-android
        if(new Sound().returnSound() == 1) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.notification_sound);
            if(SENTINALSOUND == 0) {
                mp.start();
                SENTINALSOUND = 1;
            }
        }
        //End Alert
        //End Notifications
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
