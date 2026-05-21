package com.example.data

import kotlinx.coroutines.flow.Flow

class FarmRepository(private val farmDao: FarmDao) {

    // --- Fish Inventory ---
    val allFishInventory: Flow<List<FishInventory>> = farmDao.getAllFishInventory()

    suspend fun insertFishInventory(item: FishInventory) {
        farmDao.insertFishInventory(item)
    }

    suspend fun updateFishInventory(item: FishInventory) {
        farmDao.updateFishInventory(item)
    }

    suspend fun deleteFishInventory(item: FishInventory) {
        farmDao.deleteFishInventory(item)
    }

    // --- Sales ---
    val allSales: Flow<List<Sale>> = farmDao.getAllSales()

    suspend fun insertSale(sale: Sale) {
        farmDao.insertSale(sale)
    }

    suspend fun updateSale(sale: Sale) {
        farmDao.updateSale(sale)
    }

    suspend fun deleteSale(sale: Sale) {
        farmDao.deleteSale(sale)
    }

    // --- Expenses ---
    val allExpenses: Flow<List<Expense>> = farmDao.getAllExpenses()

    suspend fun insertExpense(expense: Expense) {
        farmDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        farmDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        farmDao.deleteExpense(expense)
    }

    // --- Item Inventory ---
    val allItemInventories: Flow<List<ItemInventory>> = farmDao.getAllItemInventories()

    suspend fun insertItemInventory(item: ItemInventory) {
        farmDao.insertItemInventory(item)
    }

    suspend fun updateItemInventory(item: ItemInventory) {
        farmDao.updateItemInventory(item)
    }

    suspend fun deleteItemInventory(item: ItemInventory) {
        farmDao.deleteItemInventory(item)
    }
}
