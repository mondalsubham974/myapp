package com.example.myapplication

class Users(val uid:String ="",val username:String ="",val profile:String ="",val cover:String ="",val status:String ="",
            val search:String ="",val friend:String ="",val unfriend:String ="",val request_type:String ="") {
    constructor():this("","","","","","","","","")
}