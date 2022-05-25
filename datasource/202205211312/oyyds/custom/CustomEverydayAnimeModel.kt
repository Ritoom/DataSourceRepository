package com.skyd.imomoe.model.impls.custom

import com.google.gson.reflect.TypeToken
import com.skyd.imomoe.bean.*
import com.skyd.imomoe.model.util.JsoupUtil
import com.skyd.imomoe.model.interfaces.IEverydayAnimeModel
import com.skyd.imomoe.net.RetrofitManager
import org.jsoup.select.Elements

class CustomEverydayAnimeModel : IEverydayAnimeModel {

    override suspend fun getEverydayAnimeData(): Triple<ArrayList<TabBean>, ArrayList<List<Any>>, String> {
        val myJsonParser: MyJsonParser<BaseBean> =
            MyJsonParser(typeElementName = "type", targetClass = BaseBean::class.java)
                .addClassType("AnimeCover12", AnimeCover12Bean::class.java)
                .create()

        val raw = RetrofitManager
            .get()
            .create(DataSourceService::class.java)
            .getEverydayAnimeData()

        val first = raw.data!!.first

        @Suppress("UNCHECKED_CAST")
        val second = myJsonParser.fromJson<ArrayList<List<Any?>?>>(
            raw.data.second.toString(),
            object : TypeToken<ArrayList<List<BaseBean>>>() {}.type
        ).filterNotNull().onEach {
            it.filterNotNull()
        } as ArrayList<List<Any>>

        val third = raw.data.third.orEmpty()

        return Triple(first, ArrayList(second), third)
    }
}