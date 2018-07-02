package com.example.clean

import com.google.gson.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Type

class KeyTypeAdapter : JsonSerializer<Key>, JsonDeserializer<Key> {
    companion object {
        const val CLASS_PROPERTY = "class"

        const val EXCEPTION_MESSAGE = "Unable to deserialize Key class instance."
    }

    override fun serialize(src: Key, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = context.serialize(src)
        val jsonObject = json.asJsonObject
        jsonObject.addProperty(CLASS_PROPERTY, src.javaClass.name)
        return jsonObject
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Key? {
        val jsonObject = json.asJsonObject

        try {
            return context.deserialize(jsonObject, Class.forName(jsonObject.get(CLASS_PROPERTY).asString) as Type)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(EXCEPTION_MESSAGE, e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(EXCEPTION_MESSAGE, e)
        } catch (e: InstantiationException) {
            throw RuntimeException(EXCEPTION_MESSAGE, e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(EXCEPTION_MESSAGE, e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(EXCEPTION_MESSAGE, e)
        }
    }
}