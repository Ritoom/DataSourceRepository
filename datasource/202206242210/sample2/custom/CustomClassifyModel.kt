package com.skyd.imomoe.model.impls.custom

import android.app.Activity
import androidx.compose.runtime.NoLiveLiterals
import com.skyd.imomoe.adsapi.config.randomUserAgent
import com.skyd.imomoe.adsapi.websource.WebSource
import com.skyd.imomoe.bean.ClassifyBean
import com.skyd.imomoe.bean.PageNumberBean
import com.skyd.imomoe.model.util.JsoupUtil
import com.skyd.imomoe.model.interfaces.IClassifyModel
import com.skyd.imomoe.util.Util.toEncodedUrl
import org.jsoup.Jsoup
import org.jsoup.select.Elements

@NoLiveLiterals
class CustomClassifyModel : IClassifyModel {
    override fun setActivity(activity: Activity) {
    }

    override fun clearActivity() {
    }

    override suspend fun getClassifyData(
        partUrl: String
    ): Pair<ArrayList<Any>, PageNumberBean?> {
        val classifyList: ArrayList<Any> = ArrayList()
        var pageNumberBean: PageNumberBean? = null
        val url: String = (CustomConst.MAIN_URL + partUrl).toEncodedUrl()
        val document = JsoupUtil.getDocument(url)
        val areaElements: Elements = document.getElementsByClass("area")
        for (i in areaElements.indices) {
            val areaChildren: Elements = areaElements[i].children()
            for (j in areaChildren.indices) {
                when (areaChildren[j].className()) {
                    "fire l" -> {
                        val fireLChildren: Elements = areaChildren[j].children()
                        for (k in fireLChildren.indices) {
                            when (fireLChildren[k].className()) {
                                "lpic" -> {
                                    classifyList.addAll(
                                        CustomParseHtmlUtil.parseLpic(fireLChildren[k], url)
                                    )
                                }
                                "pages" -> {
                                    pageNumberBean =
                                        CustomParseHtmlUtil.parseNextPages(fireLChildren[k])
                                }
                            }
                        }
                    }
                }
            }
        }
        return Pair(classifyList, pageNumberBean)
    }

    override suspend fun getClassifyTabData(): ArrayList<ClassifyBean> {
        val html = WebSource.getWebSource(
            url = CustomConst.MAIN_URL + "/list/",
            userAgent = randomUserAgent()
        )
        val classifyTabList: ArrayList<ClassifyBean> = ArrayList()
        val document = Jsoup.parse(html)
        val fireLElements: Elements = document.getElementsByClass("area")
            .select("[class=fire l]")
        for (i in fireLElements.indices) {
            val areaChildren: Elements = fireLElements[i].children()
            for (j in areaChildren.indices) {
                when (areaChildren[j].className()) {
                    "search-list" -> {
                        classifyTabList.addAll(
                            CustomParseHtmlUtil.parseSearchList(areaChildren[j])
                        )
                    }
                }
            }
        }
        return classifyTabList
    }
}
