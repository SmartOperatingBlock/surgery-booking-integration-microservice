/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin

import application.SurgeryBookingManager
import com.azure.digitaltwins.core.BasicDigitalTwin
import com.azure.digitaltwins.core.BasicDigitalTwinMetadata
import com.azure.digitaltwins.core.BasicRelationship
import com.azure.digitaltwins.core.DigitalTwinsClient
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder
import com.azure.identity.ClientSecretCredentialBuilder
import entities.SurgeryBooking

/**
 * The Azure endpoint.
 */
const val ENDPOINT = "https://digital-twin-layer.api.neu.digitaltwins.azure.net"

/**
 * The Azure tenant id.
 */
const val TENANT_ID = "8e97c84a-c23e-4463-8faf-d9a974450bc1"

/**
 * The Azure client id.
 */
const val CLIENT_ID = "52e143e0-1993-436f-ac31-8ba5787445e4"

/**
 * The Azure client secret.
 */
const val CLIENT_SECRET = "zuu8Q~0JA2NnKpnXEDMyew4WdBcw.XIjYWwK8b1R"

/**
 * Manager that manage the update of the digital twin on Azure digital twin platform.
 */
class DigitalTwinSurgeryBookingManager : SurgeryBookingManager {

    private val client: DigitalTwinsClient = DigitalTwinsClientBuilder()
        .credential(
            ClientSecretCredentialBuilder()
                .tenantId(TENANT_ID)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .build()
        )
        .endpoint(ENDPOINT)
        .buildClient()

    /**
     * Updates the azure digital twin platform with the surgery booking information.
     */
    override fun manage(surgeryBooking: SurgeryBooking) {
        val digitalTwin = BasicDigitalTwin(surgeryBooking.surgeryID.id)
        digitalTwin.metadata = BasicDigitalTwinMetadata()
            .setModelId("dtmi:io:github:smartoperatingblock:SurgeryBooking;1")
        digitalTwin.contents["surgery_type"] = surgeryBooking.surgeryType.type
        digitalTwin.contents["booking_date_time"] = surgeryBooking.surgeryDateTime.date.toString()
        client.createOrReplaceDigitalTwin(surgeryBooking.surgeryID.id, digitalTwin, BasicDigitalTwin::class.java)

        val surgeryHealthProfessionalRelationShip = createRelationship(
            surgeryBooking.surgeryID.id,
            surgeryBooking.healthProfessionalID.id,
            "rel_responsible_health_professional"
        )

        client.createOrReplaceRelationship(
            surgeryHealthProfessionalRelationShip.sourceId,
            surgeryHealthProfessionalRelationShip.id,
            surgeryHealthProfessionalRelationShip,
            BasicRelationship::class.java
        )

        if (!checkIfDigitalTwinExists(surgeryBooking.healthcareUserID.id)) requestHealthCareUserInformation()
        val surgeryHealthcareUserRelationship = createRelationship(
            surgeryBooking.surgeryID.id,
            surgeryBooking.healthcareUserID.id,
            "rel_healthcare_user"
        )

        client.createOrReplaceRelationship(
            surgeryHealthcareUserRelationship.sourceId,
            surgeryHealthcareUserRelationship.id,
            surgeryHealthcareUserRelationship,
            BasicRelationship::class.java
        )
    }

    /**
     * Create a relationship between two digital twins.
     */
    private fun createRelationship(sourceID: String, targetID: String, relationshipName: String) =
        BasicRelationship("$sourceID-$targetID", sourceID, targetID, relationshipName)

    /**
     * Check if the digital twin exists.
     */
    private fun checkIfDigitalTwinExists(digitalTwinId: String) =
        client.query("SELECT * FROM digitaltwins WHERE \$dtId = '$digitalTwinId'", String::class.java).count() > 0

    /**
     * Get information about the health care user.
     */
    private fun requestHealthCareUserInformation(): Nothing = TODO()
}
