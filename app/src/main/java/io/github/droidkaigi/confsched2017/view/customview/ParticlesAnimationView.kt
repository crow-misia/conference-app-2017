package io.github.droidkaigi.confsched2017.view.customview


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.util.AttributeSet
import android.view.View

import java.util.ArrayList
import java.util.Random

import io.github.droidkaigi.confsched2017.R


class ParticlesAnimationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()

    private val particles = ArrayList<Particle>()

    private val lines = ArrayList<Line>()

    init {
        paint.color = Color.WHITE
        setGradientBackground()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            particles.clear()
            particles.addAll(createParticles(MAX_HEXAGONS))

            lines.clear()
            for (i in 0 until particles.size - 1) {
                val particle = particles[i]
                // So there are exactly C(particles.size(), 2) (Mathematical Combination) number of lines, which makes more sense.
                (i + 1 until particles.size).forEach { j -> lines.add(Line(particle, particles[j])) }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lines.clear()
        particles.clear()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        run {
            var i = 0
            val size = particles.size
            while (i < size) {
                particles[i].draw(canvas, paint)
                i++
            }
        }

        var i = 0
        val size = lines.size
        while (i < size) {
            lines[i].draw(canvas, paint)
            i++
        }
    }

    private fun setGradientBackground() {
        val shaderFactory = object : ShapeDrawable.ShaderFactory() {
            override fun resize(width: Int, height: Int): Shader {
                return LinearGradient(0f, height.toFloat(), width.toFloat(), 0f,
                        intArrayOf(ContextCompat.getColor(context, R.color.dark_light_green), (ContextCompat.getColor(context, R.color.dark_light_green) + ContextCompat.getColor(context, R.color.dark_purple)) / 2, ContextCompat.getColor(context, R.color.dark_purple), (ContextCompat.getColor(context, R.color.dark_purple) + ContextCompat.getColor(context, R.color.dark_pink)) / 2, ContextCompat.getColor(context, R.color.dark_pink)),
                        floatArrayOf(0.0f, 0.15f, 0.5f, 0.85f, 1.0f),
                        Shader.TileMode.CLAMP)
            }
        }
        val backgroundPaint = PaintDrawable()
        backgroundPaint.shape = RectShape()
        backgroundPaint.shaderFactory = shaderFactory
        background = backgroundPaint
    }

    private fun createParticles(count: Int): List<Particle> {
        val particles = ArrayList<Particle>()
        for (i in 0 until count) {
            particles.add(Particle(width, height, this))
        }
        return particles
    }

    /**
     * A Pair of 'Particles' whose centers can be 'linked to each other ... Called 'Line'.
     */
    private class Line
    /**
     * Constructor for a Pair.

     * @param first  the first object in the Pair
     * *
     * @param second the second object in the pair
     */
    internal constructor(first: Particle, second: Particle) : Pair<Particle, Particle>(first, second) {

        internal fun draw(canvas: Canvas, paint: Paint) {
            if (!first.shouldBeLinked(LINK_HEXAGON_DISTANCE, second)) {
                return
            }

            val distance = Math.sqrt(
                    Math.pow((second.center.x - first.center.x).toDouble(), 2.0) + Math.pow((second.center.y - first.center.y).toDouble(), 2.0)
            )
            val alpha = MAX_ALPHA - Math.floor(distance * MAX_ALPHA / LINK_HEXAGON_DISTANCE).toInt()
            paint.alpha = alpha
            canvas.drawLine(first.center.x.toFloat(), first.center.y.toFloat(), second.center.x.toFloat(), second.center.y.toFloat(), paint)
        }

        companion object {
            const val MAX_ALPHA = 172
        }
    }

    private class Particle internal constructor(maxWidth: Int, maxHeight: Int, hostWidth: Int, hostHeight: Int) {

        private var scale: Float = 0.toFloat()
        private var alpha: Int = 0
        private var moveSpeed: Float = 0.toFloat()
        private var flashSpeed: Float = 0.toFloat()
        internal val center: Point = Point()
        private val vector: Point = Point()

        internal val path = Path()

        internal constructor(maxWidth: Int, maxHeight: Int, view: View) : this(maxWidth, maxHeight, view.width, view.height) {}

        init {
            reset(maxWidth, maxHeight)
            center.x = (hostWidth - hostWidth * random.nextFloat()).toInt()
            center.y = (hostHeight - hostHeight * random.nextFloat()).toInt()
        }

        internal fun shouldBeLinked(linkedDistance: Int, particle: Particle): Boolean {
            if (this == particle) {
                return false
            }
            // Math.pow(x, 2) and x * x stuff, for your information and my curiosity.
            // ref: http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/5755b2aee8e8/src/share/vm/opto/library_call.cpp#l1799
            val distance = Math.sqrt(
                    Math.pow((particle.center.x - this.center.x).toDouble(), 2.0) + Math.pow((particle.center.y - this.center.y).toDouble(), 2.0)
            )
            return distance < linkedDistance
        }

        internal fun draw(canvas: Canvas, paint: Paint) {
            move(canvas.width, canvas.height)
            paint.alpha = alpha
            createHexagonPathOrUpdate(center.x.toFloat(), center.y.toFloat(), scale)
            canvas.drawPath(this.path, paint)
        }

        private fun move(maxWidth: Int, maxHeight: Int) {
            alpha += flashSpeed.toInt()
            if (alpha < 0) {
                alpha = 0
                flashSpeed = Math.abs(flashSpeed)
            } else if (MAX_ALPHA < alpha) {
                alpha = MAX_ALPHA
                flashSpeed = -Math.abs(flashSpeed)
            }

            center.x += (vector.x * moveSpeed).toInt()
            center.y += (vector.y * moveSpeed).toInt()
            val radius = (BASE_RADIUS * scale).toInt()
            if (center.x < -radius || maxWidth + radius < center.x || center.y < -radius || maxHeight + radius < center.y) {
                reset(maxWidth, maxHeight)
            }
        }

        private fun reset(maxWidth: Int, maxHeight: Int) {
            scale = random.nextFloat() + random.nextFloat()
            alpha = random.nextInt(MAX_ALPHA + 1)
            moveSpeed = random.nextFloat() + random.nextFloat() + 0.5f
            flashSpeed = random.nextInt(8) + 1f

            // point on the edge of the screen
            val radius = (BASE_RADIUS * scale).toInt()
            if (random.nextBoolean()) {
                center.x = (maxWidth - maxWidth * random.nextFloat()).toInt()
                center.y = if (random.nextBoolean()) -radius else maxHeight + radius
            } else {
                center.x = if (random.nextBoolean()) -radius else maxWidth + radius
                center.y = (maxHeight - maxHeight * random.nextFloat()).toInt()
            }

            // move direction
            vector.x = (random.nextInt(5) + 1) * if (random.nextBoolean()) 1 else -1
            vector.y = (random.nextInt(5) + 1) * if (random.nextBoolean()) 1 else -1
            if (center.x == 0) {
                vector.x = Math.abs(vector.x)
            } else if (center.x == maxWidth) {
                vector.x = -Math.abs(vector.x)
            }
            if (center.y == 0) {
                vector.y = Math.abs(vector.y)
            } else if (center.y == maxHeight) {
                vector.y = -Math.abs(vector.y)
            }
        }

        private fun createHexagonPathOrUpdate(centerX: Float, centerY: Float, scale: Float) {
            path.reset()
            val radius = (BASE_RADIUS * scale).toInt()
            for (i in 0..5) {
                val x = (centerX + radius * Math.cos(2.0 * i.toDouble() * Math.PI / 6.0 + Math.PI)).toFloat()
                val y = (centerY - radius * Math.sin(2.0 * i.toDouble() * Math.PI / 6.0 + Math.PI)).toFloat()
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()
        }

        companion object {
            const val MAX_ALPHA = 128
            const val BASE_RADIUS = 100f
        }

    }

    companion object {
        private val TAG = ParticlesAnimationView::class.java.simpleName

        // Use a single static Random generator to ensure the randomness. Also save memory.
        private val random = Random()

        const val MAX_HEXAGONS = 40

        const val LINK_HEXAGON_DISTANCE = 600
    }
}
