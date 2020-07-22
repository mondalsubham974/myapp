package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class FriendRequestFragment : Fragment() {

    private var friendAdapter: FriendAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var requestButton: Button? =null
    private var cancelButton: Button? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_friend_request, container, false)
        recyclerView = view.findViewById(R.id.request_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        requestButton = view.findViewById(R.id.activity_request_item_layout_confirm_friend)
        cancelButton = view.findViewById(R.id.activity_request_item_layout_cancel_button)
        mUsers = ArrayList()



        friendRequest()

        return view


    }







    private fun friendRequest() {
        val firebaseUserId = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("Received")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for (snapshot in p0.children){
                    val user: Users = snapshot.getValue(Users::class.java)!!
                    if (!(firebaseUserId)!!.equals(user.uid)){
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }


                friendAdapter = FriendAdapter(context!!,mUsers!!,false)
                recyclerView?.adapter = friendAdapter
            }

        })
    }


}
class FriendAdapter(private val mcontext: Context, private val mUsers:List<Users>,
                   private val isChatCheck:Boolean): RecyclerView.Adapter<FriendAdapter.ViewHolder?>()
{
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FriendAdapter.ViewHolder {
        val view: View = LayoutInflater.from(mcontext).inflate(R.layout.activity_request_item_layout,viewGroup,false)
        return FriendAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: FriendAdapter.ViewHolder, i: Int) {
        val user: Users = mUsers[i]
        holder.usernameTxt.text = user.username
        Picasso.get().load(user.profile).placeholder(R.drawable.blank_profile_picture).into(holder.profileImageView)


    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var usernameTxt: TextView = itemView.findViewById(R.id.activity_request_item_layout_username)
        var profileImageView: CircleImageView = itemView.findViewById(R.id.activity_request_item_layout_ProfileImage)
        var onlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_online)
        var offlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_offline)
        var lastMessageTxt: TextView = itemView.findViewById(R.id.activity_user_item_layout_lastMessage)
        var confirmFriendButton: Button = itemView.findViewById(R.id.activity_request_item_layout_confirm_friend)
        var cancelFriendButton: Button = itemView.findViewById(R.id.activity_request_item_layout_cancel_button)

    }


}