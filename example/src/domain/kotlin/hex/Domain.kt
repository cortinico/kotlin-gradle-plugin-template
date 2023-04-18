package hex

import java.util.UUID

data class User(val id: UUID)

fun createUser(saveUser: SaveUser): CreateUser = {
    saveUser(it)
}
