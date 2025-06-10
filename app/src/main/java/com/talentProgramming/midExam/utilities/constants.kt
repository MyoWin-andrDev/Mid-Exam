package com.talentProgramming.midExam.utilities

const val SPLASH_DELAY = 3000L
const val SHARED_PREF_NAME = "MY_PREF"
const val KEY_USERNAME = "usernameLoggedIn"
const val KEY_USER_ID = "userId"
const val KEY_IS_USER_LOGGED_IN = "isUserLoggedIn"
const val INTENT_USERNAME = "USERNAME"
const val INTENT_USERID = "USERID"
val USERNAME_VALID_PATTERN = Regex("^[A-Z](?=.*[0-9])[A-Za-z0-9._@]{5,19}\$")
val PASSWORD_VALID_PATTERN = Regex("^[A-Z](?=.*[0-9])(?=.*[!@#\$%^&*._])[A-Za-z0-9!@#\$%^&*._]{4,18}\$")