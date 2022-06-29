# Serialization Issue

Currently getting different results based on different platforms.

iOS: Passes testFailtwo but fails testFail

Android: Passes testFail but fails testFailTwo

JS: All tests pass

When a serializable data class inherits an interface or a sealed interface and we cast to the interface first then we get different results per platform.
