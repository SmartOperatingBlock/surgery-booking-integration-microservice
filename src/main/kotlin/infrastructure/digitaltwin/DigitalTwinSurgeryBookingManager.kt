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
import com.azure.digitaltwins.core.implementation.models.ErrorResponseException
import com.azure.identity.ClientSecretCredentialBuilder
import entity.SurgeryBooking
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking

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

    private val digitalTwinClient: DigitalTwinsClient = DigitalTwinsClientBuilder()
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
    override fun createSurgeryBookingDigitalTwin(surgeryBooking: SurgeryBooking): Boolean {
        try {
            digitalTwinClient.createOrReplaceDigitalTwin(
                surgeryBooking.surgeryID.id,
                this.createDigitalTwin(surgeryBooking),
                BasicDigitalTwin::class.java
            )

            val surgeryHealthProfessionalRelationShip = this.createSurgeryHealthProfessionalRelationship(surgeryBooking)
            digitalTwinClient.createOrReplaceRelationship(
                surgeryHealthProfessionalRelationShip.sourceId,
                surgeryHealthProfessionalRelationShip.id,
                surgeryHealthProfessionalRelationShip,
                BasicRelationship::class.java
            )

            if (!checkIfDigitalTwinExists(surgeryBooking.healthcareUserID.id)) {
                requestHealthCareUserInformation(surgeryBooking.healthProfessionalID.id)?.let {
                    createDigitalTwin(
                        it
                    )
                }
            }

            val surgeryHealthcareUserRelationship = this.createSurgeryHealthCareUserRelationship(surgeryBooking)
            digitalTwinClient.createOrReplaceRelationship(
                surgeryHealthcareUserRelationship.sourceId,
                surgeryHealthcareUserRelationship.id,
                surgeryHealthcareUserRelationship,
                BasicRelationship::class.java
            )
            return true
        } catch (e: ErrorResponseException) {
            println(e)
            return false
        }
    }

    /**
     * Create a relationship between surgery booking dt and health professional dt.
     */
    private fun createSurgeryHealthProfessionalRelationship(surgeryBooking: SurgeryBooking) =
        BasicRelationship(
            "${surgeryBooking.surgeryID.id}-${surgeryBooking.healthProfessionalID.id}",
            surgeryBooking.surgeryID.id, surgeryBooking.healthProfessionalID.id,
            "rel_responsible_health_professional"
        )

    /**
     * Create a relationship between surgery booking dt and healthcare user dt.
     */
    private fun createSurgeryHealthCareUserRelationship(surgeryBooking: SurgeryBooking) =
        BasicRelationship(
            "${surgeryBooking.surgeryID.id}-${surgeryBooking.healthcareUserID.id}",
            surgeryBooking.surgeryID.id, surgeryBooking.healthcareUserID.id,
            "rel_healthcare_user"
        )

    /**
     * Check if the digital twin exists.
     */
    private fun checkIfDigitalTwinExists(digitalTwinId: String) =
        digitalTwinClient.query(
            "SELECT * FROM digitaltwins WHERE \$dtId = '$digitalTwinId'", String::class.java
        ).count() > 0

    /**
     * Create a [BasicDigitalTwin] of a surgery booking.
     */
    private fun createDigitalTwin(surgeryBooking: SurgeryBooking): BasicDigitalTwin {
        val digitalTwin = BasicDigitalTwin(surgeryBooking.surgeryID.id)
        digitalTwin.metadata = BasicDigitalTwinMetadata()
            .setModelId("dtmi:io:github:smartoperatingblock:SurgeryBooking;1")
        digitalTwin.contents["surgery_type"] = surgeryBooking.surgeryType.type
        digitalTwin.contents["booking_date_time"] = surgeryBooking.surgeryDateTime.date.toString()
        return digitalTwin
    }

    /**
     * Create a [BasicDigitalTwin] of a healthcare user.
     */
    private fun createDigitalTwin(healtCareUser: HealthcareUser): BasicDigitalTwin {
        val digitalTwin = BasicDigitalTwin(healtCareUser.taxCode)
        digitalTwin.metadata = BasicDigitalTwinMetadata()
            .setModelId("dtmi:io:github:smartoperatingblock:HealthcareUser;1")
        digitalTwin.contents["name"] = healtCareUser.name
        digitalTwin.contents["surname"] = healtCareUser.surname
        return digitalTwin
    }

    /**
     * Get information about the health care user.
     */
    private fun requestHealthCareUserInformation(healthcareUserTaxCode: String): HealthcareUser? =
        runBlocking {
            val client = HttpClient(CIO)
            val response: Map<String, Any> = client.get(
                "https://localhost:8080/api/patients/$healthcareUserTaxCode"
            ).body()
            val name = response["name"]
            val surname = response["surname"]
            if (name != null && surname != null) {
                HealthcareUser(healthcareUserTaxCode, name.toString(), surname.toString())
            } else null
        }
}
