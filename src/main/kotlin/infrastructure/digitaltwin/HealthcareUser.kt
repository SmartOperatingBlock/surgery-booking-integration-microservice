/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin

/**
 * Digital twin representation of a Healthcare User.
 * @param taxCode the tax code.
 * @param name the name.
 * @param surname the surname.
 */
data class HealthcareUser(val taxCode: String, val name: String, val surname: String)
