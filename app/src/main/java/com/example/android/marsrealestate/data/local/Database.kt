package com.example.android.marsrealestate.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.marsrealestate.data.network.MarsProperty

@Dao
interface MarsDao {
    @Query("SELECT * FROM mars_entity")
    fun getAlldata(): LiveData<List<MarsEntity>?>

    @Query("SELECT * FROM mars_entity WHERE type = :filter")
    fun getFilteredProperties(filter: String): LiveData<List<MarsProperty>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg marsEntity: MarsEntity?)
}

@Database(entities = [MarsEntity::class], version = 1)
abstract class MarsDatabase : RoomDatabase() {
    abstract val marsDao: MarsDao
}

private lateinit var INSTANCE: MarsDatabase

fun getDatabase(context: Context): MarsDatabase {
    synchronized(MarsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                    context.applicationContext, MarsDatabase::class.java,
                    "mars_databse"
            ).build()
        }
    }
    return INSTANCE
}