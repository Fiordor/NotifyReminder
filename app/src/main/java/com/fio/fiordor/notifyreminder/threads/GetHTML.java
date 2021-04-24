package com.fio.fiordor.notifyreminder.threads;

import android.util.Log;

import com.fio.fiordor.notifyreminder.CreateActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class GetHTML extends Thread {

    public static final String URL = "https://es.wikipedia.org/wiki/Especial:Aleatoria";
    //public static final String URL = "https://en.wikipedia.org/wiki/Special:Random";

    private WeakReference<CreateActivity> weakReference;
    private boolean save;

    public GetHTML(CreateActivity createActivity) {
        weakReference = new WeakReference<>(createActivity);
        save = true;
    }

    public void cancel() {
        save = false;
    }

    @Override
    public void run() {

        String outputText = null;

        if (weakReference.get() == null) return;

        try {

            Document doc = Jsoup.connect(URL).get();

            String url = doc.location();

            Element elementTitle = doc.getElementById("firstHeading");
            String title = elementTitle.text();

            Element elementContentText = doc.getElementById("mw-content-text");
            Elements elementsContextText = elementContentText.getElementsByClass("mw-parser-output");
            Element elementParseOutput = elementsContextText.first();
            Elements ps = elementParseOutput.getElementsByTag("p");
            Element p = ps.first();
            String text = p.text();

            outputText = String.format("%s\n%s\n%s", url, title, text);

        } catch (IOException e) {
            return;
        }

        if (weakReference.get() == null) return;

        if (save && outputText != null) {
            weakReference.get().setReplyText(outputText);
        }
    }
}
