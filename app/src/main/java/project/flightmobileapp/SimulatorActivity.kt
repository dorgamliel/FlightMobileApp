package project.flightmobileapp

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_simulator.*
import kotlinx.android.synthetic.main.activity_simulator.view.*
import kotlinx.android.synthetic.main.activity_simulator.view.rudderBar
import kotlinx.android.synthetic.main.joystick.*
import kotlinx.android.synthetic.main.joystick.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.*



class SimulatorActivity : AppCompatActivity() {
    private var aileron: Double = 0.0
    private var prevAileron: Double = 0.0
    private var elevator: Double = 0.0
    private var prevElevator: Double = 0.0
    private var rudder: Double = 0.0
    private var throttle: Double = 0.0
    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)
        setSeekBarListeners()
        setJoystickListeners()

    }

    private fun setJoystickListeners() {
        //Setting move listener for joystick.
        joystickView.setOnMoveListener (object: JoystickView.OnMoveListener {
            override fun onMove(angle: Int, strength: Int) {
                //Setting joystick's aileron and elevator.
                aileron = cos(Math.toRadians(angle.toDouble())) * strength / 100
                elevator = sin(Math.toRadians(angle.toDouble())) * strength / 100
                //Replace values if difference is more than 1 percent.
                if (abs(aileron - prevAileron) > 0.01) {
                    prevAileron = aileron
                }
                if (abs(elevator - prevElevator) > 0.01) {
                    prevElevator = elevator
                }
                //Async call to server.
                //TODO - call post function.
                CoroutineScope(Dispatchers.IO).launch {  }
            }
        })
        joystickView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                Log.i("test", event.action.toString()) ///see event action value
                if (event.action == MotionEvent.ACTION_BUTTON_RELEASE) {
                    Log.i("test", "hee2")
                    //Update stick position with animation.
                    val xAnim = ObjectAnimator.ofFloat(v, "x", 210F)
                    val yAnim = ObjectAnimator.ofFloat(v, "y", 210F)
                    xAnim.apply {
                        duration = 80
                        start()
                    }
                    yAnim.apply {
                        duration = 80
                        start()
                    }
                    return false
                }
                return false
            }
        })
    }

    private fun setSeekBarListeners() {
        val rudderSeekBar = findViewById<SeekBar>(R.id.rudderBar)
        rudderSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                rudder = progress.toDouble()
                //Async call to server.
                //TODO - call post function.
                CoroutineScope(Dispatchers.IO).launch {  }
            }
        })
        val throttleSeekBar = findViewById<SeekBar>(R.id.throttleBar)
        throttleSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                throttle = progress.toDouble()
                //Async call to server.
                //TODO - call post function.
                CoroutineScope(Dispatchers.IO).launch {  }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.i("SimulatorActivity", "onStart Called")

    }

    //The dialog when there is a connection problem.
    fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        //Dialog message.
        dialogBuilder.setMessage("It looks like there is a network problem. Would you like " +
                "to return to main menu?")
        //Dialog buttons.
        dialogBuilder.setPositiveButton("Return",
            DialogInterface.OnClickListener (function = positiveButtonClick))
        dialogBuilder.setNegativeButton("Stay") { _, _ -> }
        val b = dialogBuilder.create()
        b.show()
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