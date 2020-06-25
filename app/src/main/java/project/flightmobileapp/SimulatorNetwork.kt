package project.flightmobileapp
import android.graphics.Bitmap
import android.media.Image
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
import retrofit2.http.*
import java.io.File


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



private var BASE_URL = "http://10.0.2.2:52713/"
fun setBaseUrl(address : String) {
    BASE_URL = address
}




interface SimulatorApiService {
    @GET("/api/screenshot")
    @Streaming
    fun getScreenshot(): Deferred<ResponseBody>

    @POST("/api/command")
    fun postCommand(@Body command : Command): Deferred<Response<Void>>


    //TODO try to change url up
    @POST("/api/connect")
    fun connectToServer(): Deferred<Response<Void>>

}

object SimulatorConnectionApi {
    val retrofitService: SimulatorApiService by lazy {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()
        retrofit.create(SimulatorApiService::class.java)
    }
}


object SimulatorApi {
    val retrofitService: SimulatorApiService by lazy {
         val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()
        retrofit.create(SimulatorApiService::class.java)
    }
}

data class SimulatorProperty(
    val id: String,
    val address: String
    //@Json(name = "img_src") val imgSrcUrl: String
)

data class Command(
    val aileron: Double,
    val elevator: Double,
    val throttle: Double,
    val rudder: Double
)