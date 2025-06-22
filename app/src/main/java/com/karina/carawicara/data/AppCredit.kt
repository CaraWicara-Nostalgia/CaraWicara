package com.karina.carawicara.data

data class AppCredit(
    val category: String,
    val items: List<CreditItem>
)

data class CreditItem(
    val title: String,
    val source: String,
    val type: String,
    val license: String? = null,
    val url: String? = null,
)
