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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_simulator.view.*
import kotlinx.android.synthetic.main.joystick.*
import kotlinx.android.synthetic.main.joystick.view.*
import kotlin.math.*


class SimulatorActivity : AppCompatActivity() {
    private var centerX: Float = 0F
    private var centerY: Float  = 0F
    private var firstClick: Boolean = true
    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        finish()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)
        Log.i("SimulatorActivity", "onCreate Called")
        //Creating a listener for click events.
        val listener = View.OnTouchListener(function = {view, motionEvent ->
            //Click down on button for the first time.
            if (motionEvent.action == MotionEvent.ACTION_DOWN && firstClick) {
                centerX = view.x
                centerY = view.y
                firstClick = false
            }
            Log.i("-y1", view.y.toString())
            //Moving stick while pressing it.
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                //Get line length (center position and current stick position).
                val length = lineLength(view.x.toDouble(), view.y.toDouble(),
                    centerX.toDouble(), centerY.toDouble())
                if (length <= 190) {
                    //Change x and y axis according to stick movement.
                    view.animate().x((motionEvent.x + view.x) / 1.6F)
                        .y((motionEvent.y + view.y) / 1.6F)
                        .setDuration(0)
                        .start()
                    Log.i("-y2", view.y.toString())

                } else {
                    val angle = getAngle( centerX.toDouble(), centerY.toDouble(),
                        view.x.toDouble(), view.y.toDouble())
                    //Change x and y axis according to stick movement.
                    view.animate().x((210 * cos(angle)).toFloat() + centerX)
                        .y((210 * sin(angle)).toFloat() + centerY)
                        .setDuration(0)
                        .start()
                }
            }
            //Releasing press
            if (motionEvent.action  == MotionEvent.ACTION_UP) {
                updateStickPosition(view)
            }
            true
        })
        //Set listener of joystick.
        joystick_stick.setOnTouchListener(listener)
    }

    private fun lineLength(x: Double, y: Double, x1: Double, y1: Double): Double {
        return sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y))
    }

    private fun getAngle(x: Double, y: Double, x1: Double, y1: Double): Double {
        val angle = (atan2((y1 - y),  (x1 - x)))
        return angle
    }

    //Disable joystick outer circle button click sound.
    fun none(view: View) {
        if (view.spacer != null) {
            view.spacer.isSoundEffectsEnabled = false
        }
    }
    private fun updateStickPosition(view: View) {
        //Update stick position with animation.
        val xAnim = ObjectAnimator.ofFloat(view, "x", centerX)
        val yAnim = ObjectAnimator.ofFloat(view, "y", centerY)
        xAnim.apply {
            duration = 80
            start()
        }
        yAnim.apply {
            duration = 80
            start()
        }
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