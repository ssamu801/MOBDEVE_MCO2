package com.mobdeve.s11.group12.mco2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mobdeve.s11.group12.mco2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //pin location
        viewBinding.redPinBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, PinDetailsActivity::class.java)
            this.startActivity(intent);
        })

        //add new entry
        viewBinding.addBtn.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(this@MainActivity, NewEntryActivity::class.java)
            this.startActivity(intent);
        })

    }
}