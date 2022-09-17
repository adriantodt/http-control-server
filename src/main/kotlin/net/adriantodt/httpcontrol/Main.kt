package net.adriantodt.httpcontrol

import io.javalin.Javalin
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun main() {
    Kodein.direct {
        bind<Int>(tag = "port") with instance(7000)
        bind<Javalin>() with singleton {
            Javalin.create {
                it.addStaticFiles("public")
            }
        }
        bind<HttpControlServer>() with singleton { HttpControlServer(kodein) }
    }.instance<HttpControlServer>().init()
}

