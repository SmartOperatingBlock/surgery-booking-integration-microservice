/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenters.deserializer

import application.presenters.deserializer.SurgeryBookingJsonKeys.HEALTH_CARE_USER_ID
import application.presenters.deserializer.SurgeryBookingJsonKeys.HEALTH_PROFESSIONAL_ID
import application.presenters.deserializer.SurgeryBookingJsonKeys.SURGERY_DATE_TIME
import application.presenters.deserializer.SurgeryBookingJsonKeys.SURGERY_ID
import application.presenters.deserializer.SurgeryBookingJsonKeys.SURGERY_TYPE
import com.google.gson.Gson
import com.google.gson.JsonObject
import entities.HealthProfessionalID
import entities.HealthcareUserID
import entities.SurgeryBooking
import entities.SurgeryDateTime
import entities.SurgeryID
import entities.SurgeryType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A json deserializer implementation for [SurgeryBooking].
 */
class SurgeryBookingJsonDeserializer : SurgeryBookingDeserializer<String> {

    /**
     * Deserializes the json returning an instance of [SurgeryBooking].
     *
     */
    override fun deserialize(data: String): SurgeryBooking {
        val jsonObject = Gson().fromJson(data, JsonObject::class.java)
        val surgeryID = SurgeryID(jsonObject.getSurgeryID())
        val surgeryType = SurgeryType(jsonObject.getSurgeryType())
        val healthcareUserID = HealthcareUserID(jsonObject.getHealthcareUserID())
        val healthProfessionalID = HealthProfessionalID(jsonObject.getHealthProfessionalID())
        val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val surgeryDateTime = SurgeryDateTime(LocalDateTime.parse(jsonObject.getSurgeryDateTime(), formatter))
        return SurgeryBooking(surgeryID, surgeryType, healthcareUserID, healthProfessionalID, surgeryDateTime)
    }

    /**
     * Gets the surgery id from [JsonObject].
     */
    private fun JsonObject.getSurgeryID(): String = this[SURGERY_ID].asString

    /**
     * Gets the type of the surgery from [JsonObject].
     */
    private fun JsonObject.getSurgeryType(): String = this[SURGERY_TYPE].asString

    /**
     * Gets the healthcare user id from [JsonObject].
     */
    private fun JsonObject.getHealthcareUserID(): String = this[HEALTH_CARE_USER_ID].asString

    /**
     * Gets the health professional id from [JsonObject].
     */
    private fun JsonObject.getHealthProfessionalID(): String = this[HEALTH_PROFESSIONAL_ID].asString

    /**
     * Gets the health professional id from [JsonObject].
     */
    private fun JsonObject.getSurgeryDateTime(): String = this[SURGERY_DATE_TIME].asString
}
