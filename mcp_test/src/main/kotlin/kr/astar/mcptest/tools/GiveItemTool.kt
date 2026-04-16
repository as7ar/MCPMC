package kr.astar.mcptest.tools

import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.papermc.paper.entity.PlayerGiveResult
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.tools.MCPMCTool
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.toToolResult
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GiveItemTool : MCPMCTool(
    "give_item", "give an item to player",
    mapOf(
        "player" to SchemaType.STRING,
        "itemid" to SchemaType.STRING,
        "amount" to SchemaType.INTEGER
    )
) {
    override suspend fun call(request: CallToolRequest): CallToolResult {
        val player = Bukkit.getPlayer(
            request.getParam("player") ?: return "cant found player, is player that have the name online?".toToolResult()
        )
        val item = Material.getMaterial(request.getParam("itemid") ?: "")
            ?: return "item not found".toToolResult()
        val amount = request.getParam("amount")?.toIntOrNull() ?: 0

        var playerItemResult: PlayerGiveResult? = null

        Bukkit.getScheduler().runTask(MCPMC.plugin, Runnable {
            playerItemResult =  player?.give(ItemStack(item, amount))
        })

        return "item given to player. result: $playerItemResult".toToolResult()
    }
}