package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items WHERE userId = :userId ORDER BY isChecked ASC, name ASC")
    fun getAllShoppingItems(userId: String): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingItemEntity>)

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)

    @Query("DELETE FROM shopping_items WHERE isChecked = 1 AND userId = :userId")
    suspend fun deleteCheckedItems(userId: String)

    @Query("DELETE FROM shopping_items WHERE userId = :userId")
    suspend fun deleteAllItems(userId: String)

    @Query("UPDATE shopping_items SET isChecked = :checked WHERE id = :id AND userId = :userId")
    suspend fun updateCheckedStatus(userId: String, id: Int, checked: Boolean)
}