package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.*
import androidx.versionedparcelable.ParcelField
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
): Parcelable

@Entity(tableName = "followed_election_table")
data class Follow(
        @PrimaryKey val id: Int
)