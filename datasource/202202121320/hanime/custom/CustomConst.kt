package com.skyd.imomoe.model.impls.custom

import com.skyd.imomoe.BuildConfig
import com.skyd.imomoe.model.interfaces.IConst

class CustomConst : IConst {
    override val actionUrl = ActionUrl()

    class ActionUrl : IConst.IActionUrl {
        override fun ANIME_RANK(): String = "search"
        override fun ANIME_PLAY(): String = "watch"
        override fun ANIME_DETAIL(): String = "/watch"
        override fun ANIME_SEARCH(): String = "/search"
        fun ANIME_CLASSIFY(): String = "/classify"
    }

    override fun MAIN_URL(): String = BuildConfig.MAIN_URL

    override fun versionName(): String = "0.0.1"

    override fun versionCode(): Int = 1

    override fun about(): String {
        return "默认数据源，不提供任何数据，请在设置界面手动选择自定义数据源！"
    }
}
