/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenters.deserializer

/**
 * Maintains all the keys present in the json of surgery booking information.
 */
object SurgeryBookingJsonKeys {

    /**
     * The key of the surgery id in the json.
     */
    const val SURGERY_ID = "surgeryID"

    /**
     * The key of the surgery type in the json.
     */
    const val SURGERY_TYPE = "surgeryType"

    /**
     * The key of the patient tax code in the json.
     */
    const val HEALTH_CARE_USER_ID = "healthcareUserID"

    /**
     * The key of the health professional id in the json.
     */
    const val HEALTH_PROFESSIONAL_ID = "healthProfessionalID"

    /**
     * The key of the date time of the surgery in the json.
     */
    const val SURGERY_DATE_TIME = "surgeryDateTime"
}
