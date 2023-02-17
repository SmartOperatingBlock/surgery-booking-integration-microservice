import application.SurgeryBookingController
import infrastructure.DigitalTwinSurgeryBookingManager
import infrastructure.SurgicalBookingDataReceiver

/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

/**
 * Template for kotlin projects.
 */
fun main() {
    val surgeryBookingManager = DigitalTwinSurgeryBookingManager()
    val surgeryBookingController = SurgeryBookingController(surgeryBookingManager)
    val surgeryBookingDataReceiver = SurgicalBookingDataReceiver(surgeryBookingController)
    val dataSimulation = """
        {
        	"surgeryID" : "surgery1", 
        	"surgeryType" : "aneurysmClipping", 
        	"healthcareUserID" : "hu1",
        	"healthProfessionalID" : "hp1",
        	"surgeryDateTime" : "2023-02-17T09:18:33" 
        }
    """.trimIndent()
    surgeryBookingDataReceiver.receiveSurgeryBookingInformation(dataSimulation)
}
