package hex

var inMemoryUser: User? = null

fun saveInMemoryUser(): (User) -> Unit = {
    inMemoryUser = it
}
