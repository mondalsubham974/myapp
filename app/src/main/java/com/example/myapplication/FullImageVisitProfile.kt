package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_visit_profile.*
import kotlinx.android.synthetic.main.activity_visit_profile.*

private var image_viewer: ImageView? = null
private var imageUrl: String = ""
class FullImageVisitProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_visit_profile)

        image_viewer = findViewById(R.id.full_image_visit_profile)
        Picasso.get().load(imageUrl).into(image_viewer)

    }
}