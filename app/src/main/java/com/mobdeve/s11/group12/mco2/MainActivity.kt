package com.mobdeve.s11.group12.mco2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s11.group12.mco2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.redPinBtn.setOnClickListener {

            val intent = Intent(applicationContext, PinDetailsActivity::class.java)
            this.startActivity(intent);
        }

    }
}