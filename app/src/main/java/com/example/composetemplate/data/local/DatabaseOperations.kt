package com.example.composetemplate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.example.composetemplate.utils.exceptions.NoImplementationDbOperationException
import kotlinx.coroutines.flow.Flow

/**
 * This interface will include all the necessary operations related to saving, updating and deleting data from DB.
 * Its main purpose is to create a contract we defined with any type of database we want to use.
 *
 * Another main purpose to implement this interface is to change easily the instance of the classes using DI.
 * make the code more readable and testability
 *
 * NOTICE: In this project we work with Flow so we will get our item in Flow.
 * if we would like to change to something else (LiveData for example), we will change the signature,
 * and that will make us to implement everything and to keep unity
 *
 * NOTE: every necessary methods we can add here.
 */
interface DatabaseOperations<T> {
    suspend fun upsert(item: T) /** Insert or update model to DB */
    suspend fun upsert(items: List<T>) /** Insert or update list of model to DB */
    suspend fun deleteItem(item: T) /** Delete model to DB */
    fun getItem(id: String): Flow<T> /** Get Single model in Flow to DB */
    fun getItems(): Flow<List<T>> /** Get models list in Flow to DB */
    fun deleteTable() /** Delete the all table */
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
    @Upsert
    override suspend fun upsert(item: T)
    @Upsert
    override suspend fun upsert(items: List<T>)
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
     * Without to implement this method the project will make exception! (good practice)
     */
    override fun deleteTable() {
        TODO(reason = NoImplementationDbOperationException().message)
    }
    override fun getItem(id: String): Flow<T> {
        TODO(reason = NoImplementationDbOperationException().message)
    }
    override fun getItems(): Flow<List<T>> {
        TODO(reason = NoImplementationDbOperationException().message)
    }

}