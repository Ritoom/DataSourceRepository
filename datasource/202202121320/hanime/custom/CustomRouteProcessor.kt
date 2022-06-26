package com.skyd.imomoe.model.impls.custom

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.skyd.imomoe.model.interfaces.IRouteProcessor
import com.skyd.imomoe.util.showToast
import com.skyd.imomoe.view.activity.*
import java.net.URLDecoder

class CustomRouteProcessor : IRouteProcessor {
    override fun process(context: Context, actionUrl: String): Boolean {
        return false
    }

    override fun process(activity: Activity, actionUrl: String): Boolean {
        val decodeUrl = URLDecoder.decode(actionUrl, "UTF-8")
        val const = CustomConst()
        var solved = true
        when {
            decodeUrl.startsWith(const.actionUrl.ANIME_DETAIL()) -> {     //番剧封面点击进入
                activity.startActivity(
                    Intent(activity, AnimeDetailActivity::class.java)
                        .putExtra("partUrl", actionUrl)
                )
            }
            decodeUrl.startsWith(const.actionUrl.ANIME_PLAY()) -> {     //番剧每一集点击进入
                val playCode = actionUrl.replace("watch?","")
                if (playCode.isNotEmpty()) {
                    var detailPartUrl =
                        actionUrl.substringAfter(const.actionUrl.ANIME_DETAIL(), "")
                    detailPartUrl = const.actionUrl.ANIME_DETAIL() + detailPartUrl
                    activity.startActivity(
                        Intent(activity, PlayActivity::class.java)
                            .putExtra(
                                "partUrl",
                                actionUrl.substringBefore(const.actionUrl.ANIME_DETAIL())
                            )
                            .putExtra("detailPartUrl", detailPartUrl)
                    )
                } else {
                    "播放集数解析错误！".showToast()
                }
            }
            decodeUrl.startsWith(const.actionUrl.ANIME_SEARCH()) -> {     // 进入搜索页面
                decodeUrl.replace(const.actionUrl.ANIME_SEARCH(), "").let {
                    val keyWord = it.replaceFirst(Regex("/.*"), "")
                    val pageNumber = it.replaceFirst(Regex("($keyWord/)|($keyWord)"), "")
                    activity.startActivity(
                        Intent(activity, SearchActivity::class.java)
                            .putExtra("keyWord", keyWord)
                            .putExtra("pageNumber", pageNumber)
                    )
                }
            }
            decodeUrl.startsWith(const.actionUrl.ANIME_RANK()) -> {     // 排行榜
                activity.startActivity(Intent(activity, RankActivity::class.java))
            }
            decodeUrl.startsWith("/app${const.actionUrl.ANIME_CLASSIFY()}") -> {     //如进入分类页面
                val paramList = actionUrl.replace("/app${const.actionUrl.ANIME_CLASSIFY()}", "")
                    .replaceAfterLast("/","")
                val param = paramList.substring(0,paramList.length-1)
                if (param.isNotBlank()) {
                    activity.startActivity(
                        Intent(activity, ClassifyActivity::class.java)
                            .putExtra("partUrl", param)
                            .putExtra("classifyTabTitle", "")
                            .putExtra("classifyTitle", param)
                    )
                } else "跳转协议格式错误".showToast()
            }
            decodeUrl.startsWith(const.actionUrl.ANIME_CLASSIFY()) -> {     //如进入分类页面
                val paramList = actionUrl.replace(const.actionUrl.ANIME_CLASSIFY(), "")
                    .replaceAfterLast("/","")
                val param = paramList.substring(0,paramList.length-1)
                if (param.isNotBlank()) {
                    activity.startActivity(
                        Intent(activity, ClassifyActivity::class.java)
                            .putExtra("partUrl", param)
                            .putExtra("classifyTabTitle", "")
                            .putExtra("classifyTitle", param)
                    )
                } else "跳转协议格式错误".showToast()
            }
            else -> return false
        }
        return solved
    }
}
