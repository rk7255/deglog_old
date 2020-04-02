package jp.ryuk.deglog_a01.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "animal_table")
data class Animal(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var date: Long = System.currentTimeMillis(),
    var name: String = "",
    var weight: Int = -1,
    var length: Int = -1,
    var memo: String = ""
)