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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ConnectionActivity : AppCompatActivity()  {

    private var chosenList = ArrayList<TextView>()
    private var chosenAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("ConnectionActivity", "onCreate Called")
        setListenersForIP()
        setListenerForConnectButton()
        updateList()

    }

    override fun onRestart() {
        super.onRestart()
        updateList()
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
                addToDb(address)
                CoroutineScope(Dispatchers.Main).launch {
                    connectToServer(address, list)
                }
            } else {
                Toast.makeText(this, "Please enter a valid address.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addToDb(address: String) {
        val dbHandler = IpDatabase(this)
        val addr = dbHandler.ipDatabaseDao().getIP(address)
        val nullVar = null
        if (addr != nullVar) {
            dbHandler.ipDatabaseDao().deleteIP(addr)
        }
        dbHandler.ipDatabaseDao().insert(IpAddresses(address))
    }

    /*Update ip addresses list in window.
    Check for number of saved addresses, and put on screen accordingly.*/
    private fun updateList() {
        val dbHandler = IpDatabase(this)
        val lastAddresses = dbHandler.ipDatabaseDao().getLastFive()
        when (lastAddresses.size) {
            0 -> {}
            1 -> {
                first_address.text = lastAddresses[0].address
            }
            2 -> {
                first_address.text = lastAddresses[0].address
                second_address.text = lastAddresses[1].address
            }
            3 -> {
                first_address.text = lastAddresses[0].address
                second_address.text = lastAddresses[1].address
                third_address.text = lastAddresses[2].address
            }
            4 -> {
                first_address.text = lastAddresses[0].address
                second_address.text = lastAddresses[1].address
                third_address.text = lastAddresses[2].address
                fourth_address.text = lastAddresses[3].address
            }
            else -> {
                first_address.text = lastAddresses[0].address
                second_address.text = lastAddresses[1].address
                third_address.text = lastAddresses[2].address
                fourth_address.text = lastAddresses[3].address
                fifth_address.text = lastAddresses[4].address
            }
        }
    }

    //Connection of simulator to server.
    private suspend fun connectToServer(address: String, list: ArrayList<TextView>) {
        //If connection is successful, open simulator activity.
        try {
            // set given URL to be BASE_URL of retrofitService and attempt to connect to simulator
            setBaseUrl(address)
            val connectResult = SimulatorConnectionApi.retrofitService.connectToServer().await()
            if (connectResult.isSuccessful) {
                val simulatorActivity = Intent(this, SimulatorActivity::class.java)
                // Open simulator.
                startActivity(simulatorActivity)
                startSimActivity(properAddress, address, list)
                // Updating parameters of IP list ordering function (called from "onRestart")
                chosenAddress = address
                chosenList = list
            } else {
                Toast.makeText(this, "Could not connect to server.",
                    Toast.LENGTH_SHORT).show()
            }
        } catch (e : Exception) {
            Toast.makeText(this, "Could not connect to server.",
                Toast.LENGTH_SHORT).show()
        }
    }

    //Stating simulator activity.
    private fun startSimActivity(newAddress: String, address: String, list: ArrayList<TextView>) {
        val simulatorActivity = Intent(this, SimulatorActivity::class.java)
        setBaseUrl(newAddress)
        // Open simulator.
        startActivity(simulatorActivity)
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
}