package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*


class MessageChatActivity : AppCompatActivity() {

    var userIdVisit:String?= ""
    var firebaseUser:FirebaseUser? = null
    var ChatAdapter: ChatAdapter? = null
    var mChatList: List<Chat>? = null
    var reference: DatabaseReference ? = null
    lateinit var mchat_recyclerview: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)






        mchat_profile.setOnClickListener {
            val intent = Intent(this,VisitProfileActivity::class.java)
            intent.putExtra("Visit_id",userIdVisit)
            this.startActivity(intent)
        }
        mchat_username.setOnClickListener {
            val intent = Intent(this,VisitProfileActivity::class.java)
            intent.putExtra("Visit_id",userIdVisit)
            this.startActivity(intent)
        }


        intent = intent
        userIdVisit = intent.getStringExtra("Visit_id")
        Log.d("MessageChatActivity","username->$userIdVisit")
        firebaseUser = FirebaseAuth.getInstance().currentUser

        mchat_recyclerview = findViewById(R.id.mchat_recyclerview)
        mchat_recyclerview.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        mchat_recyclerview.layoutManager = linearLayoutManager

        val refUser = FirebaseDatabase.getInstance().reference.child("User").child(userIdVisit.toString())
        Log.d("MessageChatActivity","username->$refUser")
        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("MainActivity", "user->${p0.exists()}")
                if (p0.exists()) {
                    val user:Users? = p0.getValue(Users::class.java)
                    mchat_username.text = user!!.username
                    Log.d("MainActivity", "user->${user.username}")
                    Picasso.get().load(user.profile).placeholder(R.drawable.blank_profile_picture).into(mchat_profile)

                    retrieveMessages(firebaseUser!!.uid,userIdVisit,user.profile)
                }
            }

        })

        send_btn.setOnClickListener{
            val msg = messageBox.text.toString()
            if (msg == ""){
                Toast.makeText(this,"Please Write Message, first", Toast.LENGTH_LONG).show()
            }
            else{
                sendMessageToUser(firebaseUser!!.uid,userIdVisit,msg)
            }
            messageBox.setText("")
        }
        gallery_btn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Pick image"),438)
        }

    }

    private fun retrieveMessages(senderId: String, receiverId: String?, reciverimageurl: String) {
        mChatList = ArrayList()
        reference = FirebaseDatabase.getInstance().reference.child("Chats")
        Log.d("msgeeee","reciver->$reference")
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat!!.receiver == senderId && chat.sender == receiverId ||
                        chat.receiver == receiverId && chat.sender.equals(senderId)){
                        (mChatList as ArrayList<Chat>).add(chat)

                    }

                    ChatAdapter = ChatAdapter(this@MessageChatActivity,(mChatList as ArrayList<Chat>),reciverimageurl)
                    mchat_recyclerview.adapter = ChatAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun sendMessageToUser(senderId: String, receiverId: String?, msg: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String,Any?>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = msg
        messageHashMap["receiver"] = receiverId
        messageHashMap["isseen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageId"] = messageKey
        reference.child("Chats").child(messageKey!!).setValue(messageHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val chatsListReference =
                        FirebaseDatabase.getInstance().reference.child("ChatsList").child(firebaseUser!!.uid)
                            .child(userIdVisit.toString())
                    chatsListReference.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val chatsListReceiverRef = FirebaseDatabase.getInstance().reference
                                .child("ChatsList").child(firebaseUser!!.uid).child(userIdVisit.toString())
                            if(!p0.exists()){
                                chatsListReference.child("id").setValue(firebaseUser!!.uid)
                            }
                            chatsListReceiverRef.child("id").setValue(firebaseUser!!.uid)
                        }

                    })

                }
            }

    }
    override fun onActivityResult(requestCode: Int,resultCode: Int,data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 438 && resultCode == RESULT_OK && data != null && data.data != null){
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("image is uploading, please wait....")
            progressBar.show()

            val fileUrl = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId= ref.push().key
            val filePath = storageReference.child("$messageId")

            val uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUrl!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful){
                    task.exception?.let {
                        throw it


                    }

                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val downloadUrl = task.result
                    Log.d("Bankura", "user->${downloadUrl}")
                    val mUrl = downloadUrl.toString()
                    Log.d("Bankura", "user->${downloadUrl.toString()}")
                    val messageHashMap = HashMap<String,Any?>()
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["message"] = ""
                    messageHashMap["receiver"] = userIdVisit
                    messageHashMap["isseen"] = false
                    messageHashMap["url"] = mUrl
                    messageHashMap["messageId"] = messageId

                    ref.child("Chats").child(messageId!!).setValue(messageHashMap)

                }
                progressBar.dismiss()
            }

        }


    }
}