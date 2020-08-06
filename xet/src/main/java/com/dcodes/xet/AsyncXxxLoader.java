package com.dcodes.xet;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsyncXxxLoader extends AsyncTask<String, Void, String> {

    private OnXxxCompleteListener onXxxCompleteListener;
    private XetModel xetModel;

    AsyncXxxLoader(OnXxxCompleteListener onXxxCompleteListener) {
        this.onXxxCompleteListener = onXxxCompleteListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Document document = Jsoup.connect(strings[0]).get();
            Elements scripts = document.select("script");

            Element playerScript = getPlayerScript (scripts);
            if (playerScript == null) return "No script found that contains player.";
            String[] patterns = new String[] {"Low\\('(.*?)'\\)", "High\\('(.*?)'\\)"};
            String hdUrl = null, sdUrl = null;

            Pattern pattern1 = Pattern.compile(patterns[0]);
            Pattern pattern2 = Pattern.compile(patterns[1]);
            Matcher matcher1 = pattern1.matcher(playerScript.toString());
            Matcher matcher2 = pattern2.matcher(playerScript.toString());
            while (matcher1.find()){
                sdUrl = matcher1.group(1);
            }
            while (matcher2.find()){
                hdUrl = matcher2.group(1);
            }

            xetModel = new XetModel(sdUrl, hdUrl);
        } catch (Exception e){
            return e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) onXxxCompleteListener.onError(s);
        else if (xetModel != null) onXxxCompleteListener.onSuccess(xetModel);
    }

    private Element getPlayerScript(Elements scripts) {
        String PLAYER = "new HTML5Player";
        for (Element e : scripts){
            if (e == null) continue;
            if (e.toString().contains(PLAYER)){
                return e;
            }
        }
        return null;
    }

    public interface OnXxxCompleteListener {
        void onSuccess (XetModel xetModel);
        void onError (String message);
    }
}
