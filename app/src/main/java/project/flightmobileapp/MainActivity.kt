package project.flightmobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "onCreate Called")
        setContentView(R.layout.activity_main)
    }
    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart Called")
        val intent = Intent(this, ConnectionActivity::class.java)
        //Show connection page on startup.
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        Log.i("MainActivity","onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity","onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity","onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity","onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("MainActivity","onRestart Called")
    }
}