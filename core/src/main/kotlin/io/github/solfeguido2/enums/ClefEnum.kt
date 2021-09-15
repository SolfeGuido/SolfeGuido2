package io.github.solfeguido2.enums

enum class ClefEnum(
    val icon: IconName,
    val height: Float,
    val iconBaseLine: Float,
    val minNote: Int
) {
    GClef(
        IconName.GClef,
        7.4f,
        1.8f,
        53
    ),
    FClef(IconName.FClef, 5.7f, 3f, 36),
    CClef3(IconName.CClef3, 4.5f, 3.5f, 46),
    CClef4(IconName.CClef4, 4.5f, 4.5f, 50)
}