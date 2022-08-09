package com.example.datepickerdemo.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import com.example.datepickerdemo.R
import com.example.mpchart.animation.ChartAnimator
import com.example.mpchart.charts.BarChart
import com.example.mpchart.highlight.Highlight
import com.example.mpchart.highlight.Range
import com.example.mpchart.interfaces.dataprovider.BarDataProvider
import com.example.mpchart.interfaces.datasets.IBarDataSet
import com.example.mpchart.renderer.BarChartRenderer
import com.example.mpchart.utils.MPPointF
import com.example.mpchart.utils.Transformer
import com.example.mpchart.utils.Utils
import com.example.mpchart.utils.ViewPortHandler


class RoundedBarChart : BarChart {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        readRadiusAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        readRadiusAttr(context, attrs)
    }

    private fun readRadiusAttr(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.RoundedBarChart, 0, 0)
        try {
            setRadius(a.getFloat(R.styleable.RoundedBarChart_radius, 0F))
        } finally {
            a.recycle()
        }
    }

    fun setRadius(radius: Float) {
        renderer = RoundedBarChartRenderer(this, animator, viewPortHandler, radius)
    }

    class RoundedBarChartRenderer(
        chart: BarDataProvider,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler,
        private val radius: Float
    ) : BarChartRenderer(chart, animator, viewPortHandler) {

        private val path = Path()

        override fun drawHighlighted(c: Canvas, indices: Array<out Highlight>) {
            val barData = mChart.barData

            for (high in indices) {
                val set = barData.getDataSetByIndex(high.dataSetIndex)
                if (set == null || !set.isHighlightEnabled) continue

                val e = set.getEntryForXValue(high.x, high.y)

                if (!isInBoundsX(e, set)) continue

                val trans = mChart.getTransformer(set.axisDependency)

                mHighlightPaint.color = set.highLightColor
                mHighlightPaint.alpha = set.highLightAlpha

                val isStack = high.stackIndex >= 0 && e.isStacked

                val y1: Float
                val y2: Float

                if (isStack) {
                    if (mChart.isHighlightFullBarEnabled) {
                        y1 = e.positiveSum
                        y2 = -e.negativeSum
                    } else {
                        val range: Range = e.ranges[high.stackIndex]
                        y1 = range.from
                        y2 = range.to
                    }
                } else {
                    y1 = e.y
                    y2 = 0f
                }

                prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)

                setHighlightDrawPos(high, mBarRect)

                fillRectRound(
                    mBarRect.left,
                    mBarRect.top,
                    mBarRect.right,
                    mBarRect.bottom,
                    radius,
                    radius,
                    true
                )
                c.drawPath(path, mHighlightPaint)
            }
        }

        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val trans: Transformer = mChart.getTransformer(dataSet.axisDependency)

            mBarBorderPaint.color = dataSet.barBorderColor
            mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)

            val drawBorder = dataSet.barBorderWidth > 0f

            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

            if (mChart.isDrawBarShadowEnabled) {
                mShadowPaint.color = dataSet.barShadowColor
                val barData = mChart.barData
                val barWidth = barData.barWidth
                val barWidthHalf = barWidth / 2.0f
                var x: Float
                var i = 0
                val count = Math.min(
                    Math.ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt(),
                    dataSet.entryCount
                )
                while (i < count) {
                    val e = dataSet.getEntryForIndex(i)
                    x = e.x
                    mBarShadowRectBuffer.left = x - barWidthHalf
                    mBarShadowRectBuffer.right = x + barWidthHalf
                    trans.rectValueToPixel(mBarShadowRectBuffer)
                    if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                        i++
                        continue
                    }
                    if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                    mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                    mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                    c.drawRoundRect(mBarShadowRectBuffer, radius, radius, mShadowPaint)
                    i++
                }
            }

            val buffer = mBarBuffers[index]
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
            buffer.setBarWidth(mChart.barData.barWidth)

            buffer.feed(dataSet)

            trans.pointValuesToPixel(buffer.buffer)

            val isSingleColor = dataSet.colors.size == 1

            if (isSingleColor) {
                mRenderPaint.color = dataSet.color
            }

            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
                if (!isSingleColor) {
                    // Set the color for the currently drawn value. If the index
                    // is out of bounds, reuse colors.
                    mRenderPaint.color = dataSet.getColor(j / 4)
                }

                fillRectRound(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], radius, radius, true
                )

                c.drawPath(path, mRenderPaint)
                if (drawBorder) {
                    c.drawPath(path, mBarBorderPaint)
                }
                j += 4
            }
        }

        override fun drawValues(c: Canvas?) {
            if (isDrawingValuesAllowed(mChart)) {
                val dataSets = mChart.barData.dataSets
                val valueOffsetPlus = Utils.convertDpToPixel(4.5f)
                var posOffset = 0f
                var negOffset = 0f
                val drawValueAboveBar = mChart.isDrawValueAboveBarEnabled
                for (i in 0 until mChart.barData.dataSetCount) {
                    val dataSet = dataSets[i]
                    if (!shouldDrawValues(dataSet)) continue

                    // apply the text-styling defined by the DataSet
                    applyValueTextStyle(dataSet)
                    val isInverted = mChart.isInverted(dataSet.axisDependency)

                    // calculate the correct offset depending on the draw position of
                    // the value
                    val valueTextHeight = Utils.calcTextHeight(mValuePaint, "8").toFloat()
                    posOffset =
                        if (drawValueAboveBar) -valueOffsetPlus else valueTextHeight + valueOffsetPlus
                    negOffset =
                        if (drawValueAboveBar) valueTextHeight + valueOffsetPlus else -valueOffsetPlus
                    if (isInverted) {
                        posOffset = -posOffset - valueTextHeight
                        negOffset = -negOffset - valueTextHeight
                    }

                    // get the buffer
                    val buffer = mBarBuffers[i]
                    val phaseY = mAnimator.phaseY
                    val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
                    iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x)
                    iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y)

                    // if only single values are drawn (sum)
                    if (!dataSet.isStacked) {
                        var j = 0
                        while (j < buffer.buffer.size * mAnimator.phaseX) {
                            val x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f
                            if (!mViewPortHandler.isInBoundsRight(x)) break
                            if (!mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])
                                || !mViewPortHandler.isInBoundsLeft(x)
                            ) {
                                j += 4
                                continue
                            }
                            val entry = dataSet.getEntryForIndex(j / 4)
                            val `val` = entry.y
                            if (dataSet.isDrawValuesEnabled) {
                                drawValue(
                                    c, dataSet.valueFormatter, `val`, entry, i, x,
                                    if (`val` >= 0) (buffer.buffer[j + 3] - buffer.buffer[j + 1]) / 2 + buffer.buffer[j + 1] + posOffset else buffer.buffer[j + 3] + negOffset,
                                    dataSet.getValueTextColor(j / 4)
                                )
                            }
                            if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                                val icon = entry.icon
                                var px = x
                                var py =
                                    if (`val` >= 0) buffer.buffer[j + 1] + posOffset else buffer.buffer[j + 3] + negOffset
                                px += iconsOffset.x
                                py += iconsOffset.y
                                Utils.drawImage(
                                    c,
                                    icon, px.toInt(), py.toInt(),
                                    icon.intrinsicWidth,
                                    icon.intrinsicHeight
                                )
                            }
                            j += 4
                        }

                        // if we have stacks
                    } else {
                        val trans = mChart.getTransformer(dataSet.axisDependency)
                        var bufferIndex = 0
                        var index = 0
                        while (index < dataSet.entryCount * mAnimator.phaseX) {
                            val entry = dataSet.getEntryForIndex(index)
                            val vals = entry.yVals
                            val x =
                                (buffer.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2f
                            val color = dataSet.getValueTextColor(index)

                            // we still draw stacked bars, but there is one
                            // non-stacked
                            // in between
                            if (vals == null) {
                                if (!mViewPortHandler.isInBoundsRight(x)) break
                                if (!mViewPortHandler.isInBoundsY(buffer.buffer[bufferIndex + 1])
                                    || !mViewPortHandler.isInBoundsLeft(x)
                                ) continue
                                if (dataSet.isDrawValuesEnabled) {
                                    drawValue(
                                        c, dataSet.valueFormatter, entry.y, entry, i, x,
                                        buffer.buffer[bufferIndex + 1] +
                                                if (entry.y >= 0) posOffset else negOffset,
                                        color
                                    )
                                }
                                if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                                    val icon = entry.icon
                                    var px = x
                                    var py = buffer.buffer[bufferIndex + 1] +
                                            if (entry.y >= 0) posOffset else negOffset
                                    px += iconsOffset.x
                                    py += iconsOffset.y
                                    Utils.drawImage(
                                        c,
                                        icon, px.toInt(), py.toInt(),
                                        icon.intrinsicWidth,
                                        icon.intrinsicHeight
                                    )
                                }

                                // draw stack values
                            } else {
                                val transformed = FloatArray(vals.size * 2)
                                var posY = 0f
                                var negY = -entry.negativeSum
                                run {
                                    var k = 0
                                    var idx = 0
                                    while (k < transformed.size) {
                                        val value = vals[idx]
                                        var y: Float
                                        if (value == 0.0f && (posY == 0.0f || negY == 0.0f)) {
                                            // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                                            y = value
                                        } else if (value >= 0.0f) {
                                            posY += value
                                            y = posY
                                        } else {
                                            y = negY
                                            negY -= value
                                        }
                                        transformed[k + 1] = y * phaseY
                                        k += 2
                                        idx++
                                    }
                                }
                                trans.pointValuesToPixel(transformed)
                                var k = 0
                                while (k < transformed.size) {
                                    val `val` = vals[k / 2]
                                    val drawBelow = `val` == 0.0f && negY == 0.0f && posY > 0.0f ||
                                            `val` < 0.0f
                                    val y = (transformed[k + 1]
                                            + if (drawBelow) negOffset else posOffset)
                                    if (!mViewPortHandler.isInBoundsRight(x)) break
                                    if (!mViewPortHandler.isInBoundsY(y)
                                        || !mViewPortHandler.isInBoundsLeft(x)
                                    ) {
                                        k += 2
                                        continue
                                    }
                                    if (dataSet.isDrawValuesEnabled) {
                                        drawValue(
                                            c,
                                            dataSet.valueFormatter,
                                            vals[k / 2],
                                            entry,
                                            i,
                                            x,
                                            y,
                                            color
                                        )
                                    }
                                    if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                                        val icon = entry.icon
                                        Utils.drawImage(
                                            c,
                                            icon,
                                            (x + iconsOffset.x).toInt(),
                                            (y + iconsOffset.y).toInt(),
                                            icon.intrinsicWidth,
                                            icon.intrinsicHeight
                                        )
                                    }
                                    k += 2
                                }
                            }
                            bufferIndex =
                                if (vals == null) bufferIndex + 4 else bufferIndex + 4 * vals.size
                            index++
                        }
                    }
                    MPPointF.recycleInstance(iconsOffset)
                }
            }
        }

        private fun fillRectRound(
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            rx: Float,
            ry: Float,
            conformToOriginalPost: Boolean
        ): Path {
            var rx = rx
            var ry = ry
            path.reset()
            if (rx < 0) {
                rx = 0f
            }
            if (ry < 0) {
                ry = 0f
            }
            val width = right - left
            val height = bottom - top
            if (rx > width / 2) {
                rx = width / 2
            }
            if (ry > height / 2) {
                ry = height / 2
            }
            val widthMinusCorners = width - 2 * rx
            val heightMinusCorners = height - 2 * ry
            path.moveTo(right, top + ry)
            path.rQuadTo(0f, -ry, -rx, -ry)
            path.rLineTo(-widthMinusCorners, 0f)
            path.rQuadTo(-rx, 0f, -rx, ry)
            path.rLineTo(0f, heightMinusCorners)
            if (conformToOriginalPost) {
                path.rLineTo(0f, ry)
                path.rLineTo(width, 0f)
                path.rLineTo(0f, -ry)
            } else {
                path.rQuadTo(0f, ry, rx, ry)
                path.rLineTo(widthMinusCorners, 0f)
                path.rQuadTo(rx, 0f, rx, -ry)
            }
            path.rLineTo(0f, -heightMinusCorners)
            path.close()
            return path
        }
    }
}