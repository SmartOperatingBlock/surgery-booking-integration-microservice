/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entities

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.time.LocalDateTime

class SurgeryBookingTest : StringSpec({
    "surgery id should not be empty" {
        shouldThrow<java.lang.IllegalArgumentException> {
            SurgeryID("")
        }
    }
    "surgery type should not be empty" {
        shouldThrow<java.lang.IllegalArgumentException> {
            SurgeryType("")
        }
    }
    "healthcare user id should not be empty" {
        shouldThrow<java.lang.IllegalArgumentException> {
            HealthcareUserID("")
        }
    }
    "health professional id should not be empty" {
        shouldThrow<java.lang.IllegalArgumentException> {
            HealthProfessionalID("")
        }
    }
    "the surgery date time should be in the future" {
        shouldThrow<java.lang.IllegalArgumentException> {
            SurgeryDateTime(LocalDateTime.MIN)
        }
    }
})
