package com.example.androideletter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SignatureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.parseColor("#263238")
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val path = Path()
    var isDrawn = false

    // String builder untuk merangkai kode SVG
    private val svgPaths = StringBuilder()
    private var currentSvgPath = StringBuilder()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                // Memulai tag path baru pada SVG
                currentSvgPath = StringBuilder()
                currentSvgPath.append("<path d=\"M $x $y ")
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                // Menambahkan garis (Line) ke koordinat baru pada SVG
                currentSvgPath.append("L $x $y ")
                isDrawn = true
            }
            MotionEvent.ACTION_UP -> {
                // Menutup tag path SVG dengan pengaturan ketebalan dan warna yang sama dengan Paint
                currentSvgPath.append("\" stroke=\"#263238\" stroke-width=\"8\" fill=\"none\" stroke-linecap=\"round\" stroke-linejoin=\"round\" />")
                svgPaths.append(currentSvgPath.toString())
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun clear() {
        path.reset()
        svgPaths.clear() // Hapus juga memori SVG-nya
        isDrawn = false
        invalidate()
    }

    // FUNGSI BARU: Mengubah goresan menjadi format teks murni SVG
    fun getSignatureSvg(): String {
        val w = width
        val h = height
        // Membuat bungkus (wrapper) tag <svg> yang utuh
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 $w $h\" width=\"$w\" height=\"$h\">$svgPaths</svg>"
    }
}