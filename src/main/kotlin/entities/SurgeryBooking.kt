/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entities

/**
 * A surgery booking for an elective interventions.
 * @param surgeryID the id of the surgery.
 * @param surgeryType the type of the surgery.
 * @param patientTaxCode the tax code of the patient.
 * @param healthProfessionalID the id of the health professional.
 */
class SurgeryBooking(
    val surgeryID: SurgeryID,
    val surgeryType: SurgeryType,
    val patientTaxCode: PatientTaxCode,
    val healthProfessionalID: HealthProfessionalID
)

/**
 * The id of the surgery.
 * @param id the id.
 */
data class SurgeryID(val id: String)

/**
 * The type of the surgery.
 * @param type the type of the surgery.
 */
data class SurgeryType(val type: String)

/**
 * The tax code of the patient.
 * @param code the patient tax code.
 */
data class PatientTaxCode(val code: String)

/**
 * The id of the health professional assigned for the intervention.
 * @param id the id of the health professional.
 */
data class HealthProfessionalID(val id: String)
