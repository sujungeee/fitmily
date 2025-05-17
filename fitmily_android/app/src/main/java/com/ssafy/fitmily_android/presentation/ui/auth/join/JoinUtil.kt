package com.ssafy.fitmily_android.presentation.ui.auth.join

class JoinUtil{
    fun isInputValid(id: String, pwd: String, pwd2: String, nickname: String, birth: String): Boolean {
        return (id.isNotEmpty()
                && pwd.isNotEmpty()
                && pwd2.isNotEmpty()
                && !nickname.isNullOrEmpty()
                && birth.isNotEmpty())
    }

    fun isFormatValid(id: String, pwd: String, nickname: String, birth: String): Boolean {
        return (isValidId(id)
                && isValidPwd(pwd)
                && isValidNickname(nickname)
                && isValidBirth(birth))
    }

    fun isValidId(id: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]{4,8}$")
        return regex.matches(id)
    }

    fun isValidPwd(pwd: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9!@#\$%]{8,12}$")
        return regex.matches(pwd)
    }

    fun isEqualsPwd(pwd1: String, pwd2: String): Boolean {
        return pwd1 == pwd2
    }

    fun isValidNickname(nickname: String): Boolean {
        val regex = Regex("^[가-힣]{2,8}$")
        return regex.matches(nickname)
    }

    fun isValidBirth(birth: String): Boolean {
        return birth.length == 8 && birth.all { it.isDigit() }
    }
}