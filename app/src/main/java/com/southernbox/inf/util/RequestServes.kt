package com.southernbox.inf.util

import com.southernbox.inf.entity.ContentDTO
import com.southernbox.inf.entity.TabDTO

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by nanquan.lin on 2017/1/19 0019.
 * 网络请求服务器
 */

interface RequestServes {

    @GET("{url}")
    operator fun get(@Path("url") url: String): Call<String>

    @get:GET("tab.json")
    val tab: Call<List<TabDTO>>

    @get:GET("content.json")
    val content: Call<List<ContentDTO>>

}
