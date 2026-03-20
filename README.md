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

Basic example:

```kotlin
mcpStreamableHttp {
    Server(
        Implementation("MCPMC", version),
        ServerOptions(
            ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
                resources = ServerCapabilities.Resources(listChanged = true, subscribe = true),
                logging = ServerCapabilities.Logging
            )
        )
    ) {
        addTools(MCPMC.tools)
    }
}
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
