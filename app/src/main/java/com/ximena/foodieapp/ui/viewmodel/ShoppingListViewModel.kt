package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.ShoppingItem
import com.ximena.foodieapp.domain.usecase.AddShoppingItemsUseCase
import com.ximena.foodieapp.domain.usecase.ClearCheckedItemsUseCase
import com.ximena.foodieapp.domain.usecase.DeleteShoppingItemUseCase
import com.ximena.foodieapp.domain.usecase.GetShoppingItemsUseCase
import com.ximena.foodieapp.domain.usecase.ToggleShoppingItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    getShoppingItemsUseCase: GetShoppingItemsUseCase,
    private val addShoppingItemsUseCase: AddShoppingItemsUseCase,
    private val toggleShoppingItemUseCase: ToggleShoppingItemUseCase,
    private val deleteShoppingItemUseCase: DeleteShoppingItemUseCase,
    private val clearCheckedItemsUseCase: ClearCheckedItemsUseCase
) : ViewModel() {

    val shoppingItems: StateFlow<List<ShoppingItem>> = getShoppingItemsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleItem(id: Int, checked: Boolean) {
        viewModelScope.launch { toggleShoppingItemUseCase(id, checked) }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch { deleteShoppingItemUseCase(item) }
    }

    fun clearChecked() {
        viewModelScope.launch { clearCheckedItemsUseCase() }
    }

    fun addItem(name: String, measure: String) {
        if (name.isBlank()) return
        viewModelScope.launch { addShoppingItemsUseCase(ShoppingItem(name = name, measure = measure)) }
    }
}