package com.soyvictorherrera.bdates.data.persistence

class InMemoryTemplatesDataSource {
    private val templates = listOf(
        "¡Feliz cumple, mi ciela! \uD83D\uDC85\nHoy saca esa bichota que llevas dentro. \uD83D\uDED0",
        "¡Feliz cumple! Espero que te la pases bonito el día de hoy ¡Ánimo, ánimo, ánimo! \uD83D\uDC9E\uD83C\uDF82✨",
        "¡Feliz, feliz no cumpleaños! ¡Feliz feliz no cumpleaños!\uD83C\uDF99️... Es broma, si es tu culple. Pásatela bonito, disfruta mucho este día.",
        "¡Feliz cumple, rey! \uD83C\uDF51\uD83D\uDC4B\uD83C\uDFFB\nTóma tu pastel \uD83C\uDF82"
    )

    fun getTemplateList(): List<String> {
        return templates
    }
}