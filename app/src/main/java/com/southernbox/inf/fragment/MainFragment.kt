package com.southernbox.inf.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.southernbox.inf.R
import com.southernbox.inf.adapter.MainAdapter
import com.southernbox.inf.entity.ContentDTO
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_list.view.*
import java.util.*

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页Fragment
 */

class MainFragment : Fragment() {

    private var mContext: Context? = null
    private var firstType: String? = null
    private var secondType: String? = null
    private var adapter: MainAdapter? = null
    private val contentList = ArrayList<ContentDTO>()
    private var mRealm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        val bundle = arguments
        firstType = bundle.getString("firstType")
        secondType = bundle.getString("secondType")
        Realm.init(context)
        val realmConfig = RealmConfiguration.Builder().build()
        mRealm = Realm.getInstance(realmConfig)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initView()
        showData()
    }

    private fun initView() {
        recycler_view.setLayoutManager(LinearLayoutManager(mContext))
        adapter = MainAdapter(activity, contentList)
        recycler_view.setAdapter(adapter)
    }

    /**
     * 展示数据
     */
    fun showData() {
        if (isRemoving) {
            return
        }
        //从本地数据库获取
        contentList.clear()
        val cacheList = mRealm!!
                .where(ContentDTO::class.java)
                .equalTo("firstType", firstType)
                .equalTo("secondType", secondType)
                .findAll()
        contentList.clear()
        contentList.addAll(cacheList)
        adapter!!.notifyDataSetChanged()
    }

    fun refreshUI() {
        if (mContext != null) {
            val theme = mContext!!.theme
            val pagerBackground = TypedValue()
            theme.resolveAttribute(R.attr.pagerBackground, pagerBackground, true)
            val colorBackground = TypedValue()
            theme.resolveAttribute(R.attr.colorBackground, colorBackground, true)
            val darkTextColor = TypedValue()
            theme.resolveAttribute(R.attr.darkTextColor, darkTextColor, true)

            //更新背景颜色
            fl_content.setBackgroundResource(pagerBackground.resourceId)
            //更新Item的背景及字体颜色
            val childCount = recycler_view.getChildCount()
            for (position in 0..childCount - 1) {
                val item = recycler_view.getChildAt(position)
                item.ll_content.setBackgroundResource(colorBackground.resourceId)
                item.tv_name.setTextColor(
                        ContextCompat.getColor(mContext!!, darkTextColor.resourceId))
                item.tv_desc.setTextColor(
                        ContextCompat.getColor(mContext!!, darkTextColor.resourceId))
            }
            //让 RecyclerView 缓存在 Pool 中的 Item 失效
            val recyclerViewClass = RecyclerView::class.java
            try {
                val declaredField = recyclerViewClass.getDeclaredField("mRecycler")
                declaredField.isAccessible = true
                val declaredMethod = Class.forName(RecyclerView.Recycler::class.java.name)
                        .getDeclaredMethod("clear")
                declaredMethod.isAccessible = true
                declaredMethod.invoke(declaredField.get(recycler_view))
                val recycledViewPool = recycler_view.getRecycledViewPool()
                recycledViewPool.clear()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    companion object {

        /**
         * 获取对应的首页Fragment

         * @param firstType  一级分类
         * *
         * @param secondType 二级分类
         * *
         * @return 对应的Fragment
         */
        fun newInstance(firstType: String?, secondType: String?): MainFragment {
            val fragment = MainFragment()
            val bundle = Bundle()
            bundle.putString("firstType", firstType)
            bundle.putString("secondType", secondType)
            fragment.arguments = bundle
            return fragment
        }
    }
}

















