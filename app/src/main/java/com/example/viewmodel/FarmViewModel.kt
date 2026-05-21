package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FarmViewModel(private val repository: FarmRepository) : ViewModel() {

    // --- State Streams ---
    val fishInventoryList: StateFlow<List<FishInventory>> = repository.allFishInventory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val salesList: StateFlow<List<Sale>> = repository.allSales
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val expensesList: StateFlow<List<Expense>> = repository.allExpenses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val itemInventoryList: StateFlow<List<ItemInventory>> = repository.allItemInventories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Operations ---
    fun addFishInventory(item: FishInventory) {
        viewModelScope.launch {
            repository.insertFishInventory(item)
        }
    }

    fun updateFishInventory(item: FishInventory) {
        viewModelScope.launch {
            repository.updateFishInventory(item)
        }
    }

    fun deleteFishInventory(item: FishInventory) {
        viewModelScope.launch {
            repository.deleteFishInventory(item)
        }
    }

    fun addSale(sale: Sale) {
        viewModelScope.launch {
            repository.insertSale(sale)
        }
    }

    fun updateSale(sale: Sale) {
        viewModelScope.launch {
            repository.updateSale(sale)
        }
    }

    fun deleteSale(sale: Sale) {
        viewModelScope.launch {
            repository.deleteSale(sale)
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun addItemInventory(item: ItemInventory) {
        viewModelScope.launch {
            repository.insertItemInventory(item)
        }
    }

    fun updateItemInventory(item: ItemInventory) {
        viewModelScope.launch {
            repository.updateItemInventory(item)
        }
    }

    fun deleteItemInventory(item: ItemInventory) {
        viewModelScope.launch {
            repository.deleteItemInventory(item)
        }
    }
}

class FarmViewModelFactory(private val repository: FarmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FarmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
