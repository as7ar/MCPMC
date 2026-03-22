# MCPMC
[![](https://jitpack.io/v/as7ar/mcpmc.svg)](https://jitpack.io/#as7ar/mcpmc)

Lightweight MCP server implementation for Minecraft plugins.

## Features

- Simple MCP server setup
- Tool & resource registration
- Streamable HTTP support
- Designed for modern Minecraft plugin environments

## Installation

```bash
> git clone https://github.com/as7ar/MCPMC.git

> gradlew build
```

## Installation (via JitPack)

### 1. Add JitPack repository

**Gradle (Kotlin DSL)**

```kotlin
repositories {
    maven("https://jitpack.io")
}
```

**Gradle (Groovy)**

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

---

### 2. Add dependency

```kotlin
dependencies {
    implementation("com.github.as7ar:MCPMC:<version>")
}
```


## Usage

### Register Tool

```kotlin
import kr.astar.mcp.MCPMC
import kr.astar.mcpmc.utils.registeredToolGenerator
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.*

MCPMC.addTool(
    registeredToolGenerator(
        name = "hello",
        description = "Simple hello tool",
        param = mapOf(
            "name" to SchemaType.STRING
        )
    ) { req ->
        val name = req.getParam("name") ?: "world"

        "Hello, $name".toToolResult()
    }
)
```

## Connect

MCPMC exposes a Streamable HTTP endpoint.

Use any MCP-compatible client to connect:

- POST /mcp
- JSON-RPC based communication

## TODO
[ ] WebSocket support

## License

MIT
