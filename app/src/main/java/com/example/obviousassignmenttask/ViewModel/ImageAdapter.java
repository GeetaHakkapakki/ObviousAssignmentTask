package com.example.obviousassignmenttask.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.obviousassignmenttask.Model.ImageModel;
import com.example.obviousassignmenttask.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    Context context;
    List<ImageModel> imageList;
    String imageUrl;

    public ImageAdapter(Context mcontext, List<ImageModel> imageLists) {
        context = mcontext;
        imageList = imageLists;
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder= new ViewHolder();
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_imageview, parent, false);

            viewHolder.img = convertView.findViewById(R.id.imageView);
            viewHolder.txt_title = convertView.findViewById(R.id.txt_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        imageUrl = imageList.get(position).getUrl();
        viewHolder.txt_title.setText(imageList.get(position).getTitle());
        Glide.with(context).load(imageList.get(position).getUrl()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(viewHolder.img);
        return convertView;
    }
    public static class ViewHolder{
        private ImageView img;
        private TextView txt_title;
    }
}
