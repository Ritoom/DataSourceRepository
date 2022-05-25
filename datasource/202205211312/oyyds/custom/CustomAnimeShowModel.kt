package com.skyd.imomoe.model.impls.custom

import com.google.gson.reflect.TypeToken
import com.skyd.imomoe.bean.*
import com.skyd.imomoe.model.interfaces.IAnimeShowModel
import com.skyd.imomoe.net.RetrofitManager
import java.util.*
import kotlin.collections.ArrayList


class CustomAnimeShowModel : IAnimeShowModel {
    override suspend fun getAnimeShowData(
        partUrl: String
    ): Pair<ArrayList<Any>, PageNumberBean?> {
        val myJsonParser: MyJsonParser<BaseBean> =
            MyJsonParser(typeElementName = "type", targetClass = BaseBean::class.java)
                .addClassType("AnimeCover1", AnimeCover1Bean::class.java)
                .addClassType("AnimeCover3", AnimeCover3Bean::class.java)
                .addClassType("AnimeCover4", AnimeCover4Bean::class.java)
                .addClassType("AnimeCover5", AnimeCover5Bean::class.java)
                .addClassType("Banner1", Banner1Bean::class.java)
                .addClassType("Header1", Header1Bean::class.java)
                .addClassType("AnimeCover1Bean", AnimeCover1Bean::class.java)
                .addClassType("AnimeCover3Bean", AnimeCover3Bean::class.java)
                .addClassType("AnimeCover4Bean", AnimeCover4Bean::class.java)
                .addClassType("AnimeCover5Bean", AnimeCover5Bean::class.java)
                .addClassType("Banner1Bean", Banner1Bean::class.java)
                .addClassType("Header1Bean", Header1Bean::class.java)
                .create()

        val raw = RetrofitManager
            .get()
            .create(DataSourceService::class.java)
            .getAnimeShowData(CustomConst.MAIN_URL + "/getAnimeShowData")

        val first = myJsonParser.fromJson<ArrayList<Any?>>(
            raw.data!!.first.toString(),
            object : TypeToken<ArrayList<BaseBean>>() {}.type
        )

        val second = raw.data.second

        return ArrayList(first.filterNotNull()) to second
    }
}