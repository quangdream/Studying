package vn.edu.ptit.miniproject.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ptit.miniproject.R;
import vn.edu.ptit.miniproject.model.ItemNews;
import vn.edu.ptit.miniproject.model.ItemSaved;

/**
 * Created by QuangPC on 7/24/2017.
 */

public class SavedAdapter extends ArrayAdapter<ItemSaved> {
    private ArrayList<ItemSaved> arrSaved;
    private LayoutInflater inflater;

    public SavedAdapter(@NonNull Context context, @NonNull ArrayList<ItemSaved> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        arrSaved = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (v==null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_news,parent,false);
            viewHolder.ivImage = (ImageView) v.findViewById(R.id.ivImage);
            viewHolder.tvTittle = (TextView) v.findViewById(R.id.tvTitte);
            viewHolder.tvDes = (TextView) v.findViewById(R.id.tvDes);
            viewHolder.tvPubDate = (TextView) v.findViewById(R.id.tvpubDate);
            v.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) v.getTag();
        }

        final  ItemSaved item = arrSaved.get(position);
        viewHolder.tvTittle.setText(item.getTitle());
        viewHolder.tvDes.setText(item.getDesc());
        viewHolder.tvPubDate.setText(item.getPubDate());
        Glide.with(getContext()).load(item.getImage()).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round).into(viewHolder.ivImage);
        return v;
    }
    class ViewHolder{
        ImageView ivImage;
        TextView tvTittle;
        TextView tvDes;
        TextView tvPubDate;

    }
}
