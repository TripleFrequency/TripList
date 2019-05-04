package me.michaelhaas.triplist.ui.view

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class HeaderDecoration(
    private val view: View,
    private val scrollsHorizontally: Boolean,
    private val parallax: Float,
    private val shadowSize: Float,
    private val columns: Int
) : RecyclerView.ItemDecoration() {
    private val shadowPaint: Paint?

    init {
        if (shadowSize > 0) {
            shadowPaint = Paint()
            shadowPaint.shader = if (scrollsHorizontally)
                LinearGradient(
                    shadowSize, 0f, 0f, 0f,
                    intArrayOf(Color.argb(55, 0, 0, 0), Color.argb(55, 0, 0, 0), Color.argb(3, 0, 0, 0)),
                    floatArrayOf(0f, .5f, 1f),
                    Shader.TileMode.CLAMP
                )
            else
                LinearGradient(
                    0f, shadowSize, 0f, 0f,
                    intArrayOf(Color.argb(55, 0, 0, 0), Color.argb(55, 0, 0, 0), Color.argb(3, 0, 0, 0)),
                    floatArrayOf(0f, .5f, 1f),
                    Shader.TileMode.CLAMP
                )
        } else {
            shadowPaint = null
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        // layout basically just gets drawn on the reserved space on top of the first view
        view.layout(parent.left, 0, parent.right, view.measuredHeight)

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            if (parent.getChildAdapterPosition(view) == 0) {
                c.save()
                if (scrollsHorizontally) {
                    c.clipRect(parent.left, parent.top, view.left, parent.bottom)
                    val width = this.view.measuredWidth
                    val left = (view.left - width) * parallax
                    c.translate(left, 0f)
                    this.view.draw(c)
                    if (shadowSize > 0) {
                        c.translate(view.left - left - shadowSize, 0f)
                        shadowPaint?.let {
                            c.drawRect(
                                parent.left.toFloat(),
                                parent.top.toFloat(),
                                shadowSize,
                                parent.bottom.toFloat(),
                                shadowPaint
                            )
                        }
                    }
                } else {
                    c.clipRect(parent.left, parent.top, parent.right, view.top)
                    val height = this.view.measuredHeight
                    val top = (view.top - height) * parallax
                    c.translate(0f, top)
                    this.view.draw(c)
                    if (shadowSize > 0) {
                        c.translate(0f, view.top - top - shadowSize)
                        shadowPaint?.let {
                            c.drawRect(
                                parent.left.toFloat(),
                                parent.top.toFloat(),
                                parent.right.toFloat(),
                                shadowSize,
                                shadowPaint
                            )
                        }
                    }
                }
                c.restore()
                break
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) < columns) {
            if (scrollsHorizontally) {
                if (this.view.measuredWidth <= 0) {
                    this.view.measure(
                        View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.AT_MOST)
                    )
                }
                outRect.set(this.view.measuredWidth, 0, 0, 0)
            } else {
                if (this.view.measuredHeight <= 0) {
                    this.view.measure(
                        View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.AT_MOST)
                    )
                }
                outRect.set(0, this.view.measuredHeight, 0, 0)
            }
        } else {
            outRect.setEmpty()
        }
    }
}