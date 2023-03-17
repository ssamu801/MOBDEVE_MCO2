package com.mobdeve.s11.group12.mco2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.mobdeve.s11.group12.mco2.databinding.ActivityNewEntryBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class NewEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding : ActivityNewEntryBinding = ActivityNewEntryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // go back to home page when logo is pressed
        viewBinding.logoTv.setOnClickListener {
            finish()
        }

        // discard entry and go back to previous activity
        viewBinding.discardBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        // save entry and go to pin details
        viewBinding.saveEntryBtn.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(this@NewEntryActivity, PinDetailsActivity::class.java)
            finish()
            this.startActivity(intent);
        })

    }
}