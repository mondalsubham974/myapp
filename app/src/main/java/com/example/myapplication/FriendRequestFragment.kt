package com.example.myapplication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FriendRequestFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var requestList: ArrayList<String>? = null
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
        recyclerView = view.findViewById(R.id.request_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid

        //I just made the line below pull from the right data source.
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child("Add Friend/${firebaseUser}/Receive")
        requestList = ArrayList()
        friendRequest()
        return view
    }


    private fun friendRequest() {

        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                //TODO: this line below is not necessary
                //(requestList as ArrayList<Users>).clear()
                for (snapshot in p0.children){
                    //I also made it just get the key, since the key is the userId that we need.
                    val user = snapshot.key!!
                    Log.d("CHUKA", " uuu -> $user")
                    if (firebaseUser != user){
                        requestList!!.add(user)
                    }
                }

                mRequestAdapter = RequestAdapter(context!!,requestList!!,false)
                recyclerView?.adapter = mRequestAdapter
            }
        })
    }




}
