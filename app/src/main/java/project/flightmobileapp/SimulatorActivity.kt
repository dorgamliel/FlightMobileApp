package project.flightmobileapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SimulatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)
        Log.i("SimulatorActivity", "onCreate Called")
    }

    override fun onStart() {
        super.onStart()
        Log.i("SimulatorActivity", "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("SimulatorActivity","onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("SimulatorActivity","onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("SimulatorActivity","onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("SimulatorActivity","onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("SimulatorActivity","onRestart Called")
    }

}