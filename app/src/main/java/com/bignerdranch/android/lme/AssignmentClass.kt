package com.bignerdranch.android.lme

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class AssignmentClass(
    @ColumnInfo(name = "id") var difficulty: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "booleanClass") var booleanClass: Boolean
): Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}