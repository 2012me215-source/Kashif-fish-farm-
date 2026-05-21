package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmDao {

    // --- Fish Inventory ---
    @Query("SELECT * FROM fish_inventory ORDER BY type ASC, variety ASC")
    fun getAllFishInventory(): Flow<List<FishInventory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFishInventory(item: FishInventory)

    @Update
    suspend fun updateFishInventory(item: FishInventory)

    @Delete
    suspend fun deleteFishInventory(item: FishInventory)

    // --- Sales ---
    @Query("SELECT * FROM sales ORDER BY dateMillisecond DESC")
    fun getAllSales(): Flow<List<Sale>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale)

    @Update
    suspend fun updateSale(sale: Sale)

    @Delete
    suspend fun deleteSale(sale: Sale)

    // --- Expenses ---
    @Query("SELECT * FROM expenses ORDER BY dateMillisecond DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    // --- Item Inventory ---
    @Query("SELECT * FROM item_inventories ORDER BY itemName ASC")
    fun getAllItemInventories(): Flow<List<ItemInventory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemInventory(item: ItemInventory)

    @Update
    suspend fun updateItemInventory(item: ItemInventory)

    @Delete
    suspend fun deleteItemInventory(item: ItemInventory)
}
