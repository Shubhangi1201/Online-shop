package com.example.onlineshopauthentication

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPverificationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var error: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)

        auth = FirebaseAuth.getInstance()

        val otp = findViewById<EditText>(R.id.otpEt)
        val btn = findViewById<Button>(R.id.verifyBtn)
        error = findViewById(R.id.error)

        val number = intent.getStringExtra("phoneNumber")
        val phoneNumber = "+91${number}"

        if(phoneNumber != null){

            sendVerificationCode(phoneNumber)
        }


        btn.setOnClickListener{
            verifyOTPCode(otp.text.toString())
        }


    }




    fun sendVerificationCode(phoneNumber: String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@OTPverificationActivity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }



    private val callbacks  = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
            val code: String? = credential.smsCode
            if(!code.isNullOrBlank()){
                verifyOTPCode(code)
                Toast.makeText(this@OTPverificationActivity, "verify otp code function called", Toast.LENGTH_SHORT).show()

            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(ContentValues.TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {

            } else if (e is FirebaseTooManyRequestsException) {

            }

            Toast.makeText(this@OTPverificationActivity, "verification failed " + e.toString(), Toast.LENGTH_SHORT).show()
            error.text = e.toString()

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d(ContentValues.TAG, "onCodeSent:$verificationId")
            Toast.makeText(this@OTPverificationActivity, "code sent successfully ", Toast.LENGTH_SHORT).show()

            storedVerificationId = verificationId

        }
    }

    fun verifyOTPCode(code: String){
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signinbycredentials(credential)
    }

    fun signinbycredentials(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, "authentication failed", Toast.LENGTH_SHORT).show()

                }
            }
    }


}