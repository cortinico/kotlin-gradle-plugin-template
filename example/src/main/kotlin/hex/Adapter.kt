package hex

import example.nested.ExternalAdapter
import example.nested.ExternalDomain

class Adapter : Port

fun main() {
    Domain(Adapter(), ExternalDomain(ExternalAdapter()))
}
