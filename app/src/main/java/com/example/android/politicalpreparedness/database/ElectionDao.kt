package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Follow

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(elections: List<Election>)

    @Query("SELECT * FROM election_table ORDER BY id DESC")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * from election_table WHERE id = :id")
   suspend fun getElection(id: Int): Election



    @Query("INSERT INTO followed_election_table (id) VALUES(:idElection)")
    suspend fun insertFollowed(idElection: Int)

    @Query("SELECT * FROM election_table WHERE id in (SELECT id FROM followed_election_table)")
    fun getFollowedElections(): LiveData<List<Election>>

    @Query("SELECT CASE id WHEN NULL THEN 0 ELSE 1 END FROM followed_election_table WHERE id = :idElection")
    fun isElectionFollowed(idElection: Int): LiveData<Int>


    @Query("DELETE FROM followed_election_table WHERE id = :id")
    suspend fun unFollow(id: Int)

    @Query("DELETE FROM followed_election_table")
    suspend fun clearFollowed()
}