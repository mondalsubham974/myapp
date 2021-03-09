package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*
import kotlinx.android.synthetic.main.activity_visit_profile.*

class VisitProfileActivity : AppCompatActivity() {
    var mContext: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_profile)

        val userIdVisit = intent.getStringExtra("Visit_id")
        val profileImageView: ImageView = findViewById(R.id.settings_profile)
        val coverImageView: ImageView = findViewById(R.id.settings_cover)


        val refUser = FirebaseDatabase.getInstance().reference.child("User").child(userIdVisit.toString())

        profileImageView.setOnClickListener {
            val intent = Intent(this,FullImageActivity::class.java)
            startActivity(intent)
        }
        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Users::class.java)

                if (p0.exists()) {
                    settings_username.text = user?.username
                    Picasso.get().load(user?.profile).placeholder(R.drawable.blank_profile_picture).into(profileImageView)
                    Log.d("msg","profile -> ${user?.profile}")
                    Picasso.get().load(user?.cover).placeholder(R.drawable.cover_profile_image).into(coverImageView)
                    Log.d("msg","cover -> ${user?.cover}")
                }
            }

        })
    }


}

