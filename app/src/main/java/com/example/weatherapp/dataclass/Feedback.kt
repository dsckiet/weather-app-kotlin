package com.example.weatherapp.dataClass

import android.provider.ContactsContract

class Feedback {
    var email = " "
    var msg = " "
    var rating = ""
    constructor(email: String, msg: String ,rating :String){
        this.email = email
        this.msg = msg
        this.rating = rating
    }
}