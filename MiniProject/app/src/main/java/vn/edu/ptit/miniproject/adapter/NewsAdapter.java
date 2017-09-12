package vn.edu.ptit.miniproject.adapter;

import android.content.Context;
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

import vn.edu.ptit.miniproject.R;
import vn.edu.ptit.miniproject.model.ItemNews;

/**
 * Created by QuangPC on 7/23/2017.
 */

public class NewsAdapter extends ArrayAdapter<ItemNews> {
    private ArrayList<ItemNews> arrNews;
    private LayoutInflater inflater;

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<ItemNews> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        arrNews = objects;
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

        final ItemNews itemNews = arrNews.get(position);
        viewHolder.tvTittle.setText(itemNews.getTittle());
        viewHolder.tvDes.setText(itemNews.getDes());
        viewHolder.tvPubDate.setText(itemNews.getPubDate());
        Glide.with(getContext()).load(itemNews.getImage()).placeholder(R.mipmap.ic_launcher)
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
