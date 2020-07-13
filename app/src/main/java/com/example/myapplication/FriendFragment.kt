package com.example.myapplication

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_search_item_layout.*
import java.util.HashMap


class FriendFragment : Fragment() {

    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var mCurrent_state = "not_friends"
    private var mProfileSendReqButton: Button? =null
    private var mProfilecanButton: Button? =null
    var firebaseUserId = FirebaseAuth.getInstance().currentUser
    var refUsers = FirebaseDatabase.getInstance().reference.child("User")
    var mUserDatabase= FirebaseDatabase.getInstance().reference.child("Users")
    var mFriendsDatabse= FirebaseDatabase.getInstance().reference.child("Friends").child(firebaseUserId!!.uid)
    var mAuth = FirebaseAuth.getInstance()
    var mRootReference = FirebaseDatabase.getInstance().reference
    var user_id: String? = null
    var mfriendReqReference: DatabaseReference? = null
    var mProgressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        recyclerView = view.findViewById(R.id.friend_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        mProfileSendReqButton = view.findViewById(R.id.activity_user_item_layout_add_friend)
        mUsers = ArrayList()

        mFriendsDatabse.keepSynced(true)

        mProfileSendReqButton?.setOnClickListener{
            val firebaseUserId = FirebaseAuth.getInstance().currentUser
            if (!(firebaseUserId!!.uid).equals(firebaseUserId)){
                return@setOnClickListener
            }
            Log.e("m_current_state is : ", mCurrent_state)
            mProfileSendReqButton!!.isEnabled = false

        }
        sendFriendRequest()


        return view

    }

    private fun sendFriendRequest() {

        if (mCurrent_state == "not_friends") {
            refUsers = FirebaseDatabase.getInstance().reference.child("User").child(firebaseUserId!!.uid)
            val requestMap = HashMap<String,Any>()
            requestMap["friend_request/" + firebaseUserId!!.uid + "/" + user_id + "/request_type"] =
                "sent"
            requestMap["friend_request/" + user_id + "/" + firebaseUserId!!.uid + "/request_type"] =
                "received"
            }
            mFriendsDatabse.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    mfriendReqReference!!.child(firebaseUserId!!.uid)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                //----CHECKING IF FRIEND REQUEST IS SEND OR RECEIVED----
                                if (dataSnapshot.hasChild(user_id.toString())) {
                                    val request_type =
                                        dataSnapshot.child(user_id.toString()).child("request_type")
                                            .value.toString()
                                    if (request_type == "sent") {
                                        mCurrent_state = "req_sent"
                                        mProfileSendReqButton!!.text = "Cancel Friend Request"
                                        mProfilecanButton!!.visibility = View.INVISIBLE
                                        mProfilecanButton!!.isEnabled = false
                                    } else if (request_type == "received") {
                                        mCurrent_state = "req_received"
                                        mProfileSendReqButton!!.text = "Accept Friend Request"
                                        mProfilecanButton!!.visibility = View.VISIBLE
                                        mProfilecanButton!!.isEnabled = true
                                    }
                                    mProgressDialog!!.dismiss()
                                } else {
                                    mFriendsDatabse.child(firebaseUserId!!.uid)
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                mProfilecanButton!!.visibility =
                                                    View.INVISIBLE
                                                mProfilecanButton!!.isEnabled = false
                                                if (dataSnapshot.hasChild(user_id.toString())) {
                                                    mCurrent_state = "friends"
                                                    mProfileSendReqButton!!.text =
                                                        "Unfriend This Person"
                                                }
                                                mProgressDialog!!.dismiss()
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                mProgressDialog!!.dismiss()
                                            }
                                        })
                                }
                            }


                        })


                }

            })
    }
}