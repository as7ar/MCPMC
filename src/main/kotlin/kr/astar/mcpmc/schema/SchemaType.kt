package kr.astar.mcpmc.schema

enum class SchemaType(val schema: String) {
    STRING("string"),
    NUMBER("number"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    ARRAY("array"),
    OBJECT("object"),
    NULL("null");

    override fun toString() = this.schema
}