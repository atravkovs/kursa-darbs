package lv.at20057.c8micro.models

data class MandelbrotDto(
        val xc: Double,
        val yc: Double,
        val size: Double,
        val imageSize: Int = 512,
        val maxIterations: Int = 255
) {}