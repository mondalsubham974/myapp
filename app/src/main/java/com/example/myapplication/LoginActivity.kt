package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        login_button.setOnClickListener{
            LoginUser()
        }

        login_textView.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun LoginUser() {
        val email: String = login_email.text.toString()
        val password: String = login_password.text.toString()

        if(email == ""){
            Toast.makeText(this,"plese enter email", Toast.LENGTH_LONG).show()
        }
        if(password == ""){
            Toast.makeText(this,"plese enter password", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        val intent = Intent(this,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Error Message:"+task.exception!!.message.toString()
                            , Toast.LENGTH_LONG).show()
                    }
                }
        }

    }



}

