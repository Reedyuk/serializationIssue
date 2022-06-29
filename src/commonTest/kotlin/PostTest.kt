import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue


private val jsonFormat = Json {
    serializersModule = SerializersModule {
        polymorphic(A::class, B::class, B.serializer())
    }
}

class PostTest {

    lateinit var mockEngine: MockEngine
    lateinit var client: HttpClient

    private fun setup(content: String) {
        mockEngine = MockEngine { request ->
            respond(
                content = content,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        client = HttpClient(mockEngine) {
            expectSuccess = true
            install(Logging)
            install(ContentNegotiation) {
                json(jsonFormat)
            }
        }
    }

    @Test
    fun testFail() = runTest {
        setup(jsonFormat.encodeToString(B("abc", "def") as A))
        val response: A = client.get("https://example.com").body()
        assertTrue(response is B)
    }

    @Test
    fun testFailTwo() = runTest {
        setup(jsonFormat.encodeToString(D("abc", "def") as C))
        val response: C = client.get("https://example.com").body()
        assertTrue(response is D)
    }
}