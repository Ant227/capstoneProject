package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class VoterInfoViewModel(private val electionId: Int,
                         private val division: Division,
                             application: Application) : AndroidViewModel(application) {

    private val database = getInstance(application)
    private val repository = ElectionsRepository(database)

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

            init {
                viewModelScope.launch{
                getVoterInfo()
                }
            }

    val urlIntent = MutableLiveData<String>()
    fun loadingUrl(uri: String?) {
        urlIntent.value = uri
    }

    private val _isElectionFollowed: LiveData<Int>
        get() = database.electionDao.isElectionFollowed(electionId)

    val isElectionFollowed =
            Transformations.map(_isElectionFollowed) { followValue ->
                followValue?.let {
                    followValue == 1
                }
            }

    fun followUnfollowElection() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isElectionFollowed.value == true) {
                    repository.unfollowElection(electionId)
                } else {
                    repository.saveElection(electionId)
                }
            }
        }
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
          val test =  repository.getElection(electionId).division
            var address = division.country
            if (!division.state.isBlank() && !division.state.isEmpty()) {
                address += "/state:${division.state}"
                _voterInfo.value = CivicsApi.retrofitService.getVoterInfo(
                        address, electionId)

            }


        }
    }


}