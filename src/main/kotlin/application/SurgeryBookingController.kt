/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application

import application.presenters.deserializer.SurgeryBookingJsonDeserializer

/**
 * A controller for surgery booking.
 */
class SurgeryBookingController(private val manager: SurgeryBookingManager) {

    /**
     * Received data about surgery booking.
     */
    fun surgeryBookingInformationReceived(data: String) {
        val deserializer = SurgeryBookingJsonDeserializer()
        val surgeryBooking = deserializer.deserialize(data)
        manager.manage(surgeryBooking)
    }
}
