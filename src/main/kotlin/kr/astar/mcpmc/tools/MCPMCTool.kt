package kr.astar.mcpmc.tools

import io.modelcontextprotocol.kotlin.sdk.server.RegisteredTool
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.utils.registeredToolGenerator

//interface MCPMCTool {
//    val name: String
//    val description: String
//    val parameters: Map<String, SchemaType>
//
//    suspend fun call(request: CallToolRequest): CallToolResult
//}
//
//fun MCPMCTool.toRegisteredTool(): RegisteredTool =
//    registeredToolGenerator(name, description, parameters) { request ->
//        call(request)
//    }

abstract class MCPMCTool(
    val name: String,
    val description: String,
    val parameters: Map<String, SchemaType>
) {
    abstract suspend fun call(request: CallToolRequest): CallToolResult
}

fun MCPMCTool.toRegisteredTool(): RegisteredTool =
    registeredToolGenerator(name, description, parameters) { request ->
        call(request)
    }