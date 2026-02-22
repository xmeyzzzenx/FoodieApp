package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.dao.ShoppingItemDao
import com.ximena.foodieapp.data.remote.mapper.toEntity
import com.ximena.foodieapp.data.remote.mapper.toMealPlan
import com.ximena.foodieapp.data.remote.mapper.toShoppingItem
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealPlanRepository @Inject constructor(
    private val mealPlanDao: MealPlanDao,
    private val shoppingItemDao: ShoppingItemDao
) {
    fun getMealPlanForWeek(weekYear: String): Flow<List<MealPlan>> =
        mealPlanDao.getMealPlanForWeek(weekYear).map { it.map { e -> e.toMealPlan() } }

    suspend fun addMealPlan(mealPlan: MealPlan) =
        mealPlanDao.insertMealPlan(mealPlan.toEntity())

    suspend fun removeMealPlan(id: Int) =
        mealPlanDao.deleteMealPlanById(id)

    fun getAllShoppingItems(): Flow<List<ShoppingItem>> =
        shoppingItemDao.getAllShoppingItems().map { it.map { e -> e.toShoppingItem() } }

    suspend fun addShoppingItem(item: ShoppingItem) =
        shoppingItemDao.insertItem(item.toEntity())

    suspend fun addShoppingItems(items: List<ShoppingItem>) =
        shoppingItemDao.insertItems(items.map { it.toEntity() })

    suspend fun toggleShoppingItem(id: Int, checked: Boolean) =
        shoppingItemDao.updateCheckedStatus(id, checked)

    suspend fun deleteShoppingItem(item: ShoppingItem) =
        shoppingItemDao.deleteItem(item.toEntity())

    suspend fun clearCheckedItems() =
        shoppingItemDao.deleteCheckedItems()

    suspend fun clearAllShoppingItems() =
        shoppingItemDao.deleteAllItems()

    fun getCurrentWeekYear(): String {
        val sdf = SimpleDateFormat("yyyy-'W'ww", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }
}
