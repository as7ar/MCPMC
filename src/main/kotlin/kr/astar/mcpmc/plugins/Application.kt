package kr.astar.mcpmc.plugins

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcpStreamableHttp
import io.modelcontextprotocol.kotlin.sdk.server.mcpWebSocket
import io.modelcontextprotocol.kotlin.sdk.types.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.auth.BearerToken
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.infoJson
import kr.astar.mcpmc.utils.registeredToolGenerator
import kr.astar.mcpmc.utils.toToolResult

private val main = MCPMC.plugin
private val enabledAuth= main.config.getBoolean("auth.enable", false)
private val enabledBearer= main.config.getBoolean("bearer.enable", true)
fun Application.module() {
    install(Authentication) {
        if (enabledAuth) {
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

        if (enabledBearer) {
            bearer("mcpmc-bearer") {
                authenticate { tokenCre->
                    val token = tokenCre.token
                    if (BearerToken.validate(token)) {
                        null
                    } else {
                        UserIdPrincipal("mcpmc-${
                            BearerToken.getType(token)?.name?.lowercase() ?: return@authenticate null
                        }")
                    }
                }
            }
        }
    }

    install(ContentNegotiation) {
        json(McpJson)
    }

    MCPMC.addTools(listOf(
        registeredToolGenerator(
            name = "tool",
            description = "Get tool by Name",
            param = mapOf("name" to SchemaType.STRING)
        ) { req ->

            val tool = MCPMC.tools.find {
                it.tool.name == req.getParam("name")
            }

            if (tool == null) {
                return@registeredToolGenerator "tool not found".toToolResult()
            }

            CallToolResult(
                listOf(TextContent(tool.infoJson().also { println(it) }.toString()))
            )
        },
    ))

    mcpWebSocket {
        Server(
            Implementation("MCPMC", main.pluginMeta.version),
            ServerOptions(
                ServerCapabilities(
                    tools = ServerCapabilities.Tools(listChanged = true),
                    logging = if (MCPMC.plugin.config.getBoolean("enable-log", false))
                        ServerCapabilities.Logging else null
                )
            )
        ) {
            addTools(MCPMC.tools)
        }
    }

    routing {
        authenticate(
            if (enabledAuth) "mcpmc-oauth"
            else if (enabledBearer) "mcpmc-bearer"
            else null
        ) {
            mcpStreamableHttp {
                Server(
                    Implementation("MCPMC", main.pluginMeta.version),
                    ServerOptions(
                        ServerCapabilities(
                            tools = ServerCapabilities.Tools(listChanged = true),
                            logging = if (MCPMC.plugin.config.getBoolean("enable-log", false))
                                ServerCapabilities.Logging else null
                        )
                    )
                ) {
                    addTools(MCPMC.tools)
                }
            }
        }

        get("/status") {
            call.respond(
                buildJsonObject {
                    put("status", JsonPrimitive("ok"))
                }
            )
        }

        get("/callback") {
            val principal = call.principal<OAuthAccessTokenResponse.OAuth2>()
            if (principal != null) {
                call.respondText("Authentication successful! Token: ${principal.accessToken}")
            } else {
                call.respondText("Authentication failed", status = HttpStatusCode.Unauthorized)
            }
        }

        get("/") {
            call.respond(
                buildJsonObject {
                    put("code", JsonPrimitive(200))
                    put("data", buildJsonObject {
                        put("name", JsonPrimitive(main.pluginMeta.name))
                        put("version", JsonPrimitive(main.pluginMeta.version))
                        put(
                            "tools",
                            buildJsonArray {
                                MCPMC.tools.forEach {
                                    add(JsonPrimitive(it.tool.name))
                                }
                            }
                        )
                    })
                }
            )
        }
    }
}