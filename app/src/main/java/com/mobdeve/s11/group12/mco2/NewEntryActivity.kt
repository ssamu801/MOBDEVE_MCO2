package com.mobdeve.s11.group12.mco2

import android.annotation.SuppressLint
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
import com.mobdeve.s11.group12.mco2.databinding.ActivityNewEntryBinding
import com.squareup.picasso.Picasso
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class NewEntryActivity : AppCompatActivity() {
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var viewBinding: ActivityNewEntryBinding
    private lateinit var myDbHelper: MyDbHelper
    private var imageUri: Uri? = null
    var help : Long = 0

    private lateinit var outputStream: OutputStream

    private val myActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            try {
                if (result.data != null) {
                    imageUri = result.data!!.data
                    println(imageUri.toString())
                    Picasso.get().load(imageUri).into(viewBinding.addPhotoIv)
                }
            } catch (exception: Exception) {
                Log.d("TAG", "" + exception.localizedMessage)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val formatter = SimpleDateFormat("MM/dd/yyyy")
        val date = formatter.format(Date())

        this.viewBinding = ActivityNewEntryBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        // Current date is placed on the date field. User has option to change
        this.viewBinding.postDateEt.setText(date.toString())


        val latitude = this.intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = this.intent.getDoubleExtra("LONGITUDE", 0.0)

        this.viewBinding.selectBtn.setOnClickListener(View.OnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_OPEN_DOCUMENT
            myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"))
        })


        // go back to home page when logo is pressed
        viewBinding.logoTv.setOnClickListener {
            finish()
        }

        // discard entry and go back to previous activity
        viewBinding.discardBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        // save entry and go to pin details
        viewBinding.saveEntryBtn.setOnClickListener(View.OnClickListener { view ->

            if (areFieldsComplete()){

                val dir = File(Environment.getExternalStorageDirectory(), "PinsyMedia")

                if (!dir.exists()) {
                    dir.mkdir()
                }

                val image = this.viewBinding.addPhotoIv
                val imageBitmap = image.drawable.toBitmap()

                val file = File(dir, System.currentTimeMillis().toString() + ".jpg")

                outputStream = FileOutputStream(file) //location of the image
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)


                imageUri = Uri.fromFile(file)
                executorService.execute {
                    // This is logic for adding a Contact
                    myDbHelper = MyDbHelper.getInstance(this@NewEntryActivity)!!
                    val id = myDbHelper.insertEntry(Entry(
                        latitude,
                        longitude,
                        this.viewBinding.enterLocEt.text.toString(),
                        this.viewBinding.postDateEt.text.toString(),
                        this.viewBinding.postContentEt.text.toString(),
                        imageUri.toString()
                    ))

                    val intent : Intent = Intent(this@NewEntryActivity, PinDetailsActivity::class.java)
                    intent.putExtra("DETAILS_LATITUDE", latitude)
                    intent.putExtra("DETAILS_LONGITUDE", longitude)
                    intent.putExtra("DETAILS_LOCATION", this.viewBinding.enterLocEt.text.toString())
                    intent.putExtra("DETAILS_DATE", this.viewBinding.postDateEt.text.toString())
                    intent.putExtra("DETAILS_NOTES", this.viewBinding.postContentEt.text.toString())
                    intent.putExtra("DETAILS_IMAGE_URI", imageUri.toString())
                    intent.putExtra("DETAILS_ID", id)
                    finish()
                    this.startActivity(intent)
                }


            }
            else{
                Toast.makeText(view.context, "Please fill up all fields", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun areFieldsComplete(): Boolean {
        return this.viewBinding.enterLocEt.text.isNotEmpty() && this.viewBinding.postDateEt.text.isNotEmpty() && this.viewBinding.postContentEt.text.isNotEmpty() && (imageUri != null)
    }



}