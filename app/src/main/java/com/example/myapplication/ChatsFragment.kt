package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ChatsFragment : Fragment() {
    private var firebaseUser: String? = null
    private var userchatList: List<ChatList>? = null
    private var recyclerView: RecyclerView? = null
    private var chatfragmentList: ArrayList<String>? = null
    private var mchatlistadapter: ChatListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        recyclerView = view.findViewById(R.id.chat_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        userchatList = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("ChatsList").child(firebaseUser.toString())
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (userchatList as  ArrayList).clear()
                for (dataSnapshot in p0.children){
                    val chatlist = dataSnapshot.getValue(ChatList::class.java)
                    (userchatList as  ArrayList).add(chatlist!!)
                }
                retrievechatlist()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return view
    }

    private fun retrievechatlist(){
        chatfragmentList = ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("Confirm Friends/${firebaseUser.toString()}/Friends")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (dataSnapshot in p0.children) {
                    val users = dataSnapshot.key
                    for (eachchatlist in userchatList!!) {
                        if (firebaseUser == eachchatlist.id)
                            (chatfragmentList as ArrayList).add(users.toString())
                    }


                }
                mchatlistadapter = ChatListAdapter(context!!, chatfragmentList!!, false)
                recyclerView!!.adapter = mchatlistadapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}

