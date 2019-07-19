package com.example.android.marsrealestate.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.marsrealestate.data.network.MarsProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "mars_entity")
class MarsEntity(
        @PrimaryKey
        var id: String,
        var price: Double,
        var imgSrcUrl: String,
        var type: String) : Parcelable {

    companion object {
        fun networkToLocal(marsProperties: MarsProperty): MarsEntity? {
            val insertToDatabase = MarsEntity(
                    id = marsProperties.id,
                    price = marsProperties.price,
                    imgSrcUrl = marsProperties.imgSrcUrl,
                    type = marsProperties.type
            )

            return insertToDatabase
        }
    }
}