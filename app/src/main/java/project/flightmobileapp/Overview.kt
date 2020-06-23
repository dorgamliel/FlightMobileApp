package project.flightmobileapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class Overview: ViewModel() {
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
    get() = _response
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main )
    init {
        getProperties()
    }

    private fun getProperties() {
        val  x = coroutineScope.launch {
            val getPropertiesDeferred = SimulatorApi.retrofitService.getIpItems()
            try {
                var listResult = getPropertiesDeferred.await()
                _response.value = "Success"
            } catch(e: Exception) {
                _response.value = "Failure"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}