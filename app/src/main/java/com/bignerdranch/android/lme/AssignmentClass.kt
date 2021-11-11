package com.bignerdranch.android.lme

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//This single class is used for both assignment events and
//scheduled events and uses a Boolean to distinguish between the two.
@Entity
data class AssignmentClass(
    @ColumnInfo(name = "id") var difficulty: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "booleanClass") var booleanClass: Boolean,
    @ColumnInfo(name = "startTime") var startTime: String,
    @ColumnInfo(name = "endTime") var endTime : String
): Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}