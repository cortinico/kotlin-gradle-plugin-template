package hex

import example.nested.NestedPort
import example.nested.nestedAdapter
import java.util.UUID

fun main() {
    val saveUser = saveInMemoryUser()
    val nestedPort: NestedPort = nestedAdapter()

    val createUser = createUser(saveUser, nestedPort)

    createUser(User(UUID.randomUUID()))
}
