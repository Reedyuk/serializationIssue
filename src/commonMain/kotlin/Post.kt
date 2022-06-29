
import kotlinx.serialization.Serializable

interface A {
    val testA: String
}

@Serializable
sealed interface C {
    val testC: String
}

@Serializable
data class B(
    val testB: String,
    override val testA: String
): A

@Serializable
data class D(
    val testD: String,
    override val testC: String
): C
