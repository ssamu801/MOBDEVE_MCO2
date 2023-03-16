package com.mobdeve.s11.group12.mco2

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

        //discard entry and go back to previous activity
        viewBinding.discardBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        /*
        //save entry
        viewBinding.saveButton.setOnClickListener(View.OnClickListener {
            if(viewBinding.enterLocEt.text.toString().isNotEmpty()) {
                //code
            }
        })
         */

    }
}