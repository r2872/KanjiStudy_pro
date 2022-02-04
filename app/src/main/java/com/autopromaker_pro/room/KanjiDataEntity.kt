package com.autopromaker_pro.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kanjiData")
data class KanjiDataEntity(
    var classNum: String,
    var title: String,
    var isChecked: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
