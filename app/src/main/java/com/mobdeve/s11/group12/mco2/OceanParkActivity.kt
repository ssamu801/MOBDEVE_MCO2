package com.mobdeve.s11.group12.mco2

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s11.group12.mco2.databinding.OceanParkDetailsBinding

class OceanParkActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : OceanParkDetailsBinding = OceanParkDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // go back to home page when logo is pressed
        viewBinding.logoTv.setOnClickListener {
            finish()
        }

        //save and go back to home
        viewBinding.saveBtn.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(this@OceanParkActivity, MainActivity::class.java)
            finish()
            this.startActivity(intent);
        })
    }

}