package com.example.composetemplate.database.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * This interface will include all the necessary operations related to savin, updating and deleting data from DB.
 * Its main purpose is to create a contract we defined with any type of database we want to use.
 *
 * Another main purpose to implement this interface is to change easily the instance of the classes using DI.
 * make the code more readable and testability
 *
 */
interface DatabaseOperations<T> {
    suspend fun insert(item: T) /** Insert model to DB */
    suspend fun insert(items: List<T>) /** Insert list of model to DB */
    suspend fun update(item: T) /** Update the model in DB */
    suspend fun deleteItem(item: T) /** Delete model to DB */

}

/**
 * This interface is designed to implement all the methods related to writing and reading from the database.
 * Currently in this project we are using the Room library to implement the methods in Room.
 * If we want to change the database source (like MongoDB or any other database),
 * we will change the implementation only here in one place than in all the other Dao.
 *
 * We need to inherited from this interface in every dao we use!
 */
@Dao
interface BaseDao<T> : DatabaseOperations<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(item: T)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(items: List<T>)
    @Update
    override suspend fun update(item: T)
    @Delete
    override suspend fun deleteItem(item: T)

    /**
     * Methods to implement in other dao that need to be specify individually
     * example:
     * @sample deleteTable need to give the table name
     * @sample getItem need to give the table name and id who can be Int...
     * This way we can autoGenerate Primary key as Int and maintain uniformity
     * @sample getItems In this project we will fetch the data as flow to observe changes and maintain uniformity.
     * In this case if we want to change The flow to something else we change here and it will make us change in every places.
     *
     * Without to implement this method the project will not compile! (good practice)
     */
    fun deleteTable()
    fun getItem(id: Int): Flow<T>
    fun getItems(): Flow<List<T>>

}