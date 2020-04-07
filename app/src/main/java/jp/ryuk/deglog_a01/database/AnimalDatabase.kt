package jp.ryuk.deglog_a01.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Animal::class], version = 1, exportSchema = false)
abstract class AnimalDatabase : RoomDatabase() {

    abstract val animalDatabaseDao: AnimalDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: AnimalDatabase? = null

        fun getInstance(context: Context): AnimalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalDatabase::class.java,
                    "animal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}