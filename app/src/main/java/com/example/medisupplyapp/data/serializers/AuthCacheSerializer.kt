package com.example.medisupplyapp.data.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.medisupplyapp.datastore.AuthCacheProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AuthCacheSerializer : Serializer<AuthCacheProto> {
    override val defaultValue: AuthCacheProto = AuthCacheProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthCacheProto {
        try {
            return AuthCacheProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AuthCacheProto, output: OutputStream) {
        t.writeTo(output)
    }
}
