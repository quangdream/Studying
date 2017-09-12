package vn.edu.ptit.miniproject.xml;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import vn.edu.ptit.miniproject.model.ItemNews;

/**
 * Created by QuangPC on 7/23/2017.
 */

public class XMLAsync extends AsyncTask<String, Void,ArrayList<ItemNews>> {
    private Handler handler;
    private ProgressDialog dialog;
    private Context context;
    public static final int WHAT_NEWS = 1;

    public XMLAsync(Context context,Handler handler) {
        this.context = context;
        this.handler = handler;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected ArrayList<ItemNews> doInBackground(String... params) {
        ArrayList<ItemNews> arrNews = new ArrayList<>();
        String link = params[0];
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLHandler xmlHandler = new XMLHandler();
            parser.parse(link,xmlHandler);
            arrNews = xmlHandler.getArrNews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrNews;
    }

    @Override
    protected void onPostExecute(ArrayList<ItemNews> itemNewses) {
        super.onPostExecute(itemNewses);
        Message message = new Message();
        message.what = WHAT_NEWS;
        message.obj = itemNewses;
        handler.sendMessage(message);
        if(itemNewses.size() >5) {
            dialog.dismiss();
        }
    }
}
