package com.skyd.imomoe.model.impls.custom

import androidx.compose.runtime.NoLiveLiterals
import com.skyd.imomoe.BuildConfig
import com.skyd.imomoe.model.interfaces.IConst

@NoLiveLiterals
class CustomConst : IConst {
    companion object {
        val ANIME_RANK: String = "/ranklist/"
        val ANIME_PLAY: String = "/vp/"
        val ANIME_DETAIL: String = "/showp/"
        val ANIME_SEARCH: String = "/s_all"
        val ANIME_LINK: String = "/link/"
        val MAIN_URL: String by lazy { CustomConst().MAIN_URL }
    }

    override val MAIN_URL: String
        get() = BuildConfig.CUSTOM_DATA_MAIN_URL

    override fun versionName(): String = "1.2.3"

    override fun versionCode(): Int = 7

    override fun about(): String {
        return "数据来源：${MAIN_URL}"
    }
}
