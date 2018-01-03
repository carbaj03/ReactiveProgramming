/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acv.reactiveprogramming;

/**
 * Algebra representing all possible results from a Permission request
 */
sealed class PermissionResult(open val requestCode: Int, open val permission: String) {
    /**
     * Data class representing a successful permission request
     */
    data class Success(override val requestCode: Int, override val permission: String) : PermissionResult(requestCode, permission)

    /**
     * Data class representing a failed permission request
     */
    data class Failure(override val requestCode: Int, override val permission: String) : PermissionResult(requestCode, permission)
}