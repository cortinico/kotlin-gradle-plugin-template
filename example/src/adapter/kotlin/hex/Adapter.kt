package hex

import example.nested.NestedDomain
import example.nested.NestedPort
import example.nested.nestedAdapter

var inMemoryUser: User? = null

fun saveInMemoryUser(): (User) -> Unit = {
    inMemoryUser = it

    val nestedPort: NestedPort = nestedAdapter()
    nestedPort(NestedDomain())
}
