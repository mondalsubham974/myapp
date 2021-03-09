package com.example.myapplication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase


class ChatsFragment : androidx.fragment.app.Fragment() {


    lateinit var recyclerView: RecyclerView
    private var userChatList: ArrayList<String>? = null
    private var firebaseUser: String? = FirebaseAuth.getInstance().currentUser?.uid
    private var chatlistadapter: ChatListAdapter? = null
    private var userIdVisit: String?= null
    private var ref: DatabaseReference? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chats, container, false)


        recyclerView = view.findViewById(R.id.chat_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        ref = FirebaseDatabase.getInstance().reference.child("ChatsList/${firebaseUser}")
        userChatList = ArrayList()


        Log.d("Subham", "chatlist->$ref")
        retrievechatlist()

        return view
    }

    private fun retrievechatlist(){
        userChatList = ArrayList()


        Log.d("Subha", "chatlist->$ref")
        ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children) {
                    //I also made it just get the key, since the key is the userId that we need.
                    val user = snapshot.key!!
                    Log.d("CHUKA", " uuu -> $user")
                    if (firebaseUser != user) {
                        userChatList!!.add(user)
                    }

                }
                chatlistadapter = ChatListAdapter(context!!, userChatList,false)
                recyclerView.adapter = chatlistadapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}

