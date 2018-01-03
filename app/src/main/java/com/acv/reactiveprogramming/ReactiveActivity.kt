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
package com.acv.reactiveprogramming

import com.jakewharton.rxrelay2.BehaviorRelay

/**
 * Delegate class for Android lifecycle responsibilities in an Activity to transform them on reactive streams.
 *
 * It wraps the lifecycle in a more comprehensible approach for this app.
 */
class ReactiveActivity {

    val lifecycleRelay: BehaviorRelay<ActivityLifecycle> = BehaviorRelay.create<ActivityLifecycle>()

    val activityResultRelay: BehaviorRelay<ActivityResult> = BehaviorRelay.create<ActivityResult>()

    val permissionResultRelay: BehaviorRelay<PermissionResult> = BehaviorRelay.create<PermissionResult>()

    val onBackRelay: BehaviorRelay<Unit> = BehaviorRelay.create<Unit>()

    private fun call(lifecycle: ActivityLifecycle) = lifecycleRelay.accept(lifecycle)

    /**
     * To be called on the first time an Activity is created
     */
    fun onEnter() = call(ActivityLifecycle.Enter)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onCreate() = call(ActivityLifecycle.Create)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onStart() = call(ActivityLifecycle.Start)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onResume() = call(ActivityLifecycle.Resume)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onPause() = call(ActivityLifecycle.Pause)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onStop() = call(ActivityLifecycle.Stop)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onDestroy() = call(ActivityLifecycle.Destroy)

    /**
     * To be called when an Activity is finished by a business request
     */
    fun onExit() = call(ActivityLifecycle.Exit)

    /**
     * To be called after receiving a result from another Activity
     */
    fun onActivityResult(result: ActivityResult) = activityResultRelay.accept(result)

    /**
     * To be called after receiving a result of a permission request
     */
    fun onPermissionResult(result: PermissionResult) = permissionResultRelay.accept(result)

    /**
     * To be called when the user presses the back key
     */
    fun onBackPressed() = onBackRelay.accept(Unit)

    /**
     * Creates a proxy object [ActivityReactiveBuddy] to access framework events, like lifecycle.
     *
     * @return a new [ActivityReactiveBuddy]
     */
    fun createBuddy() = object : ActivityReactiveBuddy {
        override fun lifecycle() = lifecycleRelay.hide()

        override fun activityResult() = activityResultRelay.hide()

        override fun permissionResult() = permissionResultRelay.hide()

        override fun back() = onBackRelay.hide()
    }
}