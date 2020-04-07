package jp.ryuk.deglog_a01.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DiaryDatabaseDao {
    @Insert
    fun insert(diary: Diary)

    @Update
    fun update(diary: Diary)

    @Delete
    fun delete(diary: Diary)

    @Query("SELECT * FROM diary_table ORDER BY id DESC")
    fun getDiaries() : LiveData<List<Diary>>

    @Query("SELECT * FROM diary_table WHERE id = :key")
    fun getDiary(key: Long) : Diary

    @Query("SELECT * FROM diary_table WHERE name = :name ORDER BY id LIMIT 1")
    fun getLatestDiary(name: String) : Diary

    @Query("DELETE FROM diary_table")
    fun clear()
}