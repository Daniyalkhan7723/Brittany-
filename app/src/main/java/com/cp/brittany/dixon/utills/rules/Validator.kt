package com.cp.brittany.dixon.utills.rules

import com.cp.brittany.dixon.utills.isValidEmail


object Validator {


    fun validateName(fName: String): ValidationResult {
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length >= 2)
        )
    }

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty() && email.isValidEmail())
        )
    }

    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length >= 4)
        )
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return ValidationResult(
            (!confirmPassword.isNullOrEmpty() && confirmPassword.length >= 4 && password == confirmPassword)
        )
    }


}

data class ValidationResult(
    val status: Boolean = false
)








