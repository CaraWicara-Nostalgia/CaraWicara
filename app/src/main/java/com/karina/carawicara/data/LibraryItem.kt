package com.karina.carawicara.data

import com.karina.carawicara.R

data class LibraryItem(
    val image: Int,
    val text: String,
    val homonym: String,
    val sound: Int
)

val libraryData = mapOf(
    "A" to listOf(
        LibraryItem(R.drawable.air, "Air", "a.ir", R.raw.api),
        LibraryItem(R.drawable.api, "Api", "a.pi", R.raw.api),
        LibraryItem(R.drawable.aku, "Aku", "a.ku", R.raw.aku),
        LibraryItem(R.drawable.asap, "Asap", "a.sap", R.raw.asap),
    ),
    "B" to listOf(
        LibraryItem(R.drawable.balon, "Balon", "ba.lon", R.raw.balon),
        LibraryItem(R.drawable.balok, "Balok", "ba.lok", R.raw.balok),
        LibraryItem(R.drawable.badai, "Badai", "ba.dai", R.raw.badai),
        LibraryItem(R.drawable.bantal, "Bantal", "ban.tal", R.raw.bantal),
    ),
    "C" to listOf(
        LibraryItem(R.drawable.celana, "Celana", "ce.la.na", R.raw.celana),
        LibraryItem(R.drawable.cahaya, "Cahaya", "ca.ha.ya", R.raw.cahaya),
        LibraryItem(R.drawable.cair, "Cair", "ca.ir", R.raw.cair),
        LibraryItem(R.drawable.cincin, "Cincin", "cin.cin", R.raw.cincin),
    )
)

val letterSounds = mapOf(
    "A" to R.raw.a,
    "B" to R.raw.b,
    "C" to R.raw.c
)
