package com.cmp.adminportal.web.rest

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.format.support.FormattingConversionService
import org.springframework.http.MediaType
import javax.persistence.EntityManager

import java.io.IOException
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

import org.assertj.core.api.Assertions.assertThat

private val mapper = createObjectMapper()

/** MediaType for JSON UTF8  */
@JvmField
val APPLICATION_JSON_UTF8: MediaType = MediaType.APPLICATION_JSON_UTF8

private fun createObjectMapper(): ObjectMapper {
    return ObjectMapper().apply {
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        registerModule(JavaTimeModule())
    }
}

/**
 * Convert an object to JSON byte array.
 *
 * @param object the object to convert.
 * @return the JSON byte array.
 * @throws IOException
 */
@Throws(IOException::class)
fun convertObjectToJsonBytes(`object`: Any): ByteArray {
    return mapper.writeValueAsBytes(`object`)
}

/**
 * Create a byte array with a specific size filled with specified data.
 *
 * @param size the size of the byte array.
 * @param data the data to put in the byte array.
 * @return the JSON byte array.
 */
fun createByteArray(size: Int, data: String): ByteArray {
    val byteArray = ByteArray(size)
    for (i in 0 until size) {
        byteArray[i] = java.lang.Byte.parseByte(data, 2)
    }
    return byteArray
}

/**
 * A matcher that tests that the examined string represents the same instant as the reference datetime.
 */
class ZonedDateTimeMatcher(private val date: ZonedDateTime) : TypeSafeDiagnosingMatcher<String>() {

    override fun matchesSafely(item: String, mismatchDescription: Description): Boolean {
        try {
            if (!date.isEqual(ZonedDateTime.parse(item))) {
                mismatchDescription.appendText("was ").appendValue(item)
                return false
            }
            return true
        } catch (e: DateTimeParseException) {
            mismatchDescription.appendText("was ").appendValue(item)
                .appendText(", which could not be parsed as a ZonedDateTime")
            return false
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("a String representing the same Instant as ").appendValue(date)
    }
}

/**
 * Creates a matcher that matches when the examined string represents the same instant as the reference datetime.
 * @param date the reference datetime against which the examined string is checked.
 */
fun sameInstant(date: ZonedDateTime): ZonedDateTimeMatcher {
    return ZonedDateTimeMatcher(date)
}

/**
 * Verifies the equals/hashcode contract on the domain object.
 */
fun <T : Any> equalsVerifier(clazz: KClass<T>) {
    val domainObject1 = clazz.createInstance()
    assertThat(domainObject1.toString()).isNotNull()
    assertThat(domainObject1).isEqualTo(domainObject1)
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode())
    // Test with an instance of another class
    val testOtherObject = Any()
    assertThat(domainObject1).isNotEqualTo(testOtherObject)
    assertThat(domainObject1).isNotEqualTo(null)
    // Test with an instance of the same class
    val domainObject2 = clazz.createInstance()
    assertThat(domainObject1).isNotEqualTo(domainObject2)
    // HashCodes are equals because the objects are not persisted yet
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject2.hashCode())
}

/**
 * Create a [FormattingConversionService] which use ISO date format, instead of the localized one.
 * @return the created [FormattingConversionService].
 */
fun createFormattingConversionService(): FormattingConversionService {
    val dfcs = DefaultFormattingConversionService()
    val registrar = DateTimeFormatterRegistrar()
    registrar.setUseIsoFormat(true)
    registrar.registerFormatters(dfcs)
    return dfcs
}

/**
 * Finds stored objects of the specified type.
 * @param clazz the class type to be searched.
 * @return a list of all found objects.
 * @param <T> the type of objects to be searched.
 */
fun <T : Any> EntityManager.findAll(clazz: KClass<T>): List<T> {
    val cb = this.criteriaBuilder
    val cq = cb.createQuery(clazz.java)
    val rootEntry = cq.from(clazz.java)
    val all = cq.select(rootEntry)
    return this.createQuery(all).resultList
}
