package com.skyd.imomoe.model.impls.custom

import androidx.compose.runtime.NoLiveLiterals
import com.skyd.imomoe.model.interfaces.IConst


@NoLiveLiterals
class CustomConst : IConst {
    companion object {
        val ANIME_RANK: String = "/top/"
        val ANIME_PLAY: String = "/getPlayData"
        val ANIME_DETAIL: String = "/getAnimeDetailData"
        val ANIME_SEARCH: String = "/search/"
        const val MAIN_URL: String = "http://my-api.oyyds.top/api/imomoe"
    }

    override val MAIN_URL: String
        get() = CustomConst.MAIN_URL

    override fun versionName(): String = "1.0.1"

    override fun versionCode(): Int = 2

    override fun about(): String {
        return "数据来源：${MAIN_URL}"
    }
}
