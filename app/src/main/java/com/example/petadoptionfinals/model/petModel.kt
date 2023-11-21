package com.example.petadoptionfinals.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class petModel(

    //var profilePic: String,
    var name : String?=null,
    var gender : String? = null,
    var breed : String? = null,
    var imageUrl : String?=null,
    var userinfo : String?=null,

) : Parcelable
