package com.skyd.imomoe.model.impls.custom

import androidx.compose.runtime.NoLiveLiterals
import java.io.Serializable

@NoLiveLiterals
class DataWrapper<T>(
    val code: Int = 0,
    val message: String? = null,
    val data: T? = null
) : Serializable