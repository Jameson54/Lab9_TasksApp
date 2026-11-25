package com.example.tasks.model

enum class SortOrder(val label: String) {
    DATE_ASC("Дата ↑"),
    DATE_DESC("Дата ↓"),
    TITLE_ASC("Алфавит");

    companion object {
        fun from(name: String?): SortOrder =
            entries.firstOrNull { it.name == name } ?: DATE_ASC
    }
}
