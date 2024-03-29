/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import java.time.Instant

/**
 * A surgery booking for an elective interventions.
 * @param surgeryID the id of the surgery.
 * @param surgeryType the type of the surgery.
 * @param healthcareUserID the tax code of the patient.
 * @param healthProfessionalID the id of the health professional.
 * @param surgeryDateTime the date time of the surgery.
 * @param patientID the id of the patient.
 */
class SurgeryBooking(
    val surgeryID: SurgeryID,
    val surgeryType: SurgeryType,
    val healthcareUserID: HealthcareUserID,
    val healthProfessionalID: HealthProfessionalID,
    val surgeryDateTime: SurgeryDateTime,
    val patientID: PatientID,
)

/**
 * The id of the surgery.
 * @param id the id.
 */
data class SurgeryID(val id: String) {
    init {
        require(id.isNotEmpty())
    }
}

/**
 * The type of the surgery.
 * @param type the type of the surgery.
 */
data class SurgeryType(val type: String) {
    init {
        require(type.isNotEmpty())
    }
}

/**
 * The id of the healthcare user.
 * @param id the healthcare user id.
 */
data class HealthcareUserID(val id: String) {
    init {
        require(id.isNotEmpty())
    }
}

/**
 * The id of the health professional assigned for the intervention.
 * @param id the id of the health professional.
 */
data class HealthProfessionalID(val id: String) {
    init {
        require(id.isNotEmpty())
    }
}

/**
 * The id of the patient of the intervention.
 * @param id the id of the patient.
 */
data class PatientID(val id: String) {
    init {
        require(id.isNotEmpty())
    }
}

/**
 * The date time of the surgery.
 * @param date the id of the health professional.
 */
data class SurgeryDateTime(val date: Instant) {
    init {
        require(date.isAfter(Instant.now()))
    }
}
