/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure

import application.SurgeryBookingController

/**
 * This class is used for receiving data about surgery booking from third party system.
 */
class SurgicalBookingDataReceiver(private val controller: SurgeryBookingController) {

    /**
     * Receives data about surgery booking.
     */
    fun receiveSurgeryBookingInformation(data: String) {
        controller.surgeryBookingInformationReceived(data)
    }
}
