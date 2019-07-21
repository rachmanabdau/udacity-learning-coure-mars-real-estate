package com.example.android.marsrealestate.repository

import com.example.android.marsrealestate.data.local.MarsDatabase
import com.example.android.marsrealestate.data.local.MarsEntity
import com.example.android.marsrealestate.data.network.MarsApi
import com.example.android.marsrealestate.data.network.MarsApiFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: MarsDatabase) {

    /**
     * Take data from server and insert to database an then
     * retrieve data from database to be displayed in the UI
     */
    suspend fun fetchProperties(): List<MarsEntity>? {
        return withContext(Dispatchers.IO) {
            refreshData()
            database.marsDao.getAlldata()
        }
    }

    /**
     * Refresh data by retrieving data from server and then save it to database
     */
    suspend fun refreshData() {
        val propertiesFromNetwork = MarsApi.retrofitService.getPRoperties().await()
        for (i in propertiesFromNetwork) {
            database.marsDao.insert(MarsEntity.networkToLocal(i))
        }
    }

    /**
     * Retrieve data from data with filtered or not filtered data
     */
    suspend fun getFilteredProperties(filter: MarsApiFilter): List<MarsEntity>? {
        return withContext(Dispatchers.IO) {
            if (filter == MarsApiFilter.SHOW_ALL) {
                database.marsDao.getAlldata()
            } else {
                database.marsDao.getFilteredProperties(filter.value)
            }
        }
    }
}