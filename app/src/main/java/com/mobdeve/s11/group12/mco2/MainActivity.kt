package com.mobdeve.s11.group12.mco2

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private companion object{
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "PERMISSION_TAG"
    }


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


            //add new entry
            viewBinding.addBtn.setOnClickListener(View.OnClickListener {
                if(tempLat == null && tempLong == null){
                    Toast.makeText(this, "Select a location to pin", Toast.LENGTH_SHORT).show()
                }
                else{
                    if (checkPermission()){
                        tempMarker!!.remove()
                        val intent : Intent = Intent(this@MainActivity, NewEntryActivity::class.java)
                        intent.putExtra("LATITUDE", tempLat)
                        intent.putExtra("LONGITUDE", tempLong)
                        tempLat = null
                        tempLong = null
                        this.startActivity(intent);
                    }
                    else{
                        tempLat = null
                        tempLong = null
                        Toast.makeText(this, "Please enable permission", Toast.LENGTH_SHORT).show()
                        requestPermission()
                    }

                }
            })



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

    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                this.startActivity(intent)
            }
            catch (e: Exception){
                Log.e(TAG, "requestPermission:", e)
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                this.startActivity(intent)
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }
        else{
            val write = ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()){
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if(write && read){
                    Log.d(TAG, "onRequestPermissionResult: External Storage Permission granted")
                    //indicator here
                }
                else{
                    Log.d(TAG, "onRequestPermissionResult: External Storage Permission denied")
                    Toast.makeText(this, "External Storage Permission denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}