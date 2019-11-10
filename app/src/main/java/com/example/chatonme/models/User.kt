package com.example.chatonme.models


open class User(
    var name: String?="",
    var uid: String?= "",
    var email: String?="",
    var image: String?="",
    var presentation: String?="",
    var country: String?="",
    var age: String?=""
){

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
