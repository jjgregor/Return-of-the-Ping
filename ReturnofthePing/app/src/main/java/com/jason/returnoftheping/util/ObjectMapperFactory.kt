package com.jason.returnoftheping.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature


/**
 * Created by Jason on 9/17/17.
 */
class ObjectMapperFactory {

    private var objectMapper: ObjectMapper = ObjectMapper ()

    init {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    fun getObjectMapper(): ObjectMapper {
        return objectMapper
    }
}