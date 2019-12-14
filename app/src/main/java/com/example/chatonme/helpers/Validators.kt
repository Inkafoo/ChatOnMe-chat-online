package com.example.chatonme.helpers

import android.util.Patterns

class Validators {
    companion object{
        
        fun validateName(name: String): Boolean{
            return name.length > 5
        }

        fun validateEmail(email: String): Boolean{
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun validatePasswordLength(password: String): Boolean{
            return password.length > 5
        }

        fun validatePasswordConfirmation(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword && validatePasswordLength(
                password
            ) && validatePasswordLength(
                confirmPassword
            )
        }

        fun isValidUser(
            name: String,
            email: String,
            password: String,
            confirmPassword: String
        ): Boolean {
            return validateName(name) && validateEmail(
                email
            ) && validatePasswordLength(password) && validatePasswordLength(
                confirmPassword
            ) && validatePasswordConfirmation(
                password,
                confirmPassword
            )
        }

    }
}