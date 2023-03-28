package com.mobdeve.s11.group12.mco2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.mobdeve.s11.group12.mco2.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            // location coordinates
            val dlsu = LatLng (14.565076303597651, 120.99317101760982)
            val eiffel = LatLng (48.85846537223444, 2.294492026983259)

            // adding of markers to the locations
            googleMap.addMarker(MarkerOptions().position(dlsu).title("DLSU"))
            googleMap.addMarker(MarkerOptions().position(eiffel).title("EIFFEL TOWER"))

            // move the camera towards a location
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(dlsu))

            // set listeners on markers
            googleMap.setOnMarkerClickListener { marker: Marker ->

                Toast.makeText(this, "Clicked location is " + marker.title + ". ID: " + marker.id, Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, PinDetailsActivity::class.java)
                this.startActivity(intent);

                false
            }

        })


        //add new entry
        viewBinding.addBtn.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(this@MainActivity, NewEntryActivity::class.java)
            this.startActivity(intent);
        })

    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

}