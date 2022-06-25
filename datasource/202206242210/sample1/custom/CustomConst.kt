package com.skyd.imomoe.model.impls.custom

import androidx.compose.runtime.NoLiveLiterals
import com.skyd.imomoe.BuildConfig
import com.skyd.imomoe.model.interfaces.IConst


@NoLiveLiterals
class CustomConst : IConst {
    companion object {
        val ANIME_RANK: String = "/top/"
        val ANIME_PLAY: String = "/v/"
        val ANIME_DETAIL: String = "/show/"
        val ANIME_SEARCH: String = "/search/"
        val MAIN_URL: String by lazy { CustomConst().MAIN_URL }
    }

    override val MAIN_URL: String
        get() = BuildConfig.MAIN_URL

    override fun versionName(): String = "1.2.3"

    override fun versionCode(): Int = 10

    override fun about(): String {
        return "数据来源：${MAIN_URL}"
    }
}
