package com.example.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class Users(val uid:String ="",val username:String ="",val profile:String ="",val cover:String ="",val status:String ="",
            val brithday:String ="",val hometown:String ="",val country:String ="",val relegion:String ="",
            val Relationship:String ="",val Profesion:String ="",val online:String ="") {
    constructor():this("","","","","","","","","")
}
