package com.example

import android.app.Application
import com.example.data.FarmDatabase
import com.example.data.FarmRepository

class FarmApplication : Application() {
    val database by lazy { FarmDatabase.getDatabase(this) }
    val repository by lazy { FarmRepository(database.farmDao()) }
}
