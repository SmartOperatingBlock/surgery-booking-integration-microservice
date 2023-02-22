/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure

/**
 * A provider for [DigitalTwinSurgeryBookingManager].
 */
object Provider {
    /**
     * The manager of surgery booking digital twin.
     */
    val digitalTwinSurgeryBookingManager = DigitalTwinSurgeryBookingManager()
}
