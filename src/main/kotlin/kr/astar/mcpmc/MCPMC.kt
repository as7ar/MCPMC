package kr.astar.mcpmc

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.modelcontextprotocol.kotlin.sdk.server.RegisteredTool
import kr.astar.mcpmc.plugins.module
import org.bukkit.plugin.java.JavaPlugin

class MCPMC : JavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var plugin: MCPMC
            private set

        internal val tools = mutableListOf<RegisteredTool>()

        @JvmStatic
        fun addTool(tool: RegisteredTool) = tools.add(tool)
    }

    private lateinit var engine: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        saveDefaultConfig()
        engine= embeddedServer(
            Netty, port = config.getInt("3000"),
            host = "0.0.0.0",
            module = Application::module
        ).start(wait = false)
    }

    override fun onDisable() {
        try {
            if (::engine.isInitialized) {
                engine.stop(
                    1000,
                    5000
                )
            }
        } catch (e: NoClassDefFoundError) { return }
    }
}
