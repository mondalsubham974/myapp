package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    var usersRefference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        usersRefference =
            firebaseUser?.uid?.let {
                FirebaseDatabase.getInstance().reference.child("Users").child(
                    it
                )
            }

        usersRefference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user: Users? = p0.getValue(Users::class.java)
                    if (context!=null){
                        view.setting_username.text = user?.username
                        Picasso.get().load(user?.profile).into(view.settings_profile)
                        Picasso.get().load(user?.profile).into(view.settings_cover)
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }



        })
        return view
    }



}