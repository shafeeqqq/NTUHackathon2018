package com.example.shaf.ntuhackathon2018;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shaf.ntuhackathon2018.Utils.ImageUtils;
import com.example.shaf.ntuhackathon2018.Utils.ImageViewHolder;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

    private List<String> imageslist;

    private Context context;

    public RecyclerAdapter(Context context){
        this.context = context;
    }



    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position){

        String currentImagePath = imageslist.get(position);
        holder.imageView.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(currentImagePath, 150, 180));
        holder.imageTitle.setText("" + position);
    }


    public void setImageslist(List<String> imageslist) {
        this.imageslist = imageslist;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount(){
        return imageslist.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageTitle;

        public ImageViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.album);
            imageTitle = itemView.findViewById(R.id.album_title);

        }
    }
}
