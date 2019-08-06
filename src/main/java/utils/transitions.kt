package utils

import javafx.animation.FadeTransition
import javafx.scene.Node
import javafx.util.Duration

fun FadeTransition(duration: Duration, node: Node?, init: FadeTransition.() -> Unit) =
        FadeTransition(duration, node).apply(init)

fun FadeTransition(duration: Duration, init: FadeTransition.() -> Unit) =
        FadeTransition(duration, null, init)

fun FadeTransition(init: FadeTransition.() -> Unit) =
        FadeTransition().apply(init)

fun fadein(d: Duration, n: Node? = null) = FadeTransition(d, n) {
    fromValue = 0.0
    toValue = 1.0
}

fun fadeout(d: Duration, n: Node? = null) = FadeTransition(d, n) {
    fromValue = 1.0
    toValue = 0.0
}
