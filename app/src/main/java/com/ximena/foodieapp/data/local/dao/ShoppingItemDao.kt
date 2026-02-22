package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

// DAO de la lista de la compra
@Dao
interface ShoppingItemDao {

    // Primero los no marcados, luego los marcados, ambos en orden alfabético
    @Query("SELECT * FROM shopping_items WHERE userId = :userId ORDER BY isChecked ASC, name ASC")
    fun getAllShoppingItems(userId: String): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity)

    // Inserta varios a la vez, por ejemplo todos los ingredientes de una receta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingItemEntity>)

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)

    // Borra solo los ya tachados
    @Query("DELETE FROM shopping_items WHERE isChecked = 1 AND userId = :userId")
    suspend fun deleteCheckedItems(userId: String)

    // Vacía toda la lista
    @Query("DELETE FROM shopping_items WHERE userId = :userId")
    suspend fun deleteAllItems(userId: String)

    // Solo cambia el check sin tocar el resto
    @Query("UPDATE shopping_items SET isChecked = :checked WHERE id = :id AND userId = :userId")
    suspend fun updateCheckedStatus(userId: String, id: Int, checked: Boolean)
}