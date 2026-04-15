package kr.astar.mcpmc.ktor.modules

import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.utils.registeredToolGenerator
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.infoJson
import kr.astar.mcpmc.utils.toToolResult
import io.modelcontextprotocol.kotlin.sdk.types.*

fun registerTools() {
//    MCPMC.addTools(listOf(
//        registeredToolGenerator(
//            name = "tool",
//            description = "Get tool by Name",
//            param = mapOf("name" to SchemaType.STRING)
//        ) { req ->
//            val tool = MCPMC.tools.find { it.tool.name == req.getParam("name") }
//            if (tool == null) {
//                return@registeredToolGenerator "tool not found".toToolResult()
//            }
//            CallToolResult(listOf(TextContent(tool.infoJson().toString())))
//        }
//    ))

}