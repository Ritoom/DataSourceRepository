package com.skyd.imomoe.model.impls.custom

import com.skyd.imomoe.bean.*
import com.skyd.imomoe.model.interfaces.IEverydayAnimeWidgetModel
import com.skyd.imomoe.net.RetrofitManager

class CustomEverydayAnimeWidgetModel : IEverydayAnimeWidgetModel {
    override fun getEverydayAnimeData(): ArrayList<List<AnimeCover10Bean>> {
        val raw = RetrofitManager
            .get()
            .create(DataSourceService::class.java)
            .getEverydayAnimeDataWidget()
            .execute()
            .body()!!
        return raw.data!!.second
    }
}