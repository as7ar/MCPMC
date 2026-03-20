package kr.astar.mcpmc.utils

import io.modelcontextprotocol.kotlin.sdk.server.RegisteredTool
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.swing.UIManager.put

fun CallToolRequest.getParam(param: String) = this.params.arguments?.get(param)?.jsonPrimitive?.content

fun RegisteredTool.infoJson() = buildJsonObject {
    put("name", tool.name)
    put("description", tool.description)

    put("parameters", buildJsonObject {
        tool.inputSchema.properties?.forEach { (key, value) ->
            put(key, buildJsonObject {
                put("type", value.jsonObject["type"])
            })
        }
    })
}