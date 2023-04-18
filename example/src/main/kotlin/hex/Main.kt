package hex

import java.util.UUID

fun main() {
    val saveUser = saveInMemoryUser()

    val createUser = createUser(saveUser)

    createUser(User(UUID.randomUUID()))
}
