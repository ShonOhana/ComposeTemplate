package com.example.composetemplate.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.composetemplate.data.local.BaseDao
import com.example.composetemplate.data.models.local_models.Test
import kotlinx.coroutines.flow.Flow


@Dao
interface TestDao: BaseDao<Test> {
    @Query("DELETE FROM test")
    override fun deleteTable()
    @Query("SELECT * FROM test WHERE id =:id ")
    override fun getItem(id: Int): Flow<Test>
    @Query("SELECT * FROM test")
    override fun getItems(): Flow<List<Test>>
}