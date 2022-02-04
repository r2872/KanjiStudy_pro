package com.autopromaker_pro.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [KanjiDataEntity::class], version = 1)
abstract class KanjiDatabase : RoomDatabase() {
    abstract fun kanjiDao(): KanjiDataDao

    companion object {
        private var instance: KanjiDatabase? = null

        @Synchronized
        fun getInstance(context: Context): KanjiDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    KanjiDatabase::class.java,
                    "kanji-database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}