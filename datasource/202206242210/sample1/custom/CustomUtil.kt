package com.skyd.imomoe.model.impls.custom

import androidx.compose.runtime.NoLiveLiterals
import com.skyd.imomoe.model.interfaces.IUtil
import com.skyd.imomoe.util.Util

@NoLiveLiterals
class CustomUtil : IUtil {
    fun getDetailLinkByEpisodeLink(episodeUrl: String): String {
        return CustomConst.ANIME_DETAIL + episodeUrl
            .replaceFirst(CustomConst.ANIME_PLAY, "")
            .replaceFirst(Regex("-.*\\.html"), "") + Util.getWebsiteLinkSuffix()
    }
}
