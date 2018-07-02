package com.upwork.android.core

import android.os.Parcel
import android.os.Parcelable
import com.example.clean.Key
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import flow.KeyParceler
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter

/**
 * Assumes states are [Parcelable].
 */
class BasicKeyParceler(val gson: Gson) : KeyParceler {
    override fun toParcelable(key: Any): Parcelable {
        if (key is Parcelable) return key

        try {
            val json = encode(key)
            return Wrapper(json)
        } catch (e: IOException) {
            throw RuntimeException("Cannot encode Key to Parcelable", e)
        }

    }

    override fun toKey(parcelable: Parcelable): Any {
        if (parcelable is Key) return parcelable

        val wrapper = parcelable as Wrapper
        try {
            return decode(wrapper.json)
        } catch (e: IOException) {
            throw RuntimeException("Cannot Decode Parcelable to Key", e)
        }
    }

    @Throws(IOException::class)
    private fun encode(key: Any): String {
        val stringWriter = StringWriter()
        JsonWriter(stringWriter).use {
            val type = key.javaClass

            it.beginObject()
            it.name(type.name)
            gson.toJson(key, type, it)
            it.endObject()

            return stringWriter.toString()
        }
    }

    @Throws(IOException::class)
    private fun decode(json: String): Any {
        JsonReader(StringReader(json)).use {
            it.beginObject()

            val type = Class.forName(it.nextName())
            return gson.fromJson(it, type)
        }
    }

    private class Wrapper internal constructor(internal val json: String) : Parcelable {

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            out.writeString(json)
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<Wrapper> = object : Parcelable.Creator<Wrapper> {
                override fun createFromParcel(`in`: Parcel): Wrapper {
                    val json = `in`.readString()
                    return Wrapper(json)
                }

                override fun newArray(size: Int): Array<Wrapper?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}

