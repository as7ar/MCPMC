package kr.astar.mcpmc.ktor.tools

import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.tools.MCPMCTool
import kr.astar.mcpmc.tools.MCPMCToolRegistry
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.infoJson
import kr.astar.mcpmc.utils.toToolResult

class Tool : MCPMCTool {
    override val name: String = "tool"
    override val description: String = "Get tool by Name"
    override val parameters: Map<String, SchemaType> =
        mapOf("name" to SchemaType.STRING)

    override suspend fun call(request: CallToolRequest): CallToolResult {
        val tool = MCPMCToolRegistry.getAll().find { it.tool.name == request.getParam("name") }

        if (tool == null) return "tool not found".toToolResult()

        return CallToolResult(listOf(TextContent(tool.infoJson().toString())))
    }
}
