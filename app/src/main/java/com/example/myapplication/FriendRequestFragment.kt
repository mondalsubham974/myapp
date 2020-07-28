package com.example.myapplication

import android.os.Bundle
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


/**
 * A simple [Fragment] subclass.
 * --- A SIMPLE FRAGMENT FOR MANGING SENT AND RECEIVED FRIEND REQUEST----
 */
class FriendRequestFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var requestList: List<Users>? = null
    private var mDatabaseReference: DatabaseReference? = null
    private val mUsersDatabase: DatabaseReference? = null
    private val mMessageDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var firebaseUser: String? = null
    private var mRequestAdapter: RequestAdapter? = null



        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            val view: View =inflater.inflate(R.layout.fragment_friend_request, container, false)
            recyclerView = view.findViewById(R.id.friend_request_list)
            recyclerView?.setHasFixedSize(true)
            recyclerView?.layoutManager = LinearLayoutManager(context)
            mAuth = FirebaseAuth.getInstance()
            firebaseUser = mAuth!!.currentUser!!.uid
            mDatabaseReference = FirebaseDatabase.getInstance().reference.child("Friend Request")

            requestList = ArrayList()
            friendRequest()
            return view
        }

    private fun friendRequest() {
        mDatabaseReference!!.orderByChild("Receive").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, previousChildName: String?) {
                (requestList as ArrayList<Users>).clear()

                for (snapshot in p0.children){
                    val user: Users = snapshot.getValue(Users::class.java)!!
                    if (!(firebaseUser)!!.equals(user.uid)){
                        (requestList as ArrayList<Users>).add(user)
                    }
                }
                mRequestAdapter!!.notifyDataSetChanged()
                mRequestAdapter = RequestAdapter(requireContext(),requestList!!,false)
                recyclerView!!.adapter = mRequestAdapter
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }



        })



    }


}