package com.autopromaker_pro.datas

import java.io.Serializable

data class KanjiClassData(
    val className: String,
    val detail: List<KanjiData>
): Serializable