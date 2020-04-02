package jp.ryuk.deglog_a01.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_profiles")
data class Profile(
    @PrimaryKey
    var name: String = "",
    var type: String = "",
    var sex: String = "",
    var birthday: Long = System.currentTimeMillis()
)