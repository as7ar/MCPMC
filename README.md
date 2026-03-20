# MCPMC

Lightweight MCP (Model Context Protocol) server implementation for Minecraft plugins.

## Features

- Simple MCP server setup
- Tool & resource registration
- Streamable HTTP support
- Designed for modern Minecraft plugin environments

## Installation

```bash
git clone https://github.com/as7ar/MCPMC.git
```

## Usage

### Register Tool

```kotlin
import kr.astar.mcp.MCPMC

MCPMC.addTool(
    registeredToolGenerator(
        name = "hello",
        description = "Simple hello tool",
        param = mapOf(
            "name" to SchemaType.STRING
        )
    ) { req ->
        val name = req.getParam("name") ?: "world"

        CallToolResult(listOf(
            buildJsonObject {
                put("message", "Hello, $name")
            }
        ))
    }
)
```

## Connect

MCPMC exposes a Streamable HTTP endpoint.

Use any MCP-compatible client to connect:

- POST /mcp
- JSON-RPC based communication

## Roadmap

- WebSocket support
- Better tool lifecycle handling
- Plugin auto-discovery

## License

MIT
