package vn.edu.ptit.miniproject.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.ptit.miniproject.R;
import vn.edu.ptit.miniproject.activity.MainActivity;
import vn.edu.ptit.miniproject.activity.WebViewActivity;
import vn.edu.ptit.miniproject.adapter.LikedAdapter;
import vn.edu.ptit.miniproject.adapter.SavedAdapter;
import vn.edu.ptit.miniproject.model.ItemNews;
import vn.edu.ptit.miniproject.model.ItemSaved;
import vn.edu.ptit.miniproject.sql.DataBaseUtil;

/**
 * Created by QuangPC on 7/22/2017.
 */

public class SavedFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private ArrayList<ItemSaved> arrSaved = new ArrayList<>();
    private ListView listView;
    private TextView tvCheck;
    private SavedAdapter adapter;
    private DataBaseUtil database;
    private MainActivity mainActivity;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = mainActivity.getDataBaseUtil();
        arrSaved = mainActivity.getArrSaved();
        adapter = new SavedAdapter(mainActivity,arrSaved);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_layout,container,false);


    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();

    }


    private void initViews() {
        listView = (ListView) getActivity().findViewById(R.id.lvSaved);
        tvCheck = (TextView) getActivity().findViewById(R.id.tvCheckS);

        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        if(arrSaved.size()>0){
            tvCheck.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popup = new PopupMenu(getActivity(),view);
        popup.getMenuInflater().inflate(R.menu.pop_up,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.btLike){
//                    Toast.makeText(getContext(),"Liked",Toast.LENGTH_SHORT).show();
                    ItemSaved itemSaved = arrSaved.get(position);
                    FavoriteFragment fF = mainActivity.getPagerAdapter().getFavoriteFragment();
                    fF.update(itemSaved);
                }else {
                    Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                    ItemSaved itemSaved = arrSaved.get(position);
                    database.delete(itemSaved.getId());
                    arrSaved.remove(itemSaved);
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        popup.show();
        return true;
    }


    public ArrayList<ItemSaved> getArrSaved() {
        return arrSaved;
    }

    public SavedAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemSaved item = arrSaved.get(position);
        String str=item.getPathFile();
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra(NewsFragment.LINK,str);
        getContext().startActivity(intent);
    }

    public void download(ItemSaved itemSaved){
        for(ItemSaved i:arrSaved){
            if(i.getTitle().equals(itemSaved.getTitle())){
                Toast.makeText(getContext(),"This item was downloaded!!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        arrSaved.add(itemSaved);
        database.insert(itemSaved);
        adapter.notifyDataSetChanged();
    }

}
