package com.felipheallef.tasks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "hour")
    var hour: String?,
    @ColumnInfo(name = "done")
    var done: Boolean = false
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}