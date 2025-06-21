package com.app.filmfeed.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.app.filmfeed.UserData
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

val Context.userDataStore: DataStore<UserData> by dataStore(
    fileName = "userData.proto",
    serializer = UserDataSerializer
)

object UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserData {
        try {
            return UserData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserData, output: OutputStream) = t.writeTo(output)
}