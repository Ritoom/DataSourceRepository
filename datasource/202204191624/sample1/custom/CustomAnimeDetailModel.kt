package com.skyd.imomoe.model.impls.custom

import com.skyd.imomoe.bean.*
import com.skyd.imomoe.config.Api
import com.skyd.imomoe.model.util.JsoupUtil
import com.skyd.imomoe.model.interfaces.IAnimeDetailModel
import com.skyd.imomoe.route.Router.buildRouteUri
import com.skyd.imomoe.route.processor.ClassifyActivityProcessor
import org.jsoup.select.Elements

class CustomAnimeDetailModel : IAnimeDetailModel {
    override suspend fun getAnimeDetailData(
        partUrl: String
    ): Triple<ImageBean, String, ArrayList<Any>> {
        val animeDetailList: ArrayList<Any> = ArrayList()
        val cover = ImageBean("", "", "")
        var title = ""
        val url = Api.MAIN_URL + partUrl
        val document = JsoupUtil.getDocument(url)
        //番剧头部信息
        val area: Elements = document.getElementsByClass("area")
        for (i in area.indices) {
            val areaChildren = area[i].children()
            for (j in areaChildren.indices) {
                when (areaChildren[j].className()) {
                    "fire l" -> {
                        var alias = ""
                        var info = ""
                        var year = ""
                        var index = ""
                        var animeArea = ""
                        val animeType: MutableList<AnimeTypeBean> = ArrayList()
                        val tag: MutableList<AnimeTypeBean> = ArrayList()

                        val fireLChildren =
                            areaChildren[j].select("[class=fire l]")[0].children()
                        for (k in fireLChildren.indices) {
                            when (fireLChildren[k].className()) {
                                "thumb l" -> {
                                    cover.url = CustomParseHtmlUtil.getCoverUrl(
                                        fireLChildren[k].select("img").attr("src"), url
                                    )
                                    cover.referer = url
                                }
                                "rate r" -> {
                                    val rateR = fireLChildren[k]
                                    title = rateR.select("h1").text()
                                    val sinfo: Elements = rateR.select("[class=sinfo]")
                                    val span: Elements = sinfo.select("span")
                                    val p: Elements = sinfo.select("p")
                                    if (p.size == 1) {
                                        alias = p[0].text()
                                    } else if (p.size == 2) {
                                        alias = p[0].text()
                                        info = p[1].text()
                                    }
                                    year = span[0].text()
                                    animeArea = span[1].select("a").text()
                                    index = span[3].select("a").text()
                                    val typeElements: Elements = span[2].select("a")
                                    for (l in typeElements.indices) {
                                        animeType.add(
                                            AnimeTypeBean(
                                                ClassifyActivityProcessor.route.buildRouteUri {
                                                    appendQueryParameter(
                                                        "partUrl",
                                                        typeElements[l].attr("href")
                                                    )
                                                    appendQueryParameter(
                                                        "classifyTitle",
                                                        typeElements[l].text()
                                                    )
                                                }.toString(),
                                                Api.MAIN_URL + typeElements[l].attr("href"),
                                                typeElements[l].text()
                                            )
                                        )
                                    }
                                    val tagElements: Elements = span[4].select("a")
                                    for (l in tagElements.indices) {
                                        tag.add(
                                            AnimeTypeBean(
                                                ClassifyActivityProcessor.route.buildRouteUri {
                                                    appendQueryParameter(
                                                        "partUrl",
                                                        tagElements[l].attr("href")
                                                    )
                                                    appendQueryParameter(
                                                        "classifyTitle",
                                                        tagElements[l].text()
                                                    )
                                                }.toString(),
                                                Api.MAIN_URL + tagElements[l].attr("href"),
                                                tagElements[l].text()
                                            )
                                        )
                                    }
                                }
                                "tabs", "tabs noshow" -> {     //播放列表+header
                                    val menu0 = fireLChildren[k].select("[class=menu0]")[0]
                                    val main0 = fireLChildren[k].select("[class=main0]")[0]
                                    val li = menu0.select("li")
                                    var movurl = main0.select("[class=movurl]")
                                    if (movurl.size == 0)
                                        movurl = main0.select("[class=movurl movurl_pan]")
                                    for (l: Int in li.indices) {
                                        if (movurl[l].select("ul").select("li").size == 0) continue
                                        animeDetailList.add(Header1Bean("", li[l].text()))

                                        animeDetailList.add(
                                            HorizontalRecyclerView1Bean(
                                                "",
                                                CustomParseHtmlUtil.parseMovurls(movurl[l])
                                            )
                                        )
                                    }
                                }
                                "botit" -> {     //其它header
                                    animeDetailList.add(
                                        Header1Bean(
                                            "",
                                            CustomParseHtmlUtil.parseBotit(fireLChildren[k])
                                        )
                                    )
                                }
                                "dtit" -> {     //其它header
                                    animeDetailList.add(
                                        Header1Bean(
                                            "",
                                            CustomParseHtmlUtil.parseDtit(fireLChildren[k])
                                        )
                                    )
                                }
                                "info" -> {         //动漫介绍
                                    animeDetailList.add(
                                        AnimeDescribe1Bean(
                                            "",
                                            fireLChildren[k].select("[class=info]").text()
                                        )
                                    )
                                }
                                "img" -> {         //系列动漫推荐
                                    animeDetailList.addAll(
                                        CustomParseHtmlUtil.parseImg(fireLChildren[k], url)
                                    )
                                }
                            }
                        }
                        animeDetailList.add(
                            0,
                            AnimeInfo1Bean(
                                "",
                                title,
                                ImageBean("", cover.url, url),
                                alias,
                                animeArea,
                                year,
                                index,
                                animeType,
                                tag,
                                info
                            )
                        )
                    }
                }
            }
        }
        return Triple(cover, title, animeDetailList)
    }
}