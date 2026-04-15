package kr.astar.mcpmc.tools

import io.modelcontextprotocol.kotlin.sdk.server.RegisteredTool
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.TextContent

object MCPMCToolRegistry {

    private val tools = mutableListOf<MCPMCTool>()

    fun register(vararg tool: MCPMCTool) {
        tools += tool
    }

    fun getAll(): List<MCPMCTool> = tools
}

val registeredTools = MCPMCToolRegistry.getAll()
    .map { it.toRegisteredTool() }