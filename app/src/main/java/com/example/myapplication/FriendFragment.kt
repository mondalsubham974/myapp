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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var friendList: ArrayList<String>? = null
    private val mUsersDatabase: DatabaseReference? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var firebaseUser: String? = null
    private var mFriendAdapter: FriendAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend_request, container, false)
        recyclerView = view.findViewById(R.id.friend_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid

        //I just made the line below pull from the right data source.
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser!!)
        friendList = ArrayList()
        friend()
        return view
    }

    private fun friend() {
        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                //TODO: this line below is not necessary
                //(requestList as ArrayList<Users>).clear()
                for (snapshot in p0.children){
                    //I also made it just get the key, since the key is the userId that we need.
                    val user = snapshot.key!!
                    Log.d("CHUKA", " uuu -> $user")
                    if (firebaseUser != user){
                        friendList!!.add(user)
                    }
                }

                mFriendAdapter = FriendAdapter(context!!,friendList!!,false)
                recyclerView?.adapter = mFriendAdapter
            }
        })
    }



}