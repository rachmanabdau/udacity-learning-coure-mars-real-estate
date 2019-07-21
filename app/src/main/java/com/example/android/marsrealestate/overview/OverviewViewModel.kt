/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.*
import com.example.android.marsrealestate.data.local.MarsDatabase
import com.example.android.marsrealestate.data.local.MarsEntity
import com.example.android.marsrealestate.data.network.MarsApiFilter
import com.example.android.marsrealestate.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
enum class MarsApiStatus {
    LOADING, DONE, FAILED
}

class OverviewViewModel(app: Application, private val context: Context) : AndroidViewModel(app) {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = MarsDatabase.getInstance(app)
    private val repository = Repository(database)

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()
    private val _properties = MutableLiveData<List<MarsEntity>?>()
    private val _navigateToSelectedProperties = MutableLiveData<MarsEntity>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus>
        get() = _status

    val properties: LiveData<List<MarsEntity>?>
        get() = _properties

    val navigateToSelectedProperty: LiveData<MarsEntity>
        get() = _navigateToSelectedProperties

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        populateDatabase()
    }

    /**
     *
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun populateDatabase() {
        coroutineScope.launch {
            if (isNetworkAvailable()) {
                try {
                    _status.value = MarsApiStatus.LOADING
                    _properties.value = repository.fetchProperties()
                    _status.value = MarsApiStatus.DONE
                } catch (e: Throwable) {
                    _status.value = MarsApiStatus.FAILED
                }
            } else {
                _status.value = MarsApiStatus.FAILED
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun displayPropertyDetails(marsEntity: MarsEntity) {
        _navigateToSelectedProperties.value = marsEntity
    }

    fun displayPropertyDisplayComplete() {
        _navigateToSelectedProperties.value = null
    }

    fun getProperties(filter: MarsApiFilter) {
        coroutineScope.launch {
            if (isNetworkAvailable()) {
                _status.value = MarsApiStatus.LOADING
                _properties.value = repository.getFilteredProperties(filter)
                _status.value = MarsApiStatus.DONE
            } else {
                _status.value = MarsApiStatus.FAILED
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application, val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OverviewViewModel::class.java))
                return OverviewViewModel(app, context) as T

            throw IllegalArgumentException("Unable to construct viewModel (OverviewViewModel).")
        }

    }
}
