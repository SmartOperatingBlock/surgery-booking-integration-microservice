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
import application.presenters.deserializer.SurgeryBookingJsonKeys.PATIENT_ID
import application.presenters.deserializer.SurgeryBookingJsonKeys.SURGERY_DATE_TIME
import application.presenters.deserializer.SurgeryBookingJsonKeys.SURGERY_ID
import application.presenters.deserializer.SurgeryBookingJsonKeys.SURGERY_TYPE
import com.google.gson.Gson
import com.google.gson.JsonObject
import entity.HealthProfessionalID
import entity.HealthcareUserID
import entity.PatientID
import entity.SurgeryBooking
import entity.SurgeryDateTime
import entity.SurgeryID
import entity.SurgeryType
import java.time.Instant

/**
 * A json deserializer implementation for [SurgeryBooking].
 */
class SurgeryBookingJsonDeserializer : SurgeryBookingDeserializer<String> {

    override fun deserialize(data: String): SurgeryBooking {
        val jsonObject = Gson().fromJson(data, JsonObject::class.java)
        val surgeryID = SurgeryID(jsonObject.getSurgeryID())
        val surgeryType = SurgeryType(jsonObject.getSurgeryType())
        val healthcareUserID = HealthcareUserID(jsonObject.getHealthcareUserID())
        val healthProfessionalID = HealthProfessionalID(jsonObject.getHealthProfessionalID())
        val surgeryDateTime = SurgeryDateTime(Instant.parse(jsonObject.getSurgeryDateTime()))
        val patientID = PatientID(jsonObject.getPatientID())
        return SurgeryBooking(
            surgeryID,
            surgeryType,
            healthcareUserID,
            healthProfessionalID,
            surgeryDateTime,
            patientID,
        )
    }

    /**
     * Gets the surgery id from [JsonObject].
     * @return the surgery id.
     */
    private fun JsonObject.getSurgeryID(): String = this[SURGERY_ID].asString

    /**
     * Gets the type of the surgery from [JsonObject].
     * @return the surgery type.
     */
    private fun JsonObject.getSurgeryType(): String = this[SURGERY_TYPE].asString

    /**
     * Gets the healthcare user id from [JsonObject].
     * @return the healthcare user id.
     */
    private fun JsonObject.getHealthcareUserID(): String = this[HEALTH_CARE_USER_ID].asString

    /**
     * Gets the patient id from [JsonObject].
     * @return the patient id.
     */
    private fun JsonObject.getPatientID(): String = this[PATIENT_ID].asString

    /**
     * Gets the health professional id from [JsonObject].
     * @return the health professional id.
     */
    private fun JsonObject.getHealthProfessionalID(): String = this[HEALTH_PROFESSIONAL_ID].asString

    /**
     * Gets the health professional id from [JsonObject].
     * @return the surgery date time.
     */
    private fun JsonObject.getSurgeryDateTime(): String = this[SURGERY_DATE_TIME].asString
}
