package com.southernbox.inf.adapter;

import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.southernbox.inf.R;
import com.southernbox.inf.activity.DetailActivity;
import com.southernbox.inf.activity.MainActivity;
import com.southernbox.inf.entity.ContentDTO;
import com.southernbox.inf.util.ServerAPI;

import java.util.List;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页列表适配器
 */

public class MainAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private MainActivity mainActivity;
    private List<ContentDTO> mList;

    public MainAdapter(Context context, List<ContentDTO> list) {
        this.mContext = context;
        this.mList = list;
        mainActivity = (MainActivity) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final ContentDTO content = mList.get(position);
        viewHolder.ivName.setText(content.getName());
        viewHolder.ivDesc.setText(content.getIntro());

        Glide
                .with(viewHolder.itemView.getContext())
                .load(ServerAPI.BASE_URL + content.getImg())
                .override(480, 270)
                .crossFade()
                .into(viewHolder.ivImg);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(content, viewHolder.ivImg);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void onItemClick(ContentDTO content, ImageView imageView) {
        Pair[] pairs = new Pair[]{new Pair(imageView, "tran_01")};
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(mainActivity, pairs);

        DetailActivity.Companion.show(
                mContext,
                options,
                content.getName(),
                content.getImg(),
                content.getHtml()
        );
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImg;
        private TextView ivName;
        private TextView ivDesc;

        MyViewHolder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
            ivName = (TextView) itemView.findViewById(R.id.tv_name);
            ivDesc = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
