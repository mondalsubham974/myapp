package com.example.myapplication

class Chat (val sender:String ="",val message:String ="",val receiver:String ="",val url:String =""
            ,val messageId:String ="" ,val isseen:Boolean = false,val senderTime:Long = 123456789,receiverTime:Long = 123456789) {
        constructor():this("","","","","",false,123456789,123456789)
    }


