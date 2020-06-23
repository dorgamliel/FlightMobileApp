package project.flightmobileapp
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://localhost:5000"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface SimulatorApiService {
    @GET("all")
    fun getIpItems():
            Deferred<List<SimulatorProperty>>
}

object SimulatorApi {
    val retrofitService: SimulatorApiService by lazy {
        retrofit.create(SimulatorApiService::class.java)
    }
}

data class SimulatorProperty(
    val id: String,
    val address: String
    //@Json(name = "img_src") val imgSrcUrl: String
)