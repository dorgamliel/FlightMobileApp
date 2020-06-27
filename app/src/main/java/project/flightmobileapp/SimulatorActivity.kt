package project.flightmobileapp

import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.joystick.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.*


val TIMEOUT = 1
val INTERNAL_SERVER_ERROR = 2
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
    private var activityFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)
        activityFlag = true
        setSeekBarListeners()
        setJoystickListeners()
    }

    //Sets joystick listeners.
    private fun setJoystickListeners() {
        //Setting move listener for joystick.
        joystickView.setOnMoveListener (object: JoystickView.OnMoveListener {
            override fun onMove(angle: Int, strength: Int) {
                //Setting joystick's aileron and elevator.
                var sendFlag = false
                aileron = cos(Math.toRadians(angle.toDouble())) * strength / 100
                elevator = sin(Math.toRadians(angle.toDouble())) * strength / 100
                //Replace values if difference is more than 1 percent.
                if (abs(aileron - prevAileron) > 0.01) {
                    prevAileron = aileron
                    sendFlag = true
                }
                if (abs(elevator - prevElevator) > 0.01) {
                    prevElevator = elevator
                    sendFlag = true
                }
                if (sendFlag) {
                    sendCommand()
                }
            }
        })
    }

    //Sets seekbars listeners.
    private fun setSeekBarListeners() {
        val rudderSeekBar = findViewById<SeekBar>(R.id.rudderBar)
        rudderSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                rudder = progress.toDouble() / rudderSeekBar.max
                Log.i("rudder", rudder.toString())
                sendCommand()
            }
        })
        val throttleSeekBar = findViewById<SeekBar>(R.id.throttleBar)
        throttleSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                throttle = progress.toDouble() / throttleSeekBar.max
                Log.i("throttle", throttle.toString())
                sendCommand()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        activityFlag = true
        Log.i("SimulatorActivity", "onStart Called")
    }

    //The dialog when there is a connection problem.
    private fun showErrorDialog(errorType : Int) {
        onPause()
        val dialogBuilder = AlertDialog.Builder(this)
        //Dialog message.
        if (errorType == TIMEOUT) {
            dialogBuilder.setMessage(
                "It looks like there is a network problem. Would you like " +
                        "to return to main menu?")
        } else {
            dialogBuilder.setMessage(
                "It looks like and unexpected error has occurred. Would you like " +
                        "to return to main menu?")
        }
        //Dialog buttons.
        dialogBuilder.setPositiveButton("Return",
            DialogInterface.OnClickListener (function = positiveButtonClick))
        dialogBuilder.setNegativeButton("Stay") { _, _ -> onResume()
        }
        val b = dialogBuilder.create()
        b.setCanceledOnTouchOutside(false)
        b.show()
    }

    // send command to server with current flight control values
    private fun sendCommand() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val command = Command(aileron, elevator, throttle, rudder)
                val deferedResults = SimulatorApi.retrofitService.postCommand(command)
                // check for server error (could be syntax error)
                if (deferedResults.await().code() == 500) {
                    CoroutineScope(Dispatchers.Main).launch {
                        showErrorDialog(INTERNAL_SERVER_ERROR)
                    }
                }
                // timeout error
            } catch (e : Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    showErrorDialog(TIMEOUT)
                }
            }
        }
    }

    //This function uses a flag for activity status for getting snapshots from server.
    private fun getScreenShots() {
        CoroutineScope(Dispatchers.IO).launch {
            //Run as long as activity status is not paused/stopped/destroyed.
            while (activityFlag) {
                getOneScreenShot()
                delay(500)
            }
        }
    }

    //Getting screenshot image from server.
    suspend  fun getOneScreenShot() {
        try {
            // get screenshot and convert content to bytestream
            val screenshotResponse = SimulatorApi.retrofitService.getScreenshot().await()
            val imageStream = screenshotResponse.byteStream()
            val image = BitmapFactory.decodeStream(imageStream)
            val window = findViewById<ImageView>(R.id.simulator_window)
            CoroutineScope(Dispatchers.Main).launch {
                //Put image in layout.
                window.setImageBitmap(image)
            }

        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                if (e.message.equals("HTTP 500 Internal Server Error")) {
                    showErrorDialog(INTERNAL_SERVER_ERROR)
                } else {
                    showErrorDialog((TIMEOUT))
                }
            }
        }

    }

    //When activity resumes.
    override fun onResume() {
        super.onResume()
        activityFlag = true
        //Request screenshots from server.
        getScreenShots()
        Log.i("SimulatorActivity","onResume Called")
    }

    override fun onPause() {
        super.onPause()
        activityFlag = false
        Log.i("SimulatorActivity","onPause Called")
    }

    override fun onStop() {
        super.onStop()
        activityFlag = false
        Log.i("SimulatorActivity","onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        activityFlag = false
        Log.i("SimulatorActivity","onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        activityFlag = true
        Log.i("SimulatorActivity","onRestart Called")
    }

}