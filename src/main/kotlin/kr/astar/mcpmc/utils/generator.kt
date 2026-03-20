package kr.astar.mcpmc.utils

import io.modelcontextprotocol.kotlin.sdk.server.ClientConnection
import io.modelcontextprotocol.kotlin.sdk.server.RegisteredTool
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.Tool
import io.modelcontextprotocol.kotlin.sdk.types.ToolSchema
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kr.astar.mcpmc.schema.SchemaType

/**
 * @param param the parameters of the tool, where the key is the parameter name and the value is the parameter type
 * */
fun toolSchemaGenerator(param: Map<String, SchemaType>) = ToolSchema(
    buildJsonObject {
        param.forEach { (key, value) ->
            put(key, buildJsonObject {
                put("type", value.schema)
            })
        }
    }, param.keys.toList()
)

/**
 * @param name the name of the tool
 * @param description the description of the tool
 * @param param the parameters of the tool, where the key is the parameter name and the value is the parameter type
 * */
fun toolGenerator(name: String, description: String, param: Map<String, SchemaType>) = Tool(
    name, toolSchemaGenerator(param), description
)

/**
 * @param tool the tool to register
 * @param call the function to call when the tool is called, which takes a [CallToolRequest] and returns a [CallToolResult]
 * */
fun registeredToolGenerator(
    tool: Tool,
    call: suspend ClientConnection.(CallToolRequest) -> CallToolResult
) = RegisteredTool(tool, call)


/**
 * @param name the name of the tool
 * @param description the description of the tool
 * @param param the parameters of the tool, where the key is the parameter name and the value is the parameter type
 * @param call the function to call when the tool is called, which takes a [CallToolRequest] and returns a [CallToolResult]
 * */
fun registeredToolGenerator(
    name: String, description: String, param: Map<String, SchemaType>,
    call: suspend ClientConnection.(CallToolRequest) -> CallToolResult
) = RegisteredTool(toolGenerator(name, description, param), call)