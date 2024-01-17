package lv.at20057.c8micro.services

import lv.at20057.c8micro.models.Complex
import lv.at20057.c8micro.models.MandelbrotDto
import org.springframework.stereotype.Service

@Service
class MandelbrotService {

    private fun calculate(z0: Complex, max: Int): Int {
        var z = z0;

        for (t in 0..<max) {
            if (z.abs() > 2.0) {
                return t;
            }

            z = z.times(z).plus(z0);
        }

        return max;
    }

    fun mandelbrot(config: MandelbrotDto): Array<IntArray> {
        val picture: Array<IntArray> = Array(config.imageSize) { IntArray(config.imageSize) }

        for (i in 0..<config.imageSize) {
            for (j in 0..<config.imageSize) {
                val x0 = config.xc - config.size / 2 + config.size * i / config.imageSize;
                val y0 = config.yc - config.size / 2 + config.size * j / config.imageSize;

                val z0 = Complex(x0, y0)

                val colorIntensity = config.maxIterations - calculate(z0, config.maxIterations);

                val row = picture[i]
                row[config.imageSize - 1 - j] = colorIntensity
            }
        }

        return picture;
    }

}