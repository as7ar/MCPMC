package kr.astar.mcpmc.application.tools

import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.tools.MCPTool
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.infoJson
import kr.astar.mcpmc.utils.toToolResult

object MCPTestTool : MCPTool(
    "tool", "Get tool by Name",
    mapOf("name" to SchemaType.STRING)
) {
    override suspend fun call(request: CallToolRequest): CallToolResult {
        val tool = MCPMC.tools.find { it.tool.name == request.getParam("name") }

        if (tool == null) return "tool not found".toToolResult()

        return CallToolResult(listOf(TextContent(tool.infoJson().toString())))
    }
}