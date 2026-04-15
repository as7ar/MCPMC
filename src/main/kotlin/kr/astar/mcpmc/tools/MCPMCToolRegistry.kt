package kr.astar.mcpmc.tools

import io.modelcontextprotocol.kotlin.sdk.server.RegisteredTool
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.TextContent

object MCPMCToolRegistry {

    private val tools = mutableListOf<RegisteredTool>()

    @JvmStatic
    fun register(tool: MCPMCTool) {
        tools += tool.toRegisteredTool()
    }

    @JvmStatic
    fun registerAll(vararg tool: MCPMCTool) {
        tools += tool.map { it.toRegisteredTool() }
    }

    fun getAll(): List<RegisteredTool> = tools.toList()
}

