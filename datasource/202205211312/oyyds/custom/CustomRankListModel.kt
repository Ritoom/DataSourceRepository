package com.skyd.imomoe.model.impls.custom

import androidx.compose.runtime.NoLiveLiterals
import com.skyd.imomoe.bean.PageNumberBean
import com.skyd.imomoe.model.interfaces.IRankListModel

@NoLiveLiterals
class CustomRankListModel : IRankListModel {
    override suspend fun getRankListData(partUrl: String): Pair<MutableList<Any>, PageNumberBean?> {
        return Pair(ArrayList(), null)
    }
}
