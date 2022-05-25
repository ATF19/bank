package com.bsfdv.backend.infrastructure.mapping

import com.bsfdv.backend.infrastructure.core.TechnicalException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component


@Component
class JsonMapper(private val objectMapper: ObjectMapper) {

    fun <T> readFromString(json: String, className: String): T {
        return try {
            val classType = objectMapper.typeFactory.findClass(className)
            objectMapper
                .readerFor(classType)
                .readValue(json)
        } catch (e: JsonMappingException) {
            throw JsonMappingException(e.message ?: "", e)
        } catch (e: ClassNotFoundException) {
            throw JsonMappingException(e.message ?: "", e)
        } catch (e: JsonProcessingException) {
            throw JsonMappingException(e.message ?: "", e)
        }
    }

    fun <T> write(model: T): String {
        return try {
            objectMapper.writeValueAsString(model)
        } catch (e: JsonProcessingException) {
            throw JsonMappingException(e.message!!, e)
        }
    }

}

class JsonMappingException(message: String, e: Throwable) : TechnicalException(message, e)