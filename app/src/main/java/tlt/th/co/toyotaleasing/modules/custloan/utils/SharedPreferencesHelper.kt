package tlt.th.co.toyotaleasing.modules.custloan.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesHelper(context: Context, FILE_NAME: String) {
    private val sharedPreferences: SharedPreferences
    /*
     * Save the name inside the phone
     */private val editor: SharedPreferences.Editor

    /**
     * Return all key-value pairs
     */
    val all: Map<String, *>
        get() = sharedPreferences.all

    init {
        sharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    /**
     * 存储
     */
    fun put(key: String, `object`: Any) {
        if (`object` is String) {
            editor.putString(key, `object`)
        } else if (`object` is Int) {
            editor.putInt(key, `object`)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, `object`)
        } else if (`object` is Float) {
            editor.putFloat(key, `object`)
        } else if (`object` is Long) {
            editor.putLong(key, `object`)
        } else {
            editor.putString(key, `object`.toString())
        }
        editor.commit()
    }

    /**
     * Get saved data
     */
    fun getSharedPreference(key: String, defaultObject: Any): Any? {
        return if (defaultObject is String) {
            sharedPreferences.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            sharedPreferences.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            sharedPreferences.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            sharedPreferences.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            sharedPreferences.getLong(key, defaultObject)
        } else {
            sharedPreferences.getString(key, null)
        }
    }

    /**
     * Remove the value that a key value already corresponds to
     */
    fun remove(key: String) {
        editor.remove(key)
        editor.commit()
    }

    /**
     * Clear all data
     */
    fun clear() {
        editor.clear()
        editor.commit()
    }

    /**
     * Query whether a key exists
     */
    fun contain(key: String): Boolean? {
        return sharedPreferences.contains(key)
    }
}
