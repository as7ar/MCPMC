package kr.astar.mcpmc.utils

import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import kotlinx.serialization.json.jsonPrimitive

fun CallToolRequest.getParam(param: String) = this.params.arguments?.get(param)?.jsonPrimitive?.content