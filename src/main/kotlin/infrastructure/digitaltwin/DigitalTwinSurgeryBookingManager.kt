/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin

import application.SurgeryBookingManager
import application.presenters.model.HealthcareUserDto
import com.azure.digitaltwins.core.BasicDigitalTwin
import com.azure.digitaltwins.core.BasicDigitalTwinMetadata
import com.azure.digitaltwins.core.BasicRelationship
import com.azure.digitaltwins.core.DigitalTwinsClient
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder
import com.azure.digitaltwins.core.implementation.models.ErrorResponseException
import com.azure.identity.DefaultAzureCredentialBuilder
import entity.SurgeryBooking
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

/**
 * Manager that manage the update of the digital twin on Azure digital twin platform.
 */
class DigitalTwinSurgeryBookingManager : SurgeryBookingManager {
    init {
        checkNotNull(System.getenv(dtAppIdVariable)) { "azure client app id required" }
        checkNotNull(System.getenv(dtTenantVariable)) { "azure tenant id required" }
        checkNotNull(System.getenv(dtAppSecretVariable)) { "azure client secret id required" }
        checkNotNull(System.getenv(dtEndpointVariable)) { "azure dt endpoint required" }
        checkNotNull(System.getenv(patientManagementMicroserviceUrl)) { "patient management microservice url required" }
    }

    private val digitalTwinClient = DigitalTwinsClientBuilder()
        .credential(DefaultAzureCredentialBuilder().build())
        .endpoint(System.getenv(dtEndpointVariable))
        .buildClient()

    override fun createSurgeryBookingDigitalTwin(surgeryBooking: SurgeryBooking): Boolean {
        try {
            digitalTwinClient.createOrReplaceDigitalTwin(
                surgeryBooking.surgeryID.id,
                this.createDigitalTwin(surgeryBooking),
                BasicDigitalTwin::class.java,
            )

            val surgeryHealthProfessionalRelationShip = this.createSurgeryHealthProfessionalRelationship(surgeryBooking)
            digitalTwinClient.createOrReplaceRelationship(
                surgeryHealthProfessionalRelationShip.sourceId,
                surgeryHealthProfessionalRelationShip.id,
                surgeryHealthProfessionalRelationShip,
                BasicRelationship::class.java,
            )

            if (!checkIfDigitalTwinExists(surgeryBooking.healthcareUserID.id)) {
                requestHealthCareUserInformation(surgeryBooking.healthcareUserID.id).let {
                    val healthCareUserDT = createDigitalTwin(it)
                    digitalTwinClient.createOrReplaceDigitalTwin(
                        it.taxCode,
                        healthCareUserDT,
                        BasicDigitalTwin::class.java,
                    )
                }
            }

            val patientDT = this.createDigitalTwin(Patient(surgeryBooking.patientID.id))
            digitalTwinClient.createOrReplaceDigitalTwin(patientDT.id, patientDT, BasicDigitalTwin::class.java)

            val surgeryHealthcareUserRelationship = this.createSurgeryHealthCareUserRelationship(surgeryBooking)
            val patientHealthcareUserRelationship = this.createPatientHealthCareUserRelationship(surgeryBooking)
            val patientSurgeryBookingRelationship = this.createPatientSurgeryBookingRelationship(surgeryBooking)

            digitalTwinClient.createOrReplaceRelationship(
                surgeryHealthcareUserRelationship.sourceId,
                surgeryHealthcareUserRelationship.id,
                surgeryHealthcareUserRelationship,
                BasicRelationship::class.java,
            )

            digitalTwinClient.createOrReplaceRelationship(
                patientHealthcareUserRelationship.sourceId,
                patientHealthcareUserRelationship.id,
                patientHealthcareUserRelationship,
                BasicRelationship::class.java,
            )

            digitalTwinClient.createOrReplaceRelationship(
                patientSurgeryBookingRelationship.sourceId,
                patientSurgeryBookingRelationship.id,
                patientSurgeryBookingRelationship,
                BasicRelationship::class.java,
            )
            return true
        } catch (e: ErrorResponseException) {
            println(e)
            return false
        }
    }

    /**
     * Create a relationship between surgery booking dt and patient dt.
     * @param surgeryBooking the surgery booking.
     * @return a [BasicRelationship] between surgery booking and the patient.
     */
    private fun createPatientSurgeryBookingRelationship(surgeryBooking: SurgeryBooking) =
        BasicRelationship(
            "${surgeryBooking.surgeryID.id}-${surgeryBooking.patientID.id}",
            surgeryBooking.surgeryID.id,
            surgeryBooking.patientID.id,
            "rel_booking_associated_patient",
        )

    /**
     * Create a relationship between surgery booking dt and health professional dt.
     * @param surgeryBooking the surgery booking.
     * @return a [BasicRelationship] between surgery booking and the health professional.
     */
    private fun createSurgeryHealthProfessionalRelationship(surgeryBooking: SurgeryBooking) =
        BasicRelationship(
            "${surgeryBooking.surgeryID.id}-${surgeryBooking.healthProfessionalID.id}",
            surgeryBooking.surgeryID.id,
            surgeryBooking.healthProfessionalID.id,
            "rel_responsible_health_professional",
        )

    /**
     * Create a relationship between surgery booking dt and healthcare user dt.
     * @param surgeryBooking the surgery booking.
     * @return a [BasicRelationship] between surgery booking and the healthcare user.
     */
    private fun createSurgeryHealthCareUserRelationship(surgeryBooking: SurgeryBooking) =
        BasicRelationship(
            "${surgeryBooking.surgeryID.id}-${surgeryBooking.healthcareUserID.id}",
            surgeryBooking.surgeryID.id,
            surgeryBooking.healthcareUserID.id,
            "rel_healthcare_user",
        )

    /**
     * Create a relationship between patient dt and healthcare user dt.
     * @param surgeryBooking the surgery booking.
     * @return a [BasicRelationship] between patient and the healthcare user.
     */
    private fun createPatientHealthCareUserRelationship(surgeryBooking: SurgeryBooking) =
        BasicRelationship(
            "${surgeryBooking.patientID.id}-${surgeryBooking.healthcareUserID.id}",
            surgeryBooking.patientID.id,
            surgeryBooking.healthcareUserID.id,
            "rel_is_associated",
        )

    /**
     * Check if the digital twin exists.
     * @param digitalTwinId the id of the digital twin.
     * @return true if the digital twin exists, false otherwise.
     */
    private fun checkIfDigitalTwinExists(digitalTwinId: String): Boolean {
        val res = digitalTwinClient.query(
            "SELECT * FROM digitaltwins WHERE \$dtId = '$digitalTwinId'",
            String::class.java,
        ).count() > 0
        return res
    }

    /**
     * Create a [BasicDigitalTwin] of a surgery booking.
     * @param surgeryBooking the surgery booking.
     * @return a [DigitalTwinsClient] of the surgery booking
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
     * @param healthCareUser the healthcare user.
     * @return a [BasicDigitalTwin] of the healthcare user.
     */
    private fun createDigitalTwin(healthCareUser: HealthcareUser): BasicDigitalTwin {
        val digitalTwin = BasicDigitalTwin(healthCareUser.taxCode)
        digitalTwin.metadata = BasicDigitalTwinMetadata()
            .setModelId("dtmi:io:github:smartoperatingblock:HealthcareUser;1")
        digitalTwin.contents["name"] = healthCareUser.name
        digitalTwin.contents["surname"] = healthCareUser.surname
        return digitalTwin
    }

    /**
     * Create a [BasicDigitalTwin] of a patient.
     * @param patient the patient.
     * @return a [BasicDigitalTwin] of the patient.
     */
    private fun createDigitalTwin(patient: Patient): BasicDigitalTwin {
        val digitalTwin = BasicDigitalTwin(patient.id)
        digitalTwin.metadata = BasicDigitalTwinMetadata()
            .setModelId("dtmi:io:github:smartoperatingblock:Patient;1")
        return digitalTwin
    }

    /**
     * Get information about the health care user.
     * @param healthcareUserTaxCode the tax code of the healthcare user.
     * @return an instance of [HealthcareUser] if the request success, null otherwise.
     */
    private fun requestHealthCareUserInformation(healthcareUserTaxCode: String): HealthcareUser {
        val toRet = runBlocking {
            val url = "${System.getenv(patientManagementMicroserviceUrl)}/patients/$healthcareUserTaxCode"
            val client = HttpClient(CIO)
            val response = Json.decodeFromString<HealthcareUserDto>(client.get(url).body())
            val name = response.name
            val surname = response.surname
            return@runBlocking HealthcareUser(healthcareUserTaxCode, name, surname)
        }
        return toRet
    }

    companion object {
        private const val dtAppIdVariable = "AZURE_CLIENT_ID"
        private const val dtTenantVariable = "AZURE_TENANT_ID"
        private const val dtAppSecretVariable = "AZURE_CLIENT_SECRET"
        private const val dtEndpointVariable = "AZURE_DT_ENDPOINT"
        private const val patientManagementMicroserviceUrl = "PATIENT_MANAGEMENT_INTEGRATION_MICROSERVICE_URL"
    }
}
