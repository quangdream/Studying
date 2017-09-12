package vn.edu.ptit.miniproject.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import vn.edu.ptit.miniproject.R;
import vn.edu.ptit.miniproject.activity.MainActivity;
import vn.edu.ptit.miniproject.activity.WebViewActivity;
import vn.edu.ptit.miniproject.adapter.NewsAdapter;
import vn.edu.ptit.miniproject.adapter.SavedAdapter;
import vn.edu.ptit.miniproject.model.ItemNews;
import vn.edu.ptit.miniproject.model.ItemSaved;
import vn.edu.ptit.miniproject.sql.DataBaseUtil;
import vn.edu.ptit.miniproject.xml.XMLAsync;

/**
 * Created by QuangPC on 7/22/2017.
 */

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static final String LINK = "link";
    public static final int WHAT_DOWN = 2;
    public static final String PATH= Environment.getExternalStorageDirectory().getPath()+"/FilterNews"+"/";
    private ListView lvNews;
  //  private Dialog dialog;
    private TextView tvCheck;
    private NewsAdapter newsAdapter;
    private ArrayList<ItemNews> arrNews = new ArrayList<>();
    public  static final String URLS = "https://news.google.de/news/feeds?pz=1&cf=vi_vn&ned=vi_vn&hl=vi_vn&q=";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_layout,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
    }


    private void initViews() {
        lvNews = (ListView) getActivity().findViewById(R.id.lvNews);
        newsAdapter = new NewsAdapter(getContext(),arrNews);
        lvNews.setAdapter(newsAdapter);
        lvNews.setOnItemClickListener(this);
        lvNews.setOnItemLongClickListener(this);
        tvCheck = (TextView) getActivity().findViewById(R.id.tvCheck);
        if(arrNews.size() >0){
            tvCheck.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                 //   dialog.show();
                    arrNews.clear();
                    URLEncoder.encode(query,"utf-8");
                    String link = URLS+query;
                    XMLAsync xmlAsync = new XMLAsync(getContext(),handler);
                    xmlAsync.execute(link);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == XMLAsync.WHAT_NEWS){
                arrNews.addAll((ArrayList<ItemNews>) msg.obj);
                if(arrNews.size()>=5){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                 //   dialog.dismiss();
                }
                if(arrNews.size() > 0){
                    tvCheck.setVisibility(View.GONE);
                }
                newsAdapter.notifyDataSetChanged();
            }else
            if(msg.what == WHAT_DOWN){
                ItemSaved itemSaved = (ItemSaved) msg.obj;
                MainActivity mainActivity = (MainActivity) getActivity();
                SavedFragment sF = mainActivity.getPagerAdapter().getSavedFragment();
                sF.download(itemSaved);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemNews itemNews = arrNews.get(position);
        String link = itemNews.getLink();
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(LINK,link);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DownloadAsync downAsync = new DownloadAsync(getContext(),handler);
        ItemNews itenNews = arrNews.get(position);
        downAsync.execute(itenNews);
        return true;
    }


    private class DownloadAsync extends AsyncTask<ItemNews,Void,ItemSaved>{
        private Context context;
        private Handler handler;
        private ProgressDialog progressDialog;

        public DownloadAsync( Context context,Handler handler) {
            this.context = context;
            this.handler = handler;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected ItemSaved doInBackground(ItemNews... params) {
            InputStream input = null;
            FileOutputStream output = null;
            HttpURLConnection connection = null;
            ItemNews itemNews = params[0];
            ItemSaved itemSaved = new ItemSaved(itemNews.getTittle()
                                                ,itemNews.getDes()
                                                ,itemNews.getPubDate()
                                                ,itemNews.getImage(),itemNews.getLink());
            String ss[] = itemNews.getLink().split("/");
            File file = new File(PATH+ss[ss.length-1]);
            if(file.exists()==false){
                File parent = file.getParentFile();
                parent.mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try{
                URL url = new URL(itemNews.getLink());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                input = connection.getInputStream();
                output = new FileOutputStream(file);

                byte[] b = new byte[1024];
                int count = input.read(b);
                while (count != -1){
                    if(isCancelled()){
                        input.close();
                        return null;
                    }
                    output.write(b,0,count);
                    count = input.read(b);
                }
                input.close();
                output.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            return itemSaved;
        }

        @Override
        protected void onPostExecute(ItemSaved itemSaved) {
            super.onPostExecute(itemSaved);
            Message msg = new Message();
            msg.what = WHAT_DOWN;
            msg.obj = itemSaved;
            handler.sendMessage(msg);
        }
    }


}
