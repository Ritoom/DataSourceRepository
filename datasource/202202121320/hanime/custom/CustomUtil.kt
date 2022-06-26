package com.skyd.imomoe.model.impls.custom

import com.skyd.imomoe.model.interfaces.IUtil

class CustomUtil : IUtil {
    override fun getDetailLinkByEpisodeLink(episodeUrl: String): String {
        return "/$episodeUrl"
    }
}
