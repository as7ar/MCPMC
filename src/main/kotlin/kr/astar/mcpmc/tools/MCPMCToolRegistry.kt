package kr.astar.mcpmc.tools

object MCPMCToolRegistry {

    private val tools = mutableListOf<MCPMCTool>()

    fun register(vararg tool: MCPMCTool) {
        tools += tool
    }

    fun getAll(): List<MCPMCTool> = tools
}

val registeredTools = MCPMCToolRegistry.getAll()
    .map { it.toRegisteredTool() }