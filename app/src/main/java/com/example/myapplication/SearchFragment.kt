package com.example.myapplication


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? =null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.search_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        searchEditText = view.findViewById(R.id.search_bar)


        mUsers = ArrayList()
        retrieveAllUsers()

        searchEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUsers(cs.toString().toLowerCase(Locale.ROOT))
            }

        })

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
                (mUsers as ArrayList<Users>).clear()
                for (snapshot in p0.children){
                    val user: Users= snapshot.getValue(Users::class.java)!!
                    if (!(firebaseUserId).equals(user.uid)){
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                userAdapter = UserAdapter(context!!,mUsers!!,false)
                recyclerView?.adapter = userAdapter
            }

        })
    }
    private fun retrieveAllUsers() {
        val firebaseUserId = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("User")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                if (searchEditText?.text.toString() == ""){
                    for (snapshot in p0.children){
                        val user: Users = snapshot.getValue(Users::class.java)!!
                        if (!(firebaseUserId)!!.equals(user.uid)){
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }

                }
                userAdapter = UserAdapter(context!!,mUsers!!,false)
                recyclerView?.adapter = userAdapter
            }

        })

    }

}



