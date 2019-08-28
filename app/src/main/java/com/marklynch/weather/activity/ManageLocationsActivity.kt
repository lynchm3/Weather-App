package com.marklynch.weather.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marklynch.weather.ManualLocationListAdapter
import com.marklynch.weather.R
import com.marklynch.weather.data.ManualLocation
import com.marklynch.weather.viewmodel.ManageLocationsViewModel
import kotlinx.android.synthetic.main.action_bar_main.*
import kotlinx.android.synthetic.main.content_manage_locations.*
import org.koin.android.ext.android.inject


class ManageLocationsActivity : BaseActivity() {

    private val viewModel: ManageLocationsViewModel by inject()
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_locations)
        setSupportActionBar(toolbar)


        val adapter = ManualLocationListAdapter(this, viewModel)
        rv_list.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this)
        rv_list.layoutManager = LinearLayoutManager(this)

        viewModel.manualLocationLiveData?.observe(this, object : Observer<List<ManualLocation>> {
            override fun onChanged(manualLocations: List<ManualLocation>) {
                adapter.setManualLocations(manualLocations)
                if (manualLocations.isEmpty()) {
                    rv_list.visibility = View.GONE
                    tv_messaging.visibility = View.VISIBLE
                } else {
                    rv_list.visibility = View.VISIBLE
                    tv_messaging.visibility = View.GONE
                }
            }
        })
    }

    fun showRenameDialog(manualLocationToRename: ManualLocation) {

        if (alertDialog?.isShowing == true) {
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Rename Location")
        val input = EditText(this)
        input.hint = "New Location Name"
        builder.setView(input)

        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            val newName = input.text.toString()
            if (newName.isEmpty() || newName.isBlank()) {
                Toast.makeText(applicationContext, "No name entered", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.renameManualLocation(manualLocationToRename, newName)
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        this.alertDialog = builder.show()
        input.requestFocus()
    }
}