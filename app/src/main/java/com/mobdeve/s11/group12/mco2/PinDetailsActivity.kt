package com.mobdeve.s11.group12.mco2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mobdeve.s11.group12.mco2.databinding.ActivityNewEntryBinding
import com.mobdeve.s11.group12.mco2.databinding.PinDetailsBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Executors

class PinDetailsActivity : AppCompatActivity() {

    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var viewBinding: PinDetailsBinding
    private lateinit var entries: ArrayList<Entry>
    private lateinit var myDbHelper: MyDbHelper
    private var imageUri: Uri? = null

    private lateinit var outputStream: OutputStream


    private val myActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            try {
                if (result.data != null) {
                    imageUri = result.data!!.data
                    Picasso.get().load(imageUri).into(viewBinding.pictureIv)
                }
            } catch (exception: Exception) {
                Log.d("TAG", "" + exception.localizedMessage)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding= PinDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        executorService.execute{
            myDbHelper = MyDbHelper.getInstance(this@PinDetailsActivity)!!
            entries = myDbHelper.getAllEntriesDefault()
        }

        var id = this.intent.getLongExtra("DETAILS_ID", -1L)
        val latitude = this.intent.getDoubleExtra("DETAILS_LATITUDE", 0.0)
        val longitude = this.intent.getDoubleExtra("DETAILS_LONGITUDE", 0.0)
        val location = this.intent.getStringExtra("DETAILS_LOCATION")
        val date = this.intent.getStringExtra("DETAILS_DATE")
        val notes = this.intent.getStringExtra("DETAILS_NOTES")
        this.viewBinding.enterLocEt.setText(location)
        this.viewBinding.notesDateTv.setText(date)
        imageUri = Uri.parse(this.intent.getStringExtra("DETAILS_IMAGE_URI"))
        Picasso.get().load(imageUri).into(this.viewBinding.pictureIv)
        this.viewBinding.notesEt.setText(notes)

        println(id)

        // go back to home page when logo is pressed
        viewBinding.logoTv.setOnClickListener {
            finish()
        }

        viewBinding.selectBtn.setOnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_OPEN_DOCUMENT
            myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"))
        }

        //save and go back to home
        viewBinding.saveBtn.setOnClickListener(View.OnClickListener {
            if (areFieldsComplete()){

                val dir = File(Environment.getExternalStorageDirectory(), "PinsyMedia")

                if (!dir.exists()) {
                    dir.mkdir()
                }

                val image = this.viewBinding.pictureIv
                val imageBitmap = image.drawable.toBitmap()

                val file = File(dir, System.currentTimeMillis().toString() + ".jpg")

                outputStream = FileOutputStream(file) //location of the image
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                imageUri = Uri.fromFile(file)
                if (id != 1L){
                    executorService.execute {
                        val myDbHelper = MyDbHelper.getInstance(this@PinDetailsActivity)
                        println(entries.size-1)
                        println(entries[entries.size-1].id)
                        id = entries[entries.size-1].id
                        myDbHelper!!.editEntry(Entry(
                            latitude,
                            longitude,
                            this.viewBinding.enterLocEt.text.toString(),
                            this.viewBinding.notesDateTv.text.toString(),
                            this.viewBinding.notesEt.text.toString(),
                            imageUri.toString(),
                            id
                        ))
                        finish()

                    }

                }
                else{
                    executorService.execute {
                        val myDbHelper = MyDbHelper.getInstance(this@PinDetailsActivity)
                        myDbHelper!!.editEntry(Entry(
                            latitude,
                            longitude,
                            this.viewBinding.enterLocEt.text.toString(),
                            this.viewBinding.notesDateTv.text.toString(),
                            this.viewBinding.notesEt.text.toString(),
                            imageUri.toString(),
                            id
                        ))
                        finish()

                    }
                }

            }
            else{
                Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_LONG).show()
            }



        })



        viewBinding.deleteBtn.setOnClickListener {
            if (id != 1L){
                executorService.execute {
                    val myDbHelper = MyDbHelper.getInstance(this@PinDetailsActivity)
                    println(entries.size-1)
                    println(entries[entries.size-1].id)
                    id = entries[entries.size-1].id
                    myDbHelper!!.deleteEntry(Entry(
                        latitude,
                        longitude,
                        location.toString(),
                        date.toString(),
                        notes.toString(),
                        imageUri.toString(),
                        id
                    ))
                    finish()

                }

            }
            else{
                executorService.execute {
                    val myDbHelper = MyDbHelper.getInstance(this@PinDetailsActivity)
                    myDbHelper!!.deleteEntry(Entry(
                        latitude,
                        longitude,
                        location.toString(),
                        date.toString(),
                        notes.toString(),
                        imageUri.toString(),
                        id
                    ))
                    finish()

                }
            }

        }
    }

    private fun areFieldsComplete(): Boolean {
        return this.viewBinding.enterLocEt.text.isNotEmpty() && this.viewBinding.notesDateTv.text.isNotEmpty() && this.viewBinding.notesEt.text.isNotEmpty() && (imageUri != null)
    }
}