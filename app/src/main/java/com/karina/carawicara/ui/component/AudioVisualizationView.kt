package com.karina.carawicara.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.absoluteValue

enum class VisualizationType {
    WAVES,
    SPECTRUM
}

@Composable
fun AudioVisualizationView(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    density: Int = 5,
    type: VisualizationType = VisualizationType.WAVES,
    audioData: ShortArray? = null
) {
    var amplitude by remember { mutableStateOf(0f) }

    fun receive(data: ShortArray) {
        val maxAmplitude = data.maxOrNull()?.toFloat() ?: 0f
        amplitude = maxAmplitude / Short.MAX_VALUE
    }

    audioData?.let {
        receive(it)
    }

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f
        val startX = 0f
        val endX = width
        val stepX = width / density.toFloat()

        val amplitude = height / 4
        val path = Path()
        path.moveTo(startX, centerY)

        when (type) {
            VisualizationType.WAVES -> {
                for (x in startX.toInt() until endX.toInt() step stepX.toInt()) {
                    val y = centerY + amplitude * sin(2 * PI * x / width)
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }
            VisualizationType.SPECTRUM -> {
                audioData?.let { data ->
                    val barWidth = width / data.size.toFloat()
                    data.forEachIndexed { index, value ->
                        val barHeight = (value.toFloat() / Short.MAX_VALUE) * height
                        path.addRect(
                            Rect(
                                left = index * barWidth,
                                top = centerY - barHeight / 2,
                                right = (index + 1) * barWidth,
                                bottom = centerY + barHeight / 2
                            )
                        )
                    }
                }
            }
        }

        drawPath(path, color, style = Stroke(width = 2f))
    }
}

@Composable
fun BarVisualizer(
    data: ShortArray?,
    color: Color = Color.Blue
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val mData = data ?: return@Canvas

        val numBars = mData.size
        val barWidth = size.width / numBars
        val maxHeight = size.height

        mData.forEachIndexed { index, value ->
            val barHeight = (value.toInt().absoluteValue.toFloat() / Short.MAX_VALUE) * maxHeight
            val left = index * barWidth
            val top = maxHeight - barHeight
            val right = left + barWidth
            val bottom = maxHeight
            drawRect(color = color, topLeft = Offset(left, top), size = Size(barWidth, barHeight))
        }
    }
}