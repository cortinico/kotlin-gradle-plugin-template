package hex

import example.nested.NestedDomain
import example.nested.NestedPort
import java.util.UUID

data class User(val id: UUID)

fun createUser(saveUser: SaveUser, nestedPort: NestedPort): CreateUser = {
    nestedPort(NestedDomain())
    saveUser(it)
}
