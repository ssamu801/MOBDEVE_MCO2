package com.mobdeve.s11.group12.mco2

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
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
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var tempLat : Double? = null
    private var tempLong : Double? = null
    private  var tempMarker : Marker? = null

    private lateinit var entries: ArrayList<Entry>
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var myDbHelper: MyDbHelper

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap : GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        executorService.execute{
            myDbHelper = MyDbHelper.getInstance(this@MainActivity)!!
            entries = myDbHelper.getAllEntriesDefault()
        }

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            // location coordinates
            val dlsu = LatLng (14.565076303597651, 120.99317101760982)

            // adding of markers to the locations
            for (item in entries){
                googleMap.addMarker(MarkerOptions().position(LatLng(item.latitude, item.longitude)).title(item.entryLocation))
            }

            // move the camera towards a location
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(dlsu))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f))

            //for adding pins
            googleMap.setOnMapClickListener(object :GoogleMap.OnMapClickListener {

                override fun onMapClick(latlng :LatLng) {

                    if(tempLat == null) {
                        // Clears the previously touched position
                        googleMap.clear();
                        // Animating to the touched position
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))

                        // Adds temporary marker
                        val location = LatLng(latlng.latitude, latlng.longitude)
                        tempMarker = googleMap.addMarker(MarkerOptions().position(location))

                        tempLat = location.latitude
                        tempLong = location.longitude



                    }
                    else{
                        if (tempMarker == null){ // If there is no temporary marker in the map
                            for (item in entries){
                                googleMap.addMarker(MarkerOptions().position(LatLng(item.latitude, item.longitude)).title(item.entryLocation))
                            }
                        }
                        else{ // If the map has a temporary marker
                            tempMarker!!.remove()
                            tempLat = null
                            tempLong = null
                            for (item in entries){
                                googleMap.addMarker(MarkerOptions().position(LatLng(item.latitude, item.longitude)).title(item.entryLocation))
                            }
                        }
                    }
                }
            })

            // set listeners on markers
            googleMap.setOnMarkerClickListener { marker: Marker ->

                // Display the title and id of marker clicked
                // Toast.makeText(this, "Clicked location is " + marker.title + ". ID: " + marker.id, Toast.LENGTH_SHORT).show()

                for (item in entries){

                    if(marker.position == LatLng(item.latitude, item.longitude)){
                        val intent = Intent(applicationContext, PinDetailsActivity::class.java)
                        intent.putExtra("DETAILS_LATITUDE", item.latitude)
                        intent.putExtra("DETAILS_LONGITUDE", item.longitude)
                        intent.putExtra("DETAILS_LOCATION", item.entryLocation)
                        intent.putExtra("DETAILS_DATE", item.entryDate)
                        intent.putExtra("DETAILS_NOTES", item.entryNotes)
                        intent.putExtra("DETAILS_IMAGE_URI", item.imageUri)
                        intent.putExtra("DETAILS_ID", item.id)
                        this.startActivity(intent)
                    }
                }

                false
            }

        })


        if(Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                viewBinding.addBtn.setOnClickListener(View.OnClickListener {
                    val intent : Intent = Intent()
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    Toast.makeText(this, "Please enable permission", Toast.LENGTH_SHORT).show()
                    this.startActivity(intent);

                })
            }
            else{
                //add new entry
                viewBinding.addBtn.setOnClickListener(View.OnClickListener {
                    if(tempLat == null && tempLong == null){
                        Toast.makeText(this, "Select a location to pin", Toast.LENGTH_LONG).show()
                    }
                    else{
                        tempMarker!!.remove()
                        val intent : Intent = Intent(this@MainActivity, NewEntryActivity::class.java)
                        intent.putExtra("LATITUDE", tempLat)
                        intent.putExtra("LONGITUDE", tempLong)
                        tempLat = null
                        tempLong = null
                        this.startActivity(intent);
                    }
                })
            }
        }
        else{
            //add new entry
            viewBinding.addBtn.setOnClickListener(View.OnClickListener {
                if(tempLat == null && tempLong == null){
                    Toast.makeText(this, "Select a location to pin", Toast.LENGTH_LONG).show()
                }
                else{
                    tempMarker!!.remove()
                    val intent : Intent = Intent(this@MainActivity, NewEntryActivity::class.java)
                    intent.putExtra("LATITUDE", tempLat)
                    intent.putExtra("LONGITUDE", tempLong)
                    tempLat = null
                    tempLong = null
                    this.startActivity(intent);
                }
            })
        }


    }

    override fun onStart() {
        super.onStart()

        executorService.execute{
            myDbHelper = MyDbHelper.getInstance(this@MainActivity)!!
            entries = myDbHelper.getAllEntriesDefault()
        }

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            // location coordinates
            val dlsu = LatLng (14.565076303597651, 120.99317101760982)

            googleMap.clear()
            // adding of markers to the locations
            for (item in entries){
                googleMap.addMarker(MarkerOptions().position(LatLng(item.latitude, item.longitude)).title(item.entryLocation))
            }

            // move the camera towards a location
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(dlsu))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f))


        })

    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }


}