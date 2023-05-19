/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenters.deserializer

import entity.SurgeryBooking

/**
 * Interface of a generic data deserializer.
 */
fun interface SurgeryBookingDeserializer<I> {

    /**
     * Deserializes the data.
     * @param data the data to deserialize.
     * @return an instance of surgery booking.
     */
    fun deserialize(data: I): SurgeryBooking
}
