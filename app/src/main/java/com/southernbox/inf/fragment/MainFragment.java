package com.southernbox.inf.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.southernbox.inf.R;
import com.southernbox.inf.adapter.MainAdapter;
import com.southernbox.inf.databinding.FragmentMainBinding;
import com.southernbox.inf.databinding.ItemListBinding;
import com.southernbox.inf.entity.ContentDTO;
import com.southernbox.inf.util.RequestServes;
import com.southernbox.inf.util.ServerAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页Fragment
 */

public class MainFragment extends Fragment {

    private Context mContext;
    private String firstType;
    private String secondType;
    private MainAdapter adapter;
    private List<ContentDTO> contentList = new ArrayList<>();
//    private Realm mRealm;
    private FragmentMainBinding binding;

    /**
     * 获取对应的首页Fragment
     *
     * @param firstType  一级分类
     * @param secondType 二级分类
     * @return 对应的Fragment
     */
    public static MainFragment newInstance(String firstType, String secondType) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("firstType", firstType);
        bundle.putString("secondType", secondType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle bundle = getArguments();
        firstType = bundle.getString("firstType");
        secondType = bundle.getString("secondType");
//        Realm.init(getContext());
//        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
//        mRealm = Realm.getInstance(realmConfig);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        binding = DataBindingUtil.bind(rootView);
        initView();
        showData();
        return rootView;
    }

    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MainAdapter(getActivity(), contentList);
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * 展示数据
     */
    public void showData() {
        if (isRemoving()) {
            return;
        }
        //从本地数据库获取
        contentList.clear();
        loadContentData();
//        final List<ContentDTO> cacheList = mRealm
//                .where(ContentDTO.class)
//                .equalTo("firstType", firstType)
//                .equalTo("secondType", secondType)
//                .findAll();
//        contentList.clear();
//        contentList.addAll(cacheList);
//        adapter.notifyDataSetChanged();
    }

    /**
     * 获取内容数据
     */
    private void loadContentData() {
        //初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为实体类的支持
                .addConverterFactory(GsonConverterFactory.create())
                .build();
       RequestServes requestServes = retrofit.create(RequestServes.class);
        Call<List<ContentDTO>> call = requestServes.getContent();
        call.enqueue(new Callback<List<ContentDTO>>() {
            @Override
            public void onResponse(Call<List<ContentDTO>> call,
                                   retrofit2.Response<List<ContentDTO>> response) {
//                List<ContentDTO> contentList = response.body();
                contentList.addAll(response.body());
                adapter.notifyDataSetChanged();
//                if (contentList != null) {
//                    //缓存到数据库
////                    mRealm.beginTransaction();
////                    mRealm.copyToRealmOrUpdate(contentList);
////                    mRealm.commitTransaction();
//                }
            }

            @Override
            public void onFailure(Call<List<ContentDTO>> call, Throwable t) {
//                List<ContentDTO> contentList = mRealm.where(ContentDTO.class).findAll();
//                //有缓存数据可正常跳转，没有则提示点击重试
//                if (contentList != null) {
//                    ToastUtil.show(mContext, "网络连接失败");
//                    loadContentComplete = true;
//                    goMainPage();
//                } else {
//                    netError();
//                }
            }
        });
    }

    public void refreshUI() {
        if (mContext != null) {
            Resources.Theme theme = mContext.getTheme();
            TypedValue pagerBackground = new TypedValue();
            theme.resolveAttribute(R.attr.pagerBackground, pagerBackground, true);
            TypedValue colorBackground = new TypedValue();
            theme.resolveAttribute(R.attr.colorBackground, colorBackground, true);
            TypedValue darkTextColor = new TypedValue();
            theme.resolveAttribute(R.attr.darkTextColor, darkTextColor, true);

            //更新背景颜色
            binding.flContent.setBackgroundResource(pagerBackground.resourceId);
            //更新Item的背景及字体颜色
            int childCount = binding.recyclerView.getChildCount();
            for (int position = 0; position < childCount; position++) {
                ViewGroup childView = (ViewGroup) binding.recyclerView.getChildAt(position);
                ItemListBinding itemListBinding = DataBindingUtil.bind(childView);
                itemListBinding.llContent.setBackgroundResource(colorBackground.resourceId);
                itemListBinding.tvName.setTextColor(
                        ContextCompat.getColor(mContext, darkTextColor.resourceId));
                itemListBinding.tvDesc.setTextColor(
                        ContextCompat.getColor(mContext, darkTextColor.resourceId));
            }
            //让 RecyclerView 缓存在 Pool 中的 Item 失效
            Class<RecyclerView> recyclerViewClass = RecyclerView.class;
            try {
                Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
                declaredField.setAccessible(true);
                Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName())
                        .getDeclaredMethod("clear");
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(declaredField.get(binding.recyclerView));
                RecyclerView.RecycledViewPool recycledViewPool = binding.recyclerView.getRecycledViewPool();
                recycledViewPool.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

















