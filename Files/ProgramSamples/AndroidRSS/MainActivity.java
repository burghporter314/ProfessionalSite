package com.example.dylanporter.rssfeedbydylanporter;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static Button btnIMG, btnSHOW, btnHidden;
    private static TextView lblDisplay;
    private static WebView webView;
    public static TimeStampURL globalTimeStamp;
    private static int trackerInput, SENTINAL = 0, trackerNum = -1, originalX = 0, tempNum = 0, webState = 0;
    private boolean shouldReload = false, shouldReturn, shouldLoad = false, isStory = true;

    private static String[] listURL = {"http://rss.cnn.com/rss/cnn_topstories.rss"
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSHOW = (Button) findViewById(R.id.btnSHOW);
        btnHidden = (Button) findViewById(R.id.btnHidden);
        btnIMG = (Button) findViewById(R.id.btnIMG);
        lblDisplay = (TextView) findViewById(R.id.lblDisplay);
        webView = (WebView) findViewById(R.id.webView);
        trackerInput = 0;
        //Set Strict Mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        Toast.makeText(this, "Note: loading stories takes time. - Dylan ", Toast.LENGTH_LONG).show();


        //Mover
        btnSHOW.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                String[] arr = {"CNN", "CNBC", "FOX", "YAHOO!", "CBS", "The Hill", "NASA", "Wash. Times", "NY Times",
                                "Reuters", "WS Journal", "Prof. Left"};
                //insert code

                int X = (int) event.getX();
                int Y = (int) event.getY();
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:

                        trackerNum = X;
                        originalX = X;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if ((trackerNum - X) < -300){
                            shouldReturn = true;
                            btnHidden.setVisibility(View.VISIBLE);
                            btnHidden.setText("Release to Return");
                            SENTINAL = 0;
                        } else if ((trackerNum - X) < 50) {
                            btnHidden.setText("Selection");
                            shouldLoad = false;
                            shouldReturn = false;
                        } else {

                            if(webState == 0) {
                                if(isStory)
                                    tempNum = (int) (originalX - X - 59) / 49;
                                else
                                    tempNum = (int) (originalX - X - 59) / 23;
                                btnHidden.setVisibility(View.VISIBLE);
                                shouldLoad = true;
                                if(!isStory)
                                    btnHidden.setText("Go to Story: " + (tempNum));
                                else {
                                    if(tempNum >= 11) {
                                        trackerInput = 11;
                                        btnHidden.setText("Open: " + arr[11]);
                                    } else {
                                        trackerInput = tempNum;
                                        btnHidden.setText("Open: " + arr[tempNum]);
                                    }
                                }
                            }
                        }


                        break;

                    case MotionEvent.ACTION_UP:

                        if(shouldReturn) {
                            if(webState == 1) {
                                webView.setVisibility(View.INVISIBLE);
                            }
                            else {
                                lblDisplay.setText("");
                                btnIMG.setVisibility(View.VISIBLE);
                                showButtons();
                                isStory = true;
                            }
                            webState = 0;
                        }
                        if(shouldLoad) {
                            btnIMG.setVisibility(View.INVISIBLE);
                            if(!isStory) {
                                openLink(tempNum);
                                webState = 1;
                            } else {
                                hideButtons();
                                try {
                                    printStories(listURL[trackerInput]);
                                    btnHidden.setText("");
                                    isStory = false;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        btnHidden.setText("Selection");
                }
                //insert code
                return true;
            }
        });
        Intent intent = new Intent(this,AppService.class);
        startService(intent);
    }

    //Load Link
    static void openLink(int tracker) {

        webView.setVisibility(View.VISIBLE);
        int[] shiftFactor = {5, 1, -1, 1, 1, 1, 1, 1, 1, 3, 1, 0};
        String[] linkArr = globalTimeStamp.linkArr;

        try {
            webView.loadUrl(linkArr[(tracker - shiftFactor[trackerInput])]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void showButtons() {

    }
    static void hideButtons() {
        btnSHOW.setVisibility(View.VISIBLE);
    }

    void printStories(String url) throws IOException {

        if(SENTINAL == 1) return;
        SENTINAL++;
        globalTimeStamp = new TimeStampURL(url);
        String[] headlines = globalTimeStamp.getStories();
        int length = 0, beginning = 0;
        String[] arr;
        for(String a : headlines) {
            if (!a.contains("http"))
                length++;
        }
        String displayString = "\n\n\n\n";
        for(String a : headlines) {

            arr = new String[length];
            a = a.replaceAll("&#x2018;", "\'");
            a = a.replaceAll("&#x2019;", "\'");
            a = a.replaceAll("&amp;", "&");
            a = a.replaceAll("&#039;", "\'");
            a = a.replaceAll("&quot;", "\"");

            if(!a.contains("http")) {
                arr[beginning++] = a;
                if(arr[beginning-1].equals("") || arr[beginning-1].equals(" ")||arr[beginning-1].equals(null))
                    arr[beginning-1] = "New Story";
                displayString += "(" + (beginning) + ")" + ": " + arr[beginning-1] + "\n\n";
            }
        }
        displayString += "\n\n\n\n\n\n\n\n\n\n\n\n";
        lblDisplay.setText(displayString);
    }
}
