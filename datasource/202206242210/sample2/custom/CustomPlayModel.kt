package com.skyd.imomoe.model.impls.custom

import android.app.Activity
import androidx.compose.runtime.NoLiveLiterals
import com.skyd.imomoe.adsapi.websource.WebSource
import com.skyd.imomoe.bean.*
import com.skyd.imomoe.model.util.JsoupUtil
import com.skyd.imomoe.model.interfaces.IPlayModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URLDecoder

@NoLiveLiterals
class CustomPlayModel : IPlayModel {
    override suspend fun getPlayData(
        partUrl: String,
        animeEpisodeDataBean: AnimeEpisodeDataBean
    ): Triple<ArrayList<Any>, ArrayList<AnimeEpisodeDataBean>, PlayBean> {
        val playBeanDataList: ArrayList<Any> = ArrayList()
        val episodesList: ArrayList<AnimeEpisodeDataBean> = ArrayList()
        val title = AnimeTitleBean("", "")
        val episode = AnimeEpisodeDataBean("", "")
        val playBean = PlayBean(
            "",
            title,
            episode,
            CustomUtil().getDetailLinkByEpisodeLink(partUrl),
            playBeanDataList
        )
        val url = CustomConst.MAIN_URL + partUrl
        val html = WebSource.getWebSource(url = url)
        val document = Jsoup.parse(html)
        val body = document.body()
        val bofangClass: Element = body.getElementsByClass("bofang")[0]!!
        var iframeSrc = bofangClass
            .select("iframe")[0]
            .attr("src")
        if (iframeSrc.startsWith("/yxsf/player")) {
            val videoUrl = URLDecoder.decode(
                iframeSrc.substringAfter("url=")
                    .substringBefore("&"), "UTF-8"
            )
            animeEpisodeDataBean.videoUrl = videoUrl
        } else if (iframeSrc.startsWith("/")) {
            iframeSrc = CustomConst.MAIN_URL + iframeSrc
            val iframe = Jsoup.parse(WebSource.getWebSource(iframeSrc))
            val videoUrl = iframe.body().getElementById("video")!!
                .select("video").attr("src")
            animeEpisodeDataBean.videoUrl = videoUrl
        }

        // 标题
        val tit = body.getElementsByClass("tit")
        tit.select("a").apply {
            title.title = get(size - 1).text()
            title.route = get(size - 1).attr("href")
        }
        episode.title = tit
            .select("span")
            .text()
            .replace("：", "")
        if (episode.title.startsWith(": ")) {
            episode.title = episode.title.replaceFirst(": ", "")
        }
        if (episode.title.endsWith(" BDRIP")) {
            episode.title = episode.title.replaceFirst(" BDRIP", "")
        }
        animeEpisodeDataBean.title = episode.title

        // 多个播放列表
        val menu0 = body.getElementsByClass("menu0")[0]
        val main0 = body.getElementsByClass("main0")[0]
        val li = menu0.select("li")
        var movurl = main0.select("[class=movurl]")
        if (movurl.size == 0)
            movurl = main0.select("[class=movurl movurl_pan]")
        for (l: Int in li.indices) {
            if (movurl[l].select("ul").select("li").size == 0) continue
            playBeanDataList.add(Header1Bean("", li[l].text()))

            val list = CustomParseHtmlUtil.parseMovurls(
                movurl[l],
                animeEpisodeDataBean
            )
            episodesList.addAll(list)
            playBeanDataList.add(HorizontalRecyclerView1Bean("", list))
        }

        // 推荐动漫/随机推荐header
        val listtit = body.getElementsByClass("listtit")
        playBeanDataList.add(
            Header1Bean(
                "",
                listtit.select("span").text(),
            )
        )

        // 推荐动漫/随机推荐内容
        val listUlLi = body
            .getElementsByClass("list")[0]
            .getElementsByTag("ul")[0]
            .children()
        listUlLi.forEach {
            val itemTitle = it.getElementsByTag("a").last()?.text().orEmpty()
            val itemPartUrl = it.getElementsByTag("a")[0].attr("href")
            val coverUrl = it.getElementsByClass("imgblock").attr("style")
                .substringAfter("background-image:url('").substringBefore("')")
            val itemEpisode = it.getElementsByClass("itemimgtext")[0].text()
            playBeanDataList.add(
                AnimeCover1Bean(
                    route = itemPartUrl,
                    url = CustomConst.MAIN_URL + itemPartUrl,
                    title = itemTitle,
                    cover = ImageBean("", coverUrl, url),
                    episode = itemEpisode
                )
            )
        }

        playBean.title = title
        playBean.episode = episode
        playBean.data = playBeanDataList
        return Triple(playBeanDataList, episodesList, playBean)
    }

    override fun clearActivity() {
    }

    override suspend fun playAnotherEpisode(
        partUrl: String
    ): AnimeEpisodeDataBean {
        val animeEpisodeDataBean = AnimeEpisodeDataBean("", "")
        val url = CustomConst.MAIN_URL + partUrl
        val document = Jsoup.parse(WebSource.getWebSource(url))
        val body = document.body()
        val bofangClass: Element = body.getElementsByClass("bofang")[0]!!
        var iframeSrc = bofangClass
            .select("iframe")[0]
            .attr("src")
        if (iframeSrc.startsWith("/yxsf/player")) {
            val videoUrl = URLDecoder.decode(
                iframeSrc.substringAfter("url=")
                    .substringBefore("&"), "UTF-8"
            )
            animeEpisodeDataBean.videoUrl = videoUrl
        } else if (iframeSrc.startsWith("/")) {
            iframeSrc = CustomConst.MAIN_URL + iframeSrc
            val iframe = Jsoup.parse(WebSource.getWebSource(iframeSrc))
            val videoUrl = iframe.body().getElementById("video")!!
                .select("video").attr("src")
            animeEpisodeDataBean.videoUrl = videoUrl
        }

        // 标题
        val tit = body.getElementsByClass("tit")
        var episodeTitle = tit
            .select("span")
            .text()
            .replace("：", "")
        if (episodeTitle.startsWith(": ")) {
            episodeTitle = episodeTitle.replaceFirst(": ", "")
        }
        if (episodeTitle.endsWith(" BDRIP")) {
            episodeTitle = episodeTitle.replaceFirst(" BDRIP", "")
        }
        animeEpisodeDataBean.title = episodeTitle
        return animeEpisodeDataBean
    }

    override suspend fun getAnimeCoverImageBean(partUrl: String): ImageBean? {
        try {
            val url = CustomConst.MAIN_URL + CustomUtil().getDetailLinkByEpisodeLink(partUrl)
            val document = JsoupUtil.getDocument(url)
            //番剧头部信息
            val area: Elements = document.getElementsByClass("area")
            for (i in area.indices) {
                val areaChildren = area[i].children()
                for (j in areaChildren.indices) {
                    when (areaChildren[j].className()) {
                        "fire l" -> {
                            val fireLChildren =
                                areaChildren[j].select("[class=fire l]")[0].children()
                            for (k in fireLChildren.indices) {
                                when (fireLChildren[k].className()) {
                                    "thumb l" -> {
                                        return ImageBean(
                                            "", CustomParseHtmlUtil.getCoverUrl(
                                                fireLChildren[k].select("img").attr("src"),
                                                url
                                            ), url
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun setActivity(activity: Activity) {
    }

    override suspend fun getAnimeDownloadUrl(partUrl: String): String? {
        val url = CustomConst.MAIN_URL + partUrl
        val document = Jsoup.parse(WebSource.getWebSource(url))
        val body = document.body()
        val bofangClass: Element = body.getElementsByClass("bofang")[0]!!
        var iframeSrc = bofangClass
            .select("iframe")[0]
            .attr("src")
        if (iframeSrc.startsWith("/yxsf/player")) {
            return URLDecoder.decode(
                iframeSrc.substringAfter("url=")
                    .substringBefore("&"), "UTF-8"
            )
        } else if (iframeSrc.startsWith("/")) {
            iframeSrc = CustomConst.MAIN_URL + iframeSrc
            val iframe = Jsoup.parse(WebSource.getWebSource(iframeSrc))
            return iframe.body().getElementById("video")!!
                .select("video").attr("src")
        }
        error("未获取到下载地址！")
    }
}