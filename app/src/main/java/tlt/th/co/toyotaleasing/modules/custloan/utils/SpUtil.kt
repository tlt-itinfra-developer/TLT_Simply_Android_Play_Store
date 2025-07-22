package tlt.th.co.toyotaleasing.modules.custloan.utils

import android.content.Context
import android.content.SharedPreferences

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by yxm on 16-6-23.
 */
object SpUtil {
    /**
     * File name saved in the phone
     */
    val FILE_NAME = "share_data"


    /**
     * To save the data, we need to get the specific type of the saved data, and then call different save methods according to the type.
     */
    fun put(context: Context, key: String, `object`: Any) {
        put(context, FILE_NAME, key, `object`)
    }

    fun put(context: Context, spName: String, key: String, `object`: Any) {
        val sp = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE)
        val editor = sp.edit()

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

        SharedPreferencesCompat.apply(editor)
    }

    /**
     * To get the method of saving the data, we get the specific type of the saved data according to the default value, and then call the method relative to the method to get the value.
     */
    operator fun get(context: Context, key: String, defaultObject: Any): Any? {
        return get(context, FILE_NAME, key, defaultObject)
    }

    operator fun get(context: Context, spName: String, key: String, defaultObject: Any): Any? {
        val sp = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE)

        if (defaultObject is String) {
            return sp.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            return sp.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            return sp.getLong(key, defaultObject)
        }

        return null
    }

    /**
     * Remove the value that a key value already corresponds to
     */
    fun remove(context: Context, key: String) {
        remove(context, FILE_NAME, key)
    }

    fun remove(context: Context, spName: String, key: String) {
        val sp = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    @JvmOverloads
    fun clear(context: Context, spName: String = FILE_NAME) {
        val sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * Query if a key already exists
     */
    fun contains(context: Context, key: String): Boolean {
        return contains(context, FILE_NAME, key)
    }

    fun contains(context: Context, spName: String, key: String): Boolean {
        val sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return sp.contains(key)
    }

    @JvmOverloads
    fun getAll(context: Context, spName: String = FILE_NAME): Map<String, *> {
        val sp = context.getSharedPreferences(spName,
                Context.MODE_PRIVATE)
        return sp.all
    }

    /**
     * Create a compatibility class that resolves the SharedPreferencesCompat.apply method
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * Reflection method for finding apply
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }

            return null
        }

        /**
         * If you find it, use apply, otherwise use commit
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }
}
/**
 * Clear all data
 */
/**
 * Return all key-value pairs
 */