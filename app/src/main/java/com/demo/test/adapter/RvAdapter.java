package com.demo.test.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.demo.test.R;
import com.demo.test.view.NineGridView;
import com.github.ielse.imagewatcher.ImageWatcher;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {
    private Context mContext;
    private List<List<String>> mLists;
    private ImageWatcher imageWatcher;
    private RequestOptions mRequestOptions;
    private DrawableTransitionOptions mDrawableTransitionOptions;

    public RvAdapter(Context mContext, List<List<String>> mLists, ImageWatcher imageWatcher) {
        this.mContext = mContext;
        this.mLists = mLists;
        this.imageWatcher = imageWatcher;
        this.mRequestOptions = new RequestOptions().centerCrop();
        this.mDrawableTransitionOptions = DrawableTransitionOptions.withCrossFade();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int rv_item_position) {

        holder.layout.setSingleImageSize(80, 120);
        if (mLists.get(rv_item_position) != null && mLists.get(rv_item_position).size() > 0) {
            holder.layout.setAdapter(new NineImageAdapter(mContext, mRequestOptions,
                    mDrawableTransitionOptions, mLists.get(rv_item_position)));
            holder.layout.setOnImageClickListener(new NineGridView.OnImageClickListener() {
                @Override
                public void onImageClick(int nine_gv_position, View view) {
                    imageWatcher.show((ImageView) view, holder.layout.getImageViews(), getImageUriList(mLists.get(rv_item_position)));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private NineGridView layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_nine);
        }
    }

    private List<Uri> getImageUriList(List<String> list) {
        List<Uri> imageUriList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (String str : list) {
                imageUriList.add(Uri.parse(str));
            }
        }
        return imageUriList;
    }
}