package hex

import example.nested.ExternalAdapter
import example.nested.ExternalDomain
import example.nested.ExternalPort

class Domain(adapter: Port, externalDomain: ExternalDomain) {
    init {
        ExternalDomain(object : ExternalPort {})
    }
}

interface Port
