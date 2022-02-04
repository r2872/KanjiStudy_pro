package com.autopromaker_pro.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface KanjiDataDao {

    @Insert
    fun insert(data: KanjiDataEntity)

    @Query("UPDATE kanjiData SET isChecked = CASE isChecked WHEN 'false' THEN 'true' WHEN 'true' THEN 'false' ELSE isChecked END where title = :inputTitle")
    fun update(inputTitle: String)

    @Query("SELECT * FROM kanjiData where classNum = :inputClass")
    fun getClass(inputClass: String): List<KanjiDataEntity>

    @Query("SELECT count(*) FROM kanjiData where classNum = :inputTitle and isChecked ='true'")
    fun getCheckCount(inputTitle: String): Int

    @Query("SELECT isChecked FROM kanjiData where title = :inputText")
    fun getChecked(inputText: String): String
}