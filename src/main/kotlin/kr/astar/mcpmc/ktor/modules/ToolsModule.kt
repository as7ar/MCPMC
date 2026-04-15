package kr.astar.mcpmc.ktor.modules

import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.utils.registeredToolGenerator
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.infoJson
import kr.astar.mcpmc.utils.toToolResult
import io.modelcontextprotocol.kotlin.sdk.types.*
import kr.astar.mcpmc.ktor.tools.GiveItemTool
import kr.astar.mcpmc.ktor.tools.MCPTestTool
import kr.astar.mcpmc.tools.MCPMCToolRegistry

fun registerTools() {
    MCPMCToolRegistry.register(MCPTestTool, GiveItemTool)
}