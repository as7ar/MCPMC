package kr.astar.mcpmc.auth

import kr.astar.mcpmc.MCPMC
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.security.SecureRandom
import java.util.*

object BearerToken {
    private val random = SecureRandom()

    private val file: File by lazy {
        File(MCPMC.plugin.dataFolder, "token").apply {
            if (!exists()) createNewFile()
        }
    }

    private val config: YamlConfiguration by lazy {
        YamlConfiguration.loadConfiguration(file)
    }

    @JvmStatic
    fun generate(
        type: TokenType = TokenType.USER,
        expireSeconds: Long = 3600
    ): String {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)

        val token = Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)

        val expireAt = System.currentTimeMillis() + (expireSeconds * 1000)

        val path = "tokens.$token"
        config.set("$path.type", type.name)
        config.set("$path.expireAt", expireAt)

        save()

        return token
    }

    fun validate(token: String): Boolean {
        val path = "tokens.$token"
        val expireAt = config.getLong("$path.expireAt")

        if (expireAt == 0L) return false

        if (System.currentTimeMillis() > expireAt) {
            config.set(path, null)
            save()
            return false
        }

        return true
    }

    @JvmStatic
    fun getType(token: String): TokenType? {
        val type = config.getString("tokens.$token.type") ?: return null
        return runCatching { TokenType.valueOf(type) }.getOrNull()
    }

    private fun save() {
        config.save(file)
    }

    enum class TokenType {
        ADMIN,
        USER,
        GUEST,
        NO_AUTH
    }
}