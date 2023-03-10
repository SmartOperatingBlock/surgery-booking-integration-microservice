/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application

import entity.SurgeryBooking
import usecase.repository.SurgeryBookingRepository
/**
 * A controller for surgery booking.
 */
class SurgeryBookingController(private val manager: SurgeryBookingManager) : SurgeryBookingRepository {

    override fun createSurgeryBooking(surgeryBooking: SurgeryBooking): Boolean =
        manager.createSurgeryBookingDigitalTwin(surgeryBooking)
}
