package kr.astar.mcpmc.application.modules

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.application.getAuthName
import kr.astar.mcpmc.auth.BearerToken

fun Application.configureRouting() {
    val authName = getAuthName()

    routing {
        authenticate(authName) {

            get("/") {
                call.respond(buildJsonObject {
                    put("code", JsonPrimitive(200))
                    put("data", buildJsonObject {
                        put("name", JsonPrimitive(MCPMC.plugin.pluginMeta.name))
                        put("version", JsonPrimitive(MCPMC.plugin.pluginMeta.version))
                        put("tools", buildJsonArray {
                             MCPMC.tools.forEach { add(JsonPrimitive(it.tool.name)) }
                        })
                    })
                })
            }
        }

        get("/status") {
            call.respond(buildJsonObject { put("status", JsonPrimitive("ok")) })
        }

        get("/callback") {
            val principal = call.principal<OAuthAccessTokenResponse.OAuth2>()
            if (principal != null) {
                call.respondText("{\"code\": ${HttpStatusCode.OK.value}, \"code\": \"${principal.accessToken}\"}")
            } else {
                call.respondText("{\"code\": ${HttpStatusCode.Unauthorized.value}, \"code\": ${null}}", status = HttpStatusCode.Unauthorized)
            }
        }

        val tokenPath=MCPMC.plugin.config.getString("bearer.token-generator.path") ?: "-"
        val tokenType = MCPMC.plugin.config.getString("bearer.token-generator.token-type") ?: "USER"
        if (tokenPath!="-") get(tokenPath) {
            call.respondText("{\"code\":${HttpStatusCode.OK.value},\"token\": \"${BearerToken.generate(
                BearerToken.TokenType.valueOf(tokenType)
            )}\"}", ContentType.Text.Plain)
        }
    }
}