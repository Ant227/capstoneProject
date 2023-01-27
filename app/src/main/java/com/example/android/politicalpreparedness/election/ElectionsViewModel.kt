package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class ElectionsViewModel(application: Application): BaseViewModel
(app = application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepository(database)

    val upcomingElections: LiveData<List<Election>>
        get() = repository.upcomingElections


    val savedElections: LiveData<List<Election>>
        get() = repository.followedElections

init {
    viewModelScope.launch {
        refresh()
    }
}

    fun refresh(){
        showLoading.value = true
        viewModelScope.launch {
            try{

                    val electionsList = CivicsApi.retrofitService.getElections()
                    database.electionDao.insertElections(electionsList.elections)
                showLoading.value = false


            }catch(e:Exception){
                showErrorMessage.value= "Unable to retrieve Election details. ${e.message}"
            }
            showLoading.value = false
        }


    }



}