package com.mobdeve.s11.group12.mco2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s11.group12.mco2.databinding.PinDetailsBinding

class PinDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: PinDetailsBinding = PinDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // go back to home page when logo is pressed
        viewBinding.logoTv.setOnClickListener {
            finish()
        }

        //save and go back to home
        viewBinding.saveBtn.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(this@PinDetailsActivity, MainActivity::class.java)
            finish()
            this.startActivity(intent);
        })
    }
}