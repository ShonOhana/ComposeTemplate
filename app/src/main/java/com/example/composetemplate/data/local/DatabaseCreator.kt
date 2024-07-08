package com.example.composetemplate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.composetemplate.data.local.dao.TestDao
import com.example.composetemplate.data.local.db_models.Test

/**
 * This interface will provide us the database we create.
 * @property getDatabase will return the class we will implement,
 * to get us create databases in generic way and not only for using Room library
 */
interface DatabaseCreator<T> {
    /**
     * @param param1 is use to get context to create database, because android need context.
     *
     * Note: if we need another way to create DB without specific param we can create here another fun.
     * if we use in abstract class we don't even need to implement all method

     */
    fun getDatabase(): T

}

/**
 * This class is to create our database using the Room library.
 */
@Database(
    entities = [
        Test::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase(), DatabaseCreator<AppDatabase> {

    abstract fun testDao(): TestDao

    override fun getDatabase(): AppDatabase = this

    companion object {
        @Volatile
        var instance: AppDatabase? = null

         fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "app_db")
                .fallbackToDestructiveMigration()
                .build()
    }

}