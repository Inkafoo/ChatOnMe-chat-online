package com.example.chatonme.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class User(
    val name: String?="",
    val uid: String?= "",
    val email: String?="",
    val image: String?="",
    val presentation: String?="",
    val country: String?="",
    val age: String?=""

): Parcelable{

   fun toMap(): Map<String, Any?> {
      return mapOf(
         "name" to name,
         "uid" to uid,
         "email" to email,
         "image" to image,
         "presentation" to presentation,
         "country" to country,
         "age" to age
      )
   }

}

