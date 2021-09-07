package com.example.weatherapp.dataclass

import android.provider.ContactsContract

class Feedback {
    var email = " "
    var msg = " "
    constructor(email: String, msg: String){
        this.email = email
        this.msg = msg
    }
}