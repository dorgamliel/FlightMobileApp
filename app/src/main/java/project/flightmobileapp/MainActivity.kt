package project.flightmobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

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
}