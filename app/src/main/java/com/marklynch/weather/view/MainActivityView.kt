package com.marklynch.weather.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.marklynch.weather.viewmodel.activity.MainActivityViewModel
import com.marklynch.weather.livedata.network.ConnectionLiveData
import com.marklynch.weather.viewmodel.network.ConnectionModel
import com.marklynch.weather.viewmodel.network.ConnectionType

import kotlinx.android.synthetic.main.activity_main_mine.*
import kotlinx.android.synthetic.main.content_main_mine.*
import java.util.*


class MainActivityView : BaseActivityView() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.marklynch.weather.R.layout.activity_main_mine)
        setSupportActionBar(toolbar)


        val viewModel: MainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        //TIME
//        val timeChangerViewModel = ViewModelProviders.of(this).get(TimeChangerViewModel::class.java)
        val calendar = Calendar.getInstance()
        viewModel.currentTimeLiveData.observe(this, Observer<Long> { t ->
            calendar?.timeInMillis = t!!
            tv_time.text = calendar.time.toString()
        })

        //Raw web resource
//        val rawWebResourceViewModel: RawWebResourceViewModel =
//            ViewModelProviders.of(this).get(RawWebResourceViewModel::class.java)

        viewModel.rawWebResourceLiveData.observe(this,
            Observer<String> { response ->
                response?.let { tv_raw_web_resource.text = it }
            })

        //FAB
//        //Setting text when fab is clicked
//        fab.setOnClickListener { view ->
//            //            model.fabClicked(view) <---THE WAY I WAS CALLING ITTTT
//            model.liveDataFab.observe(this,
//                Observer<String> { value ->
//                    value?.let { text.text = it }
//                })
//        }


        //ONLINE CHECK, shows snackbar when connectivity changes
        val connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this, Observer<ConnectionModel> {
            if (it.isConnected) {

                when (it.type) {

                    ConnectionType.WIFI_CONNECTION -> Toast.makeText(this, "Wifi turned ON", Toast.LENGTH_SHORT).show()

                    ConnectionType.MOBILE_DATA_CONNECTION -> Toast.makeText(
                        this,
                        "Mobile data turned ON",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Connection turned OFF", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.marklynch.weather.R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            com.marklynch.weather.R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
