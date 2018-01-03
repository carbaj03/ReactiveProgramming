package com.acv.reactiveprogramming;

import io.reactivex.Observable

/**
 * Proxy interface to access Android framework responsibilities of a [ReactiveActivity]
 */
interface ActivityReactiveBuddy {
    /**
     * Non-terminating [Observable] representing the Activity lifecycle
     */
    fun lifecycle(): Observable<ActivityLifecycle>

    /**
     * Non-terminating [Observable] representing all results received for an Activity request
     */
    fun activityResult(): Observable<ActivityResult>

    /**
     * Non-terminating [Observable] representing all results received for a permission request
     */
    fun permissionResult(): Observable<PermissionResult>

    /**
     * Non-terminating [Observable] representing user back presses
     */
    fun back(): Observable<Unit>
}