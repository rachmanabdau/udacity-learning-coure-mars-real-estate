package com.example.android.marsrealestate.repository

import com.example.android.marsrealestate.data.local.MarsDatabase
import com.example.android.marsrealestate.data.local.MarsEntity
import com.example.android.marsrealestate.data.network.MarsApi
import com.example.android.marsrealestate.data.network.MarsApiFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: MarsDatabase) {

    suspend fun getAllProperties(): List<MarsEntity>? {
        return withContext(Dispatchers.IO) {
            database.marsDao.getAlldata()
        }
    }

    suspend fun refreshProperties() {
        withContext(Dispatchers.IO) {
            val propertiesFromNetwork = MarsApi.retrofitService.getPRoperties().await()
            for (i in propertiesFromNetwork) {
                database.marsDao.insert(MarsEntity.networkToLocal(i))
            }
        }
    }

    suspend fun getProperties(filter: MarsApiFilter): List<MarsEntity>? {
        return withContext(Dispatchers.IO) {
            database.marsDao.getFilteredProperties(filter.value)
        }
    }
}