package vn.edu.ptit.miniproject.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import vn.edu.ptit.miniproject.model.ItemSaved;
import vn.edu.ptit.miniproject.sql.DataBaseUtil;

/**
 * Created by QuangPC on 7/22/2017.
 */

public class FavoriteFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private ArrayList<ItemSaved> arrLiked = new ArrayList<>();
    private ListView lvFavorite;
    private TextView tvCheckF;
    private LikedAdapter adapter;
    private DataBaseUtil database;
    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = mainActivity.getDataBaseUtil();
        arrLiked = mainActivity.getArrFavo();
        adapter = new LikedAdapter(mainActivity,arrLiked);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_layout,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        lvFavorite = (ListView) mainActivity.findViewById(R.id.lvFavorite);
        tvCheckF = (TextView) mainActivity.findViewById(R.id.tvCheckF);
        lvFavorite.setAdapter(adapter);
        lvFavorite.setOnItemClickListener(this);
        lvFavorite.setOnItemLongClickListener(this);
        if(arrLiked.size()>0){
            tvCheckF.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popupMenu = new PopupMenu(mainActivity,view);
        popupMenu.getMenuInflater().inflate(R.menu.pop_up_f,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.btDelete){
                    ItemSaved itemSaved = arrLiked.get(position);
                    arrLiked.remove(itemSaved);
                    database.itemIsDisLike(itemSaved);
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        popupMenu.show();
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemSaved item = arrLiked.get(position);
        String str=item.getPathFile();
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra(NewsFragment.LINK,str);
        getContext().startActivity(intent);
    }

    public void update(ItemSaved itemSaved){
        for(ItemSaved i:arrLiked){
            if(i.getTitle().equals(itemSaved.getTitle())){
                Toast.makeText(getContext(),"This item has already liked",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getContext(),"Added to favorite list",Toast.LENGTH_SHORT).show();
        arrLiked.add(itemSaved);
        database.itemIsLike(itemSaved);
        adapter.notifyDataSetChanged();
    }
}
