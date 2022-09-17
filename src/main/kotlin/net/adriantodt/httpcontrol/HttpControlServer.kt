package net.adriantodt.httpcontrol

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.EndpointGroup
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.time.Duration
import kotlin.reflect.KFunction0

class HttpControlServer(override val kodein: Kodein) : KodeinAware, EndpointGroup {
    private val app: Javalin by instance()
    private val port: Int by instance(tag = "port")

    private val pids = LinkedHashMap<Long, Process>()

    fun init() {
        app.routes(this)
        app.start(port)
    }

    override fun addEndpoints() {
        get("pids") { ctx ->
            ctx.result(pids.mapValues { it.value.toMap() }.toString())
        }
        get("pids/:pid") { ctx ->
            ctx.result(pids[ctx.pathParam("pid").toLong()]!!.toMap().toString())
        }
        get("exit/:pid") { ctx ->
            val p = pids[ctx.pathParam("pid").toLong()]!!
            p.destroy()
            ctx.result(p.exitValue().toString())
        }
        get("app/firefox") { ctx ->
            val p = ProcessBuilder()
                .command("firefox")
                .start()

            pids[p.pid()] = p
            ctx.result(p.toMap().toString())
        }
        get("app/firefox") { ctx ->
            val p = ProcessBuilder()
                .command("firefox")
                .start()

            pids[p.pid()] = p
            ctx.result(p.toMap().toString())
        }
    }
}


fun Process.toMap() = mapOf(
    pair(::pid),
    pair(::info) { info ->
        mapOf(
            pair(info::totalCpuDuration) { it.map(Duration::toMillis).orElse(null) },
            pair(info::startInstant) { it.orElse(null) }
        )
    }
)

private inline fun <T> pair(f: KFunction0<T>) = Pair(f.name, f())
private inline fun <T, U> pair(f: KFunction0<T>, g: (T) -> U) = Pair(f.name, g(f()))