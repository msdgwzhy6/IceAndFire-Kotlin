package com.southernbox.inf.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.southernbox.inf.R
import com.southernbox.inf.activity.DetailActivity
import com.southernbox.inf.entity.ContentDTO
import com.southernbox.inf.util.ServerAPI
import kotlinx.android.synthetic.main.item_list.view.*

/**
 * Created by SouthernBox on 2017/6/25 0025.
 * 首页列表适配器
 */

class MainAdapter(content: Activity, list: List<ContentDTO>) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    var mContext: Context? = null
    var mActivity: Activity? = null
    var mList: List<ContentDTO>? = null

    init {
        mContext = content
        mActivity = content
        mList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val rootView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: MainAdapter.MyViewHolder, position: Int) {
        val content = mList!![position]

        holder.ivName!!.text = content.name
        holder.ivDesc!!.text = content.intro

        Glide
                .with(mContext)
                .load(ServerAPI.BASE_URL + content.img)
                .override(480, 270)
                .crossFade()
                .into(holder.ivImg)

        holder.itemView.setOnClickListener { _ -> onItemClick(content, holder.ivImg!!) }
    }

    override fun getItemCount(): Int = if (mList != null) (mList as List<ContentDTO>).size else 0

    fun onItemClick(content: ContentDTO, imageView: ImageView) {
        val pair = Pair(imageView as View, "tran_01")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, pair)
        DetailActivity.Companion.show(mContext!!, options, content.name!!, content.img!!, content.html!!)
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {

        var ivImg: ImageView? = null
        var ivName: TextView? = null
        var ivDesc: TextView? = null

        init {
            ivImg = itemView.iv_img
            ivName = itemView.tv_name
            ivDesc = itemView.tv_desc
        }

    }
}