package com.example.donorbox.data.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "myDonations_table")
@Serializable
@Immutable
data class MyDonations(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val myDonations: String
)
