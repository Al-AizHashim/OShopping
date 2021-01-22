package com.yemen.oshopping.sharedPreferences

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit


private const val PREF_NAME = "UserId"
private const val PREF_NAMEEMAIL = "UserEmail"
private const val PREF_SEARCH_QUERY = "searchQuery"
    object UserSharedPreferences {

        fun getStoredUserId(context: Context): Int {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_NAME, -1)
        }

        fun setStoredUserId(context: Context, userId:Int) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit {
                    putInt(PREF_NAME, userId)}
        }

        fun getStoredUserEmail(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_NAMEEMAIL, "none")
        }

        fun setStoredEmail(context: Context, userEmail:String) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit {
                    putString(PREF_NAMEEMAIL, userEmail)}
        }

        fun getStoredQuery(context: Context): String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(PREF_SEARCH_QUERY, "")!!
        }
        fun setStoredQuery(context: Context, query: String) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply()
        }
    }