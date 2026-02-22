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

// Repositorio del planificador y lista de la compra
// Es el único punto de acceso a estos datos (no se toca el DAO desde fuera)
@Singleton
class MealPlanRepository @Inject constructor(
    private val mealPlanDao: MealPlanDao,
    private val shoppingItemDao: ShoppingItemDao,
    private val authRepository: AuthRepository
) {
    // userId se recalcula cada vez por si cambia la sesión
    private val userId get() = authRepository.getCurrentUserId()

    // Devuelve el plan de la semana indicada, convirtiendo las entities a modelos
    fun getMealPlanForWeek(weekYear: String): Flow<List<MealPlan>> =
        mealPlanDao.getMealPlanForWeek(userId, weekYear).map { it.map { e -> e.toMealPlan() } }

    suspend fun addMealPlan(mealPlan: MealPlan) =
        mealPlanDao.insertMealPlan(mealPlan.toEntity(userId))

    suspend fun removeMealPlan(id: Int) =
        mealPlanDao.deleteMealPlanById(userId, id)

    fun getAllShoppingItems(): Flow<List<ShoppingItem>> =
        shoppingItemDao.getAllShoppingItems(userId).map { it.map { e -> e.toShoppingItem() } }

    suspend fun addShoppingItem(item: ShoppingItem) =
        shoppingItemDao.insertItem(item.toEntity(userId))

    // Añade todos los ingredientes de una receta de golpe
    suspend fun addShoppingItems(items: List<ShoppingItem>) =
        shoppingItemDao.insertItems(items.map { it.toEntity(userId) })

    suspend fun toggleShoppingItem(id: Int, checked: Boolean) =
        shoppingItemDao.updateCheckedStatus(userId, id, checked)

    suspend fun deleteShoppingItem(item: ShoppingItem) =
        shoppingItemDao.deleteItem(item.toEntity(userId))

    suspend fun clearCheckedItems() =
        shoppingItemDao.deleteCheckedItems(userId)

    suspend fun clearAllShoppingItems() =
        shoppingItemDao.deleteAllItems(userId)

    // Devuelve la semana actual en formato "2024-W10" para filtrar el plan
    fun getCurrentWeekYear(): String {
        val sdf = SimpleDateFormat("yyyy-'W'ww", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }
}