package kr.astar.mcpmc.ktor.modules

import kr.astar.mcpmc.ktor.tools.GiveItemTool
import kr.astar.mcpmc.ktor.tools.MCPTestTool
import kr.astar.mcpmc.tools.MCPMCToolRegistry

fun registerTools() {
    MCPMCToolRegistry.register(MCPTestTool, GiveItemTool)
}