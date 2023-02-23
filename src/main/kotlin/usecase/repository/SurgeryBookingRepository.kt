/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entities.SurgeryBooking

/**
 * Interface that models the repository for surgery booking.
 */
interface SurgeryBookingRepository {
    /**
     * Creates a [SurgeryBooking].
     * @return true if the surgery booking was created, false otherwise.
     */
    fun createSurgeryBooking(surgeryBooking: SurgeryBooking): Boolean
}
