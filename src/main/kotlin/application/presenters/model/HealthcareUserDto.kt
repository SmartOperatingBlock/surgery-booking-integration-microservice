/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenters.model

/**
 * Dto of the healthcareUser with [taxCode], [name], [surname], [height], [weight], [birthdate] and [bloodGroup].
 */
@kotlinx.serialization.Serializable
data class HealthcareUserDto(
    val taxCode: String,
    val name: String,
    val surname: String,
    val height: Double,
    val weight: Double,
    val birthdate: String,
    val bloodGroup: String,
)
