/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import application.SurgeryBookingController
import infrastructure.Provider
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

/**
 * This class is used for receiving data about surgery booking from third party system.
 */
class SurgicalBookingDataReceiver {

    /**
     * Starts ktor embedded server.
     */
    fun start() {
        embeddedServer(Netty, port = 3000, module = this::dispatcher).start(true)
    }

    /**
     * Dispatcher for http requests.
     */
    fun dispatcher(app: Application) {
        with(app) {
            receiveSurgeryBooking(this)
        }
    }

    /**
     * Receive data about surgery booking.
     */
    private fun receiveSurgeryBooking(app: Application) {
        with(app) {
            routing {
                post("/telemetrySystem") {
                    SurgeryBookingController(Provider.digitalTwinSurgeryBookingManager)
                        .surgeryBookingInformationReceived(call.receiveText())
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
