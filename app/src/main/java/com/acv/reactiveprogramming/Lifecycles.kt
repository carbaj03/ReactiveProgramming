package com.acv.reactiveprogramming


enum class ActivityLifecycle {
    Enter, Create, Start, Resume, Pause, Stop, Destroy, Exit
}

enum class ControllerLifecycle {
    Enter, Create, Attach, Detach, Destroy, Exit
}

object None {
    override fun toString() = "None"
}