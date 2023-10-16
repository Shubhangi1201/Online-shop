package com.example.onlineshopauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val phone = findViewById<EditText>(R.id.authPhoneNumber)
        val btn = findViewById<Button>(R.id.sendOTPbtn)

        btn.setOnClickListener{
            val intent = Intent(Intent(this@MainActivity, OTPverificationActivity::class.java))
            intent.putExtra("phoneNumber", phone.text.toString())
            startActivity(intent)
        }
    }
}