package com.example.android.marsrealestate.data.local

import android.os.Parcelable
import androidx.room.Entity
import com.example.android.marsrealestate.data.network.MarsProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "mars_entity")
class MarsEntity(var id: String,
                 var price: Double,
                 var imgSrcUrl: String,
                 var type: String) : Parcelable {

    companion object {
        fun networkToLocal(marsProperties: List<MarsProperty>): MarsEntity? {
            val insertToDatabase: MarsEntity? = null
            marsProperties.map {
                insertToDatabase.apply {
                    this?.id = it.id
                    this?.type = it.type
                    this?.imgSrcUrl = it.imgSrcUrl
                    this?.price = it.price
                }
            }

            return insertToDatabase
        }
    }
}