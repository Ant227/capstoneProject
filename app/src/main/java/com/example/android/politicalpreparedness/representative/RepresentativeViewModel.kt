package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class RepresentativeViewModel : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address


    fun getRepresentatives(address: String) {
        viewModelScope.launch {
            try {
                val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address)
                _representatives.value = offices.flatMap { office
                    ->
                    office.getRepresentatives(officials)
                }
            } catch (e: Exception) {
                Timber.i("Error Loading Representatives")
            }
        }

    }

    fun addressGeoLocation(address: Address) {
        _address.value = address
    }


}
