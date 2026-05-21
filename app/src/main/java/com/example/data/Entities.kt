package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "fish_inventory")
data class FishInventory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,        // e.g., Tilapia, Rohu, Catla, Grass Carp
    val variety: String,     // e.g., Fingerlings, Adult
    val count: Int,          // Specific count
    val weightKg: Double,    // Weight amount (in kg)
    val notes: String = ""
) : Serializable

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateMillisecond: Long,
    val fishType: String,
    val variety: String,
    val ratePerKg: Double,
    val quantityKg: Double,
    val totalEarnings: Double, // calculated ratePerKg * quantityKg
    val notes: String = ""
) : Serializable

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,     // "Fertilizer", "Feed", "Water", "Medicine", "Other"
    val dateMillisecond: Long,
    val description: String,  // e.g., "Urea" (Fertilizer type), "AquaFeed Max"
    val bagsCount: Int? = null, // for Fertilizer / Feed
    val rate: Double? = null,   // rate per bag or per unit
    val totalCost: Double,    // overall total cost
    val notes: String = ""
) : Serializable

@Entity(tableName = "item_inventories")
data class ItemInventory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemName: String,
    val category: String,     // "Feed", "Fertilizer", "Medicine", "Other"
    val stockCount: Double,   // count of units in stock
    val unit: String,         // "Bags", "Kgs", "Bottles", "Units"
    val notes: String = ""
) : Serializable
