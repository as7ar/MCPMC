package kr.astar.mcpmc.application

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.modelcontextprotocol.kotlin.sdk.types.McpJson
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.application.modules.configureRouting
import kr.astar.mcpmc.application.modules.mcpStreaming
import kr.astar.mcpmc.application.tools.MCPTestTool
import kr.astar.mcpmc.auth.BearerToken
import kr.astar.mcpmc.tools.MCPMCRegistry

private val main = MCPMC.plugin
private val enabledAuth = main.config.getBoolean("auth.enable", false)
private val enabledBearer = main.config.getBoolean("bearer.enable", true)

fun Application.module() {
    install(ContentNegotiation) {
        json(McpJson)
    }

    install(Authentication) {
        when(getAuthName()) {
            "mcpmc-oauth"-> {
                oauth("mcpmc-oauth") {
                    urlProvider = { "http://localhost:${main.config.getInt("port", 3001)}/callback" }
                    providerLookup = {
                        OAuthServerSettings.OAuth2ServerSettings(
                            name = "mcpmc-oauth",
                            authorizeUrl = main.config.getString("oauth.authorizeUrl") ?: "",
                            accessTokenUrl = main.config.getString("oauth.accessTokenUrl") ?: "",
                            requestMethod = HttpMethod.parse(main.config.getString("oauth.requestMethod") ?: ""),
                            clientId = main.config.getString("oauth.clientId") ?: "",
                            clientSecret = main.config.getString("oauth.clientSecret") ?: "",
                            defaultScopes = main.config.getStringList("oauth.scope")
                        )
                    }
                    client = HttpClient()
                }
            }

            "mcpmc-bearer"-> {
                bearer("mcpmc-bearer") {
                    authenticate { tokenCre ->
                        val token = tokenCre.token
                        if (BearerToken.validate(token)) {
                            null
                        } else {
                            UserIdPrincipal("mcpmc-${BearerToken.getType(token)?.name?.lowercase() ?: return@authenticate null}")
                        }
                    }
                }
            }
        }
    }

    mcpStreaming()
    configureRouting()

    MCPMCRegistry.register(
        MCPTestTool
    )
}

fun getAuthName(): String? = when {
    enabledAuth -> "mcpmc-oauth"
    enabledBearer -> "mcpmc-bearer"
    else -> null
}