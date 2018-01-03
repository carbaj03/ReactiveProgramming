package com.acv.reactiveprogramming

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.PermissionChecker.PERMISSION_DENIED
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.support.v7.app.AppCompatActivity
import com.acv.reactiveprogramming.interactor.BoundView
import com.acv.reactiveprogramming.state.bind
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiConsumer

abstract class BaseActivity : AppCompatActivity(), BoundView {
    private val reactiveActivity = ReactiveActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            reactiveActivity.onEnter()
        }
        reactiveActivity.onCreate()
        onAttachBinders()
    }

    abstract fun onAttachBinders()

    override fun onStart() {
        super.onStart()
        reactiveActivity.onStart()
    }

    override fun onResume() {
        super.onResume()
        reactiveActivity.onResume()
    }

    override fun onPause() {
        super.onPause()
        reactiveActivity.onPause()
    }

    override fun onStop() {
        super.onStop()
        reactiveActivity.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        reactiveActivity.onDestroy()
        if (isFinishing) {
            reactiveActivity.onExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                onResultOk(data.extras, requestCode)
            }
            Activity.RESULT_CANCELED -> {
                onResultCanceled(data.extras, requestCode)
            }
        }
    }

    private fun onResultCanceled(extras: Bundle?, requestCode: Int) =
            extras?.let {
                reactiveActivity.onActivityResult(ActivityResult.FailureWithData(requestCode, it.bundleToMap()))
            }.also {
                reactiveActivity.onActivityResult(ActivityResult.Failure(requestCode))
            }

    private fun onResultOk(extras: Bundle?, requestCode: Int) =
            extras?.let {
                reactiveActivity.onActivityResult(ActivityResult.SuccessWithData(requestCode, it.bundleToMap()))
            }.also {
                reactiveActivity.onActivityResult(ActivityResult.Success(requestCode))
            }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.indices.forEachIndexed { _, index ->
            when (grantResults[index]) {
                PERMISSION_GRANTED -> reactiveActivity.onPermissionResult(PermissionResult.Success(requestCode, permissions[index]))
                PERMISSION_DENIED -> reactiveActivity.onPermissionResult(PermissionResult.Failure(requestCode, permissions[index]))
            }
        }
    }

    override fun onBackPressed() =
            reactiveActivity.onBackPressed()

    override fun <T: Any> createBinder(): BiConsumer<Relay<T>, (T) -> Unit> =
            apply(object : Consumer3<Observable<ActivityLifecycle>, Relay<T>, (T) -> Unit> {
                override fun invoke(t1: Observable<ActivityLifecycle>, t2: Relay<T>, t3: (T) -> Unit) {
                    bind(t1, AndroidSchedulers.mainThread(), t2, t3)
                }
            }, createBuddy().lifecycle())

    fun <A, T, U> apply(action3: Consumer3<A, T, U>, first: A): BiConsumer<T, U> =
            BiConsumer { one, two -> action3(first, one, two) }

    interface Consumer3<in T1, in T2, in T3> {
        @Throws(Exception::class)
        operator fun invoke(t1: T1, t2: T2, t3: T3)
    }

    private fun createBuddy(): ActivityReactiveBuddy =
            reactiveActivity.createBuddy()
}

