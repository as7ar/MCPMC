package kr.astar.mcpmc.tools

import kr.astar.mcpmc.MCPMC

class MCPMCRegistry {
    companion object {
        @JvmStatic
        fun register(vararg tool: MCPTool) {
            MCPMC.addTools(tool.toList().map { it.toRegisteredTool() })
        }
    }
}