/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenters.deserializer

import entities.SurgeryBooking

/**
 * Interface of a generic data deserializer.
 */
interface SurgeryBookingDeserializer <I> {

    /**
     * Deserializes the data.
     */
    fun deserialize(data: I): SurgeryBooking
}
