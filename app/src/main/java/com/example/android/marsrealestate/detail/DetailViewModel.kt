/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.data.local.MarsEntity

/**
 * The [ViewModel] that is associated with the [DetailFragment].
 */
class DetailViewModel(marsEntity: MarsEntity, app: Application) : AndroidViewModel(app) {

    private val _selectedProperty = MutableLiveData<MarsEntity>()

    val selectedProperty: LiveData<MarsEntity>
        get() = _selectedProperty

    init {
        _selectedProperty.value = marsEntity
    }

    val displayPropertyPrice = Transformations.map(selectedProperty) {
        app.applicationContext.getString(
                when (it.type.equals("rent")) {
                    true -> R.string.display_price_monthly_rental
                    false -> R.string.display_price
                }, it.price)
    }

    val displayPropertyType = Transformations.map(selectedProperty) {
        app.applicationContext.getString(R.string.display_type,
                app.applicationContext.getString(
                        when (it.type.equals("rent")) {
                            true -> R.string.type_rent
                            false -> R.string.type_sale
                        }))
    }
}
