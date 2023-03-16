package com.mobdeve.s11.group12.mco2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s11.group12.mco2.databinding.PinDetailsBinding

class PinDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: PinDetailsBinding = PinDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.logoTv.setOnClickListener {
            finish()
        }


    }
}