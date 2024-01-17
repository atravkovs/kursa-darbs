package lv.at20057.c8micro.models

import kotlin.math.*

data class Complex(val real: Double, val imaginary: Double) {

    fun abs(): Double {
        return hypot(real, imaginary);
    }

    fun phase(): Double {
        return atan2(imaginary, real);
    }

    fun plus(b: Complex): Complex {
        val a = this;
        val re = a.real + b.real;
        val im = a.imaginary + b.imaginary;

        return Complex(re, im);
    }

    fun minus(b: Complex): Complex {
        val a = this;
        val re = a.real - b.real;
        val im = a.imaginary - b.imaginary;

        return Complex(re, im);
    }

    fun times(b: Complex): Complex {
        val a = this;
        val re = a.real * b.real - a.imaginary * b.imaginary;
        val im = a.real * b.imaginary + a.imaginary * b.real;

        return Complex(re, im);
    }

    fun scale(alpha: Double): Complex {
        return Complex(real * alpha, imaginary * alpha);
    }

    fun conjugate(): Complex {
        return Complex(real, -imaginary)
    }

    fun reciprocal(): Complex {
        val scale = real * real + imaginary * imaginary;
        return Complex(real / scale, -imaginary / scale)
    }

    fun divides(b: Complex): Complex {
        return this.times(b.reciprocal());
    }

    fun exp(): Complex {
        return Complex(exp(real) * cos(imaginary), exp(real) * sin((imaginary)))
    }

    fun sin(): Complex {
        return Complex(sin(real) * cosh(imaginary), cos(real) * sinh(imaginary))
    }

    fun cos(): Complex {
        return Complex(cos(real) * cosh(imaginary), -sin(real) * sinh(imaginary))
    }

    fun tan(): Complex {
        return this.sin().divides(this.cos())
    }

}