/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application

import entities.SurgeryBooking

/**
 * Generic manager of a Medical Instrument.
 */
interface SurgeryBookingManager {

    /**
     * Manages the surgery booking information.
     * @param surgeryBooking the surgery booking entity.
     */
    fun createSurgeryBookingDigitalTwin(surgeryBooking: SurgeryBooking): Boolean
}
