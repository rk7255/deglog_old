package jp.ryuk.deglog_a01.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AnimalDatabaseDao {
    @Insert
    fun insert(animal: Animal)

    @Update
    fun update(animal: Animal)

    @Query("SELECT * FROM animal_table WHERE id = :key")
    fun getAnimal(key: Long) : LiveData<Animal?>

    @Query("SELECT * FROM animal_table ORDER BY id DESC")
    fun getAnimals() : LiveData<List<Animal>>

    @Query("DELETE FROM animal_table")
    fun clearAll()

    @Query("SELECT * FROM animal_table ORDER BY id DESC LIMIT 1")
    fun getLatest(): Animal?

    @Query("SELECT * FROM animal_table WHERE name = :name ORDER BY id DESC LIMIT 1")
    fun getAnimalLatest(name: String): Animal?

}