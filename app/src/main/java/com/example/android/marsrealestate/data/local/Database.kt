package com.example.android.marsrealestate.data.local

import android.content.Context
import androidx.room.*

@Dao
interface MarsDao {
    @Query("SELECT * FROM mars_entity")
    fun getAlldata(): List<MarsEntity>?

    @Query("SELECT * FROM mars_entity WHERE type = :filter")
    fun getFilteredProperties(filter: String): List<MarsEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg marsEntity: MarsEntity?)
}

@Database(entities = [MarsEntity::class], version = 1)
abstract class MarsDatabase : RoomDatabase() {

    abstract val marsDao: MarsDao

    companion object {

        @Volatile
        private var INSTANCE: MarsDatabase? = null

        fun getInstance(context: Context): MarsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            MarsDatabase::class.java,
                            "sleep_history_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}