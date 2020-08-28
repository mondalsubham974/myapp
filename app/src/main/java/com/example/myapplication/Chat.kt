package com.example.myapplication

class Chat (val sender:String ="",val message:String ="",val receiver:String ="",val url:String ="",val messageId:String ="",
                val isseen:Boolean = false) {
        constructor():this("","","","","",false)
    }



