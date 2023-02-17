/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import application.SurgeryBookingController
import infrastructure.DigitalTwinSurgeryBookingManager
import infrastructure.SurgicalBookingDataReceiver
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

/**
 * Main function.
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Manages http requests to get surgery booking information.
 */
fun Application.module() {
    val surgeryBookingManager = DigitalTwinSurgeryBookingManager()
    val surgeryBookingController = SurgeryBookingController(surgeryBookingManager)
    val surgeryBookingDataReceiver = SurgicalBookingDataReceiver(surgeryBookingController)

    routing {
        post("/") {
            surgeryBookingDataReceiver.receiveSurgeryBookingInformation(call.receiveText())
        }
    }
}
