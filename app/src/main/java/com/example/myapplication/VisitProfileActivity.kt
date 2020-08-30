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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*
import kotlinx.android.synthetic.main.activity_visit_profile.*

class VisitProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_profile)
        intent = intent
        userIdVisit = intent.getStringExtra("Visit_id")
        val profileImageView: ImageView = findViewById(R.id.settings_profile)
        val coverImageView: ImageView = findViewById(R.id.settings_cover)
        profileImageView.setOnClickListener {
            val intent = Intent(this,FullImageVisitProfile::class.java)
            intent.putExtra("Visit_id",userIdVisit)
            this.startActivity(intent)
        }

        val refUser = FirebaseDatabase.getInstance().reference.child("User").child(userIdVisit!!)


        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    val user = p0.getValue(Users::class.java)
                    settings_username.text = user?.username
                    Picasso.get().load(user?.profile).placeholder(R.drawable.blank_profile_picture).into(settings_profile)
                    Log.d("msg","profile -> ${user?.profile}")
                    Picasso.get().load(user?.cover).placeholder(R.drawable.cover_profile_image).into(settings_cover)
                    Log.d("msg","cover -> ${user?.cover}")
                }
            }

        })
    }
}