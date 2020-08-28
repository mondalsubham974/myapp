package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class FriendFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var friendList: ArrayList<String>? = null
    private val mUsersDatabase: DatabaseReference? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var firebaseUser: String? = null
    private var mFriendAdapter: FriendAdapter? = null
    var userIdVisit:String= ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        recyclerView = view.findViewById(R.id.friend_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)


        firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid

        //I just made the line below pull from the right data source.
        mDatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Confirm Friends/${firebaseUser}/Friends")

        friendList = ArrayList()
        friend()



        return view
    }
    private fun searchForUsers(str : String){
        val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
        val queryUser =FirebaseDatabase.getInstance().reference.child("User")
            .orderByChild("search").startAt(str).endAt(str + "\uf8ff")

        queryUser.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children){
                    val user= snapshot.key!!
                    if (firebaseUser != user){
                        friendList!!.add(user)
                    }
                }
                mFriendAdapter = FriendAdapter(context!!,friendList!!,false)
                recyclerView?.adapter = mFriendAdapter
            }

        })
    }
    private fun friend() {
        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                //TODO: this line below is not necessary

                for (snapshot in p0.children) {
                    //I also made it just get the key, since the key is the userId that we need.
                    val user = snapshot.key!!
                    Log.d("CHUKA", " uuu -> $user")
                    if (firebaseUser != user) {
                        friendList!!.add(user)
                    }
                }

                mFriendAdapter = FriendAdapter(context!!, friendList!!, false)
                recyclerView?.adapter = mFriendAdapter
            }
        })
    }
}




