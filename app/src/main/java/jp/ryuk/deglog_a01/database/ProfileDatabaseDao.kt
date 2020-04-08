package jp.ryuk.deglog_a01.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDatabaseDao {
    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)

    @Delete
    fun delete(profile: Profile)
    
    @Query("SELECT name FROM animal_profiles")
    fun getNamesInProfile() : List<String>

    @Query("SELECT * FROM animal_profiles WHERE name = :key")
    fun getProfile(key: String) : Profile?

    @Query("SELECT * FROM animal_profiles ORDER BY name")
    fun getProfiles() : LiveData<List<Profile>>

    @Query("DELETE FROM animal_profiles")
    fun clearAll()
}