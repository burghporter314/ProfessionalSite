package com.example.dylanporter.rssfeedbydylanporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TimeStampURL <T> {
    T url;
    String[] linkArr;
    public TimeStampURL(T url) {
        this.url = url;
    }

    String returnURL() {
        return this.url.toString();
    }

    String getSource() throws IOException {
        URL tempAddress = new URL(this.url.toString());
        URLConnection socket = tempAddress.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();
        return a.toString();
    }

    int getNumStories(String url, String path) {

        int count = 0;
        while(url.contains(path)) {
            url = url.replaceFirst(path, "");
            count++;
        }

        return count;
    }


    String[] getStoriesArray(String url, int numStories,
                             String startBound, String endBound, int cutoff, int endCutoff) {
        String[] stories = new String[numStories];
        for(int i = 0; url.contains(startBound); i++) {
            int index = url.indexOf(startBound);
            if(numStories-i-1 == -1) break;
            String cutString = url.substring(index + cutoff, url.indexOf(endBound)-endCutoff);
            stories[i] = cutString.trim();
            url = url.replaceFirst(startBound, "");
            url = url.replaceFirst(endBound, "");
        }

        return stories;
    }



    String[] getStories() throws IOException {

        String url = getSource();

        int numStories = getNumStories(url, "CDATA");
        String[] headlines = null;

        if(numStories == 0) {
            numStories = getNumStories(url, "<title>");
            headlines = new String[numStories];
            headlines = getStoriesArray(url, numStories, "<title>", "</title>", 7, 0);
        } else {
            headlines = new String[numStories];
            headlines = getStoriesArray(url, numStories, "CDATA", "]]", 6, 0);
        }

        linkArr = new String[numStories];
        //CNN
        linkArr = getStoriesArray(url, numStories, "<feedburner:origLink>","</feedburner:origLink", 21, 0);
        //Everything other than CNN and ABC
        if(linkArr[3] == null)
            linkArr = getStoriesArray(url, numStories, "<link>","</link", 6, 0);
        //ABC
        if(linkArr[3] == null)
            linkArr = getStoriesArray(url, numStories, "<link>", "</link", 15, 3);

        return headlines;

    }

}