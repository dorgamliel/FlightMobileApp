package project.flightmobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConnectionActivity : AppCompatActivity()  {

    private var chosenList = arrayListOf<TextView>()
    private var chosenAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("ConnectionActivity", "onCreate Called")
        setListenersForIP()
        setListenerForConnectButton()
    }
    override fun onStart() {
        Log.i("ConnectionActivity", "onStart Called")
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.i("ConnectionActivity","onResume Called")

    }

    override fun onPause() {
        super.onPause()
        Log.i("ConnectionActivity","onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("ConnectionActivity","onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ConnectionActivity","onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        //Change order of IP addresses in window, descending order of latest connections.
        changeListOrder(chosenAddress, chosenList)
        Log.i("ConnectionActivity","onRestart Called")
    }

    //This function sets listeners for all IP addresses in main menu.
    private fun setListenersForIP() {
        //Creating  a list of IDs.
        val list = arrayListOf<TextView>(
            findViewById(R.id.first_address),
            findViewById(R.id.second_address),
            findViewById(R.id.third_address),
            findViewById(R.id.fourth_address),
            findViewById(R.id.fifth_address)
        )
        //Set click listener which switches name in URL text box.
        for (address in list) {
            address.setOnClickListener {
                //Update text in IP box according to clicked address from list.
                val editText: EditText = findViewById(R.id.ip_textbox)
                editText.setText(address.text)
                //Animation for clicking on addresses on menu.
                val animation = AnimationUtils.loadAnimation(this, R.anim.clicked_address_anim)
                address.startAnimation(animation)
            }
        }
    }

    /*This function gets a chosen address and list of IP addresses,
    and orders them by latest connection. */
    private fun changeListOrder(address: String, list: ArrayList<TextView>) {
        var i = 0
        var temp1 = address
        //Iterate all addresses and swap until reaching line with same text.
        while ((i != list.size - 1) && address != list[i].text) {
            val temp2 = list[i].text
            list[i].text = temp1
            temp1 = temp2.toString()
            i += 1
        }
        //Setting text of last address.
        list[i].text = temp1
    }

    //Setting a listener for the connect button.
    private fun setListenerForConnectButton() {
        val btn: Button = findViewById(R.id.connect_button)
        val box: EditText = findViewById(R.id.ip_textbox)
        //Create an array of all text views in the menu (all IP addresses)
        val list = getArraylist()
        btn.setOnClickListener {
            val address = box.text.toString()
            //An empty IP address does not affect button.
            if (address.isNotEmpty()) {
                connectToServer(address, list)
            } else {
                Toast.makeText(this, "Please enter a valid address.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun connectToServer(address: String, list: ArrayList<TextView>) {
        //If connection is successful, open simulator activity.
        if (checkConnection()) {
            val simulatorActivity = Intent(this, SimulatorActivity::class.java)
            // Open simulator.
            startActivity(simulatorActivity)
            // Updating parameters of IP list ordering function (called from "onRestart")
            chosenAddress = address
            chosenList = list
            //No connection to server.
        } else {
            Toast.makeText(this, "Could not connect to server.", Toast.LENGTH_SHORT).show()
        }
        //Invalid server address.
    }

    //Gets list of addresses.
    private fun getArraylist(): ArrayList<TextView> {
        return arrayListOf(
            findViewById(R.id.first_address),
            findViewById(R.id.second_address),
            findViewById(R.id.third_address),
            findViewById(R.id.fourth_address),
            findViewById(R.id.fifth_address)
        )
    }

    //TODO - update function.
    private fun checkConnection(): Boolean {
        return true
    }
}