package com.sys_ky.colorparette

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import java.util.*


class ColorPalette: ConstraintLayout {

    private val cColorList: List<Array<Int>> =
        listOf(
            arrayOf(255, 255, 255),
            arrayOf(0, 0, 0),
            arrayOf(231, 230, 230),
            arrayOf(68, 84, 106),
            arrayOf(68, 114, 196),
            arrayOf(237, 125, 49),
            arrayOf(168, 165, 165),
            arrayOf(255, 192, 0),
            arrayOf(91, 155, 213),
            arrayOf(112, 173, 71),
            arrayOf(242, 242, 242),
            arrayOf(128, 128, 128),
            arrayOf(208, 206, 206),
            arrayOf(214, 220, 228),
            arrayOf(217, 225, 242),
            arrayOf(252, 228, 214),
            arrayOf(237, 237, 237),
            arrayOf(255, 242, 204),
            arrayOf(221, 235, 247),
            arrayOf(226, 239, 218),
            arrayOf(217, 217, 217),
            arrayOf(89, 89, 89),
            arrayOf(174, 170, 170),
            arrayOf(172, 185, 202),
            arrayOf(180, 198, 231),
            arrayOf(248, 203, 173),
            arrayOf(219, 219, 219),
            arrayOf(255, 230, 153),
            arrayOf(189, 215, 238),
            arrayOf(198, 224, 180),
            arrayOf(191, 191, 191),
            arrayOf(64, 64, 64),
            arrayOf(117, 113, 113),
            arrayOf(132, 151, 176),
            arrayOf(142, 169, 219),
            arrayOf(244, 176, 132),
            arrayOf(201, 201, 201),
            arrayOf(255, 217, 102),
            arrayOf(155, 194, 230),
            arrayOf(169, 208, 142),
            arrayOf(166, 166, 166),
            arrayOf(38, 38, 38),
            arrayOf(58, 56, 56),
            arrayOf(51, 63, 79),
            arrayOf(48, 84, 150),
            arrayOf(198, 89, 17),
            arrayOf(123, 123, 123),
            arrayOf(191, 143, 0),
            arrayOf(47, 117, 181),
            arrayOf(84, 130, 53),
            arrayOf(128, 128, 128),
            arrayOf(13, 13, 13),
            arrayOf(22, 22, 22),
            arrayOf(34, 43, 53),
            arrayOf(32, 55, 100),
            arrayOf(131, 60, 12),
            arrayOf(82, 82, 82),
            arrayOf(128, 96, 0),
            arrayOf(31, 78, 120),
            arrayOf(55, 86, 35),
            arrayOf(192, 0, 0),
            arrayOf(255, 0, 0),
            arrayOf(255, 192, 0),
            arrayOf(255, 255, 0),
            arrayOf(146, 208, 80),
            arrayOf(0, 176, 80),
            arrayOf(0, 176, 240),
            arrayOf(0, 112, 192),
            arrayOf(0, 32, 96),
            arrayOf(114, 48, 160)
            )

    interface OnColorChangeListener: EventListener {
        fun onColorChangeEvent(r: Int, g: Int, b: Int, fixFlg: Boolean)
    }
    private var onColorChangeListener: OnColorChangeListener? = null
    fun setOnColorChangeListener(listener: OnColorChangeListener) {
        onColorChangeListener = listener
    }

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ColorPalette, 0, 0).apply {
            try {
                mEnableFixButton = getBoolean(R.styleable.ColorPalette_enableFixButton, true)
            } finally {
                recycle()
            }
        }
    }

    private var mEnableFixButton: Boolean = true

    private val mOldColorView = View(context)
    private val mNewColorView = View(context)

    private var mOldRed: Int = 0
    private var mOldGreen: Int = 0
    private var mOldBlue: Int = 0
    private var mRed: Int = 0
    private var mGreen: Int = 0
    private var mBlue: Int = 0

    init {
        this.post {
            initializeView()
        }
    }

    private fun initializeView() {
        this.background = resources.getDrawable(R.drawable.border_rectangle, null)

        //region 旧色と新色
        val nowTextView = TextView(context)
        nowTextView.id = View.generateViewId()
        nowTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        nowTextView.gravity = Gravity.CENTER_VERTICAL
        nowTextView.setTextColor(Color.BLACK)
        nowTextView.text = "　現在　"
        this.addView(nowTextView)

        val constraintLayout1 = ConstraintLayout(context)
        constraintLayout1.id = View.generateViewId()
        constraintLayout1.background = resources.getDrawable(R.drawable.border_rectangle2, null)
        this.addView(constraintLayout1)

        mOldColorView.id = View.generateViewId()
        constraintLayout1.addView(mOldColorView)

        val constraintLayout2 = ConstraintLayout(context)
        constraintLayout2.id = View.generateViewId()
        constraintLayout2.background = resources.getDrawable(R.drawable.border_rectangle2, null)
        this.addView(constraintLayout2)

        mNewColorView.id = View.generateViewId()
        constraintLayout2.addView(mNewColorView)

        val newTextView = TextView(context)
        newTextView.id = View.generateViewId()
        newTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        newTextView.gravity = Gravity.CENTER_VERTICAL
        newTextView.setTextColor(Color.BLACK)
        newTextView.text = "　新規　"
        this.addView(newTextView)

        var constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.constrainWidth(nowTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(nowTextView.id, height / 10)
        constraintSet.connect(nowTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
        constraintSet.connect(nowTextView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 2)
        constraintSet.applyTo(this)

        constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.constrainWidth(constraintLayout1.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(constraintLayout1.id, height / 10)
        constraintSet.connect(constraintLayout1.id, ConstraintSet.START, nowTextView.id, ConstraintSet.END, 0)
        constraintSet.connect(constraintLayout1.id, ConstraintSet.END, constraintLayout2.id, ConstraintSet.START, 0)
        constraintSet.connect(constraintLayout1.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 8)
        constraintSet.applyTo(this)

        constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout1)
        constraintSet.constrainWidth(mOldColorView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(mOldColorView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.connect(mOldColorView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
        constraintSet.connect(mOldColorView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
        constraintSet.connect(mOldColorView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 2)
        constraintSet.connect(mOldColorView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 2)
        constraintSet.applyTo(constraintLayout1)

        constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.constrainWidth(constraintLayout2.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(constraintLayout2.id, height / 10)
        constraintSet.connect(constraintLayout2.id, ConstraintSet.START, constraintLayout1.id, ConstraintSet.END, 0)
        constraintSet.connect(constraintLayout2.id, ConstraintSet.END, newTextView.id, ConstraintSet.START, 0)
        constraintSet.connect(constraintLayout2.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 8)
        constraintSet.applyTo(this)

        constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout2)
        constraintSet.constrainWidth(mNewColorView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(mNewColorView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.connect(mNewColorView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
        constraintSet.connect(mNewColorView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
        constraintSet.connect(mNewColorView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 2)
        constraintSet.connect(mNewColorView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 2)
        constraintSet.applyTo(constraintLayout2)

        constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.constrainWidth(newTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(newTextView.id, height / 10)
        constraintSet.connect(newTextView.id, ConstraintSet.START, constraintLayout2.id, ConstraintSet.END, 0)
        constraintSet.connect(newTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
        constraintSet.connect(newTextView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 2)
        constraintSet.applyTo(this)

        val line = View(context)
        line.id = View.generateViewId()
        line.setBackgroundColor(Color.BLACK)
        this.addView(line)
        constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.constrainWidth(line.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(line.id, 2)
        constraintSet.connect(line.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
        constraintSet.connect(line.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
        constraintSet.connect(line.id, ConstraintSet.TOP, nowTextView.id, ConstraintSet.BOTTOM, 10)
        constraintSet.applyTo(this)
        //endregion

        //region ok、cancelボタン
        val fixButton = Button(context)
        val cancelButton = Button(context)
        if (mEnableFixButton) {
            fixButton.id = View.generateViewId()
            fixButton.text = resources.getText(R.string.ok)
            fixButton.setOnClickListener {
                bootColorChangeEvent(true)
            }
            this.addView(fixButton)

            cancelButton.id = View.generateViewId()
            cancelButton.text = resources.getText(R.string.cancel)
            cancelButton.setOnClickListener {
                bootColorChangeEvent(true, cancelFlg = true)
            }
            this.addView(cancelButton)

            constraintSet = ConstraintSet()
            constraintSet.clone(this)
            constraintSet.constrainWidth(fixButton.id, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.constrainHeight(fixButton.id, ConstraintSet.WRAP_CONTENT)
            constraintSet.connect(fixButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
            constraintSet.connect(fixButton.id, ConstraintSet.END, cancelButton.id, ConstraintSet.START, 2)
            constraintSet.connect(fixButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 2)
            constraintSet.applyTo(this)

            constraintSet = ConstraintSet()
            constraintSet.clone(this)
            constraintSet.constrainWidth(cancelButton.id, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.constrainHeight(cancelButton.id, ConstraintSet.WRAP_CONTENT)
            constraintSet.connect(cancelButton.id, ConstraintSet.START, fixButton.id, ConstraintSet.END, 2)
            constraintSet.connect(cancelButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
            constraintSet.connect(cancelButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 2)
            constraintSet.applyTo(this)
        }
        //endregion

        //region 縦スクロール
        val scrollView = ScrollView(context)
        scrollView.id = View.generateViewId()
        this.addView(scrollView)

        constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.constrainWidth(scrollView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(scrollView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.connect(scrollView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
        constraintSet.connect(scrollView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
        constraintSet.connect(scrollView.id, ConstraintSet.TOP, line.id, ConstraintSet.BOTTOM, 0)
        if (mEnableFixButton) {
            constraintSet.connect(scrollView.id, ConstraintSet.BOTTOM, fixButton.id, ConstraintSet.TOP, 2)
        } else {
            constraintSet.connect(scrollView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 2)
        }
        constraintSet.applyTo(this)

        val scrollConstraintLayout = ConstraintLayout(context)
        scrollConstraintLayout.id = View.generateViewId()
        scrollView.addView(scrollConstraintLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //endregion

        //region シークバー
        val inTextView1 = TextView(context)
        inTextView1.id = View.generateViewId()
        inTextView1.gravity = Gravity.CENTER
        inTextView1.setTextColor(Color.BLACK)
        inTextView1.text = "　R"
        scrollConstraintLayout.addView(inTextView1)

        val redTextView = TextView(context)
        redTextView.id = View.generateViewId()
        redTextView.gravity = Gravity.CENTER
        redTextView.setTextColor(Color.BLACK)
        redTextView.text = "255　"
        redTextView.post {
            redTextView.minWidth = redTextView.width
            redTextView.text = mRed.toString().padStart(3, ' ') + "　"
        }
        scrollConstraintLayout.addView(redTextView)

        val redSeekBar = SeekBar(context)
        redSeekBar.id = View.generateViewId()
        redSeekBar.max = 255
        redSeekBar.progress = mRed
        redSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mRed = p1
                redTextView.text = mRed.toString().padStart(3, ' ') + "　"
                setNewColor()
                bootColorChangeEvent(false)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    mRed = p0.progress
                    redTextView.text = mRed.toString().padStart(3, ' ') + "　"
                    setNewColor()
                    bootColorChangeEvent(false)
                }
            }
        })
        scrollConstraintLayout.addView(redSeekBar)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(inTextView1.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(inTextView1.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(inTextView1.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
        constraintSet.connect(inTextView1.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 30)
        constraintSet.applyTo(scrollConstraintLayout)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(redTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(redTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(redTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
        constraintSet.connect(redTextView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 30)
        constraintSet.applyTo(scrollConstraintLayout)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(redSeekBar.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(redSeekBar.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(redSeekBar.id, ConstraintSet.START, inTextView1.id, ConstraintSet.END, 0)
        constraintSet.connect(redSeekBar.id, ConstraintSet.END, redTextView.id, ConstraintSet.START, 0)
        constraintSet.connect(redSeekBar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 30)
        constraintSet.applyTo(scrollConstraintLayout)

        val inTextView2 = TextView(context)
        inTextView2.id = View.generateViewId()
        inTextView2.gravity = Gravity.CENTER
        inTextView2.setTextColor(Color.BLACK)
        inTextView2.text = "　G"
        scrollConstraintLayout.addView(inTextView2)

        val greenTextView = TextView(context)
        greenTextView.id = View.generateViewId()
        greenTextView.gravity = Gravity.CENTER
        greenTextView.setTextColor(Color.BLACK)
        greenTextView.text = "255　"
        greenTextView.post {
            greenTextView.minWidth = greenTextView.width
            greenTextView.text = mGreen.toString().padStart(3, ' ') + "　"
        }
        scrollConstraintLayout.addView(greenTextView)

        val greenSeekBar = SeekBar(context)
        greenSeekBar.id = View.generateViewId()
        greenSeekBar.max = 255
        greenSeekBar.progress = mGreen
        greenSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mGreen = p1
                greenTextView.text = mGreen.toString().padStart(3, ' ') + "　"
                setNewColor()
                bootColorChangeEvent(false)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    mGreen = p0.progress
                    greenTextView.text = mGreen.toString().padStart(3, ' ') + "　"
                    setNewColor()
                    bootColorChangeEvent(false)
                }
            }
        })
        scrollConstraintLayout.addView(greenSeekBar)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(inTextView2.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(inTextView2.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(inTextView2.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
        constraintSet.connect(inTextView2.id, ConstraintSet.TOP, inTextView1.id, ConstraintSet.BOTTOM, 30)
        constraintSet.applyTo(scrollConstraintLayout)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(greenTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(greenTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(greenTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
        constraintSet.connect(greenTextView.id, ConstraintSet.TOP, inTextView1.id, ConstraintSet.BOTTOM, 30)
        constraintSet.applyTo(scrollConstraintLayout)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(greenSeekBar.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(greenSeekBar.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(greenSeekBar.id, ConstraintSet.START, inTextView1.id, ConstraintSet.END, 0)
        constraintSet.connect(greenSeekBar.id, ConstraintSet.END, greenTextView.id, ConstraintSet.START, 0)
        constraintSet.connect(greenSeekBar.id, ConstraintSet.TOP, inTextView1.id, ConstraintSet.BOTTOM, 30)
        constraintSet.applyTo(scrollConstraintLayout)


        val inTextView3 = TextView(context)
        inTextView3.id = View.generateViewId()
        inTextView3.gravity = Gravity.CENTER
        inTextView3.setTextColor(Color.BLACK)
        inTextView3.text = "　B"
        scrollConstraintLayout.addView(inTextView3)

        val blueTextView = TextView(context)
        blueTextView.id = View.generateViewId()
        blueTextView.gravity = Gravity.CENTER
        blueTextView.setTextColor(Color.BLACK)
        blueTextView.text = "255　"
        blueTextView.post {
            blueTextView.minWidth = blueTextView.width
            blueTextView.text = mBlue.toString().padStart(3, ' ') + "　"
        }
        scrollConstraintLayout.addView(blueTextView)

        val blueSeekBar = SeekBar(context)
        blueSeekBar.id = View.generateViewId()
        blueSeekBar.max = 255
        blueSeekBar.progress = mBlue
        blueSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mBlue = p1
                blueTextView.text = mBlue.toString().padStart(3, ' ') + "　"
                setNewColor()
                bootColorChangeEvent(false)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    mBlue = p0.progress
                    blueTextView.text = mBlue.toString().padStart(3, ' ') + "　"
                    setNewColor()
                    bootColorChangeEvent(false)
                }
            }
        })
        scrollConstraintLayout.addView(blueSeekBar)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(inTextView3.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(inTextView3.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(inTextView3.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 2)
        constraintSet.connect(inTextView3.id, ConstraintSet.TOP, inTextView2.id, ConstraintSet.BOTTOM, 30)
        constraintSet.applyTo(scrollConstraintLayout)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(blueTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(blueTextView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(blueTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 2)
        constraintSet.connect(blueTextView.id, ConstraintSet.TOP, inTextView2.id, ConstraintSet.BOTTOM, 30)
        constraintSet.applyTo(scrollConstraintLayout)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(blueSeekBar.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(blueSeekBar.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(blueSeekBar.id, ConstraintSet.START, inTextView1.id, ConstraintSet.END, 0)
        constraintSet.connect(blueSeekBar.id, ConstraintSet.END, greenTextView.id, ConstraintSet.START, 0)
        constraintSet.connect(blueSeekBar.id, ConstraintSet.TOP, inTextView2.id, ConstraintSet.BOTTOM, 30)
        constraintSet.applyTo(scrollConstraintLayout)
        //endregion

        //region パレット
        val colorHorizontalScrollView = HorizontalScrollView(context)
        colorHorizontalScrollView.id = View.generateViewId()
        scrollConstraintLayout.addView(colorHorizontalScrollView)

        constraintSet = ConstraintSet()
        constraintSet.clone(scrollConstraintLayout)
        constraintSet.constrainWidth(colorHorizontalScrollView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(colorHorizontalScrollView.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(colorHorizontalScrollView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 20)
        constraintSet.connect(colorHorizontalScrollView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 20)
        constraintSet.connect(colorHorizontalScrollView.id, ConstraintSet.TOP, inTextView3.id, ConstraintSet.BOTTOM, 40)
        constraintSet.connect(colorHorizontalScrollView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(scrollConstraintLayout)

        val colorScrollConstraintLayout = ConstraintLayout(context)
        colorScrollConstraintLayout.id = View.generateViewId()
        colorHorizontalScrollView.addView(colorScrollConstraintLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        var cnt: Int = 0
        val colorViewIdList: MutableList<Int> = mutableListOf<Int>()
        var topMargin: Int = 20
        var bottomMargin: Int = 0
        var sideMargin: Int = 20
        if (width >= 764) {
            sideMargin += (width - 764) / 2
        }
        cColorList.forEach { color ->

            val colorView = View(context)
            colorView.id = View.generateViewId()
            colorView.tag = cnt
            colorView.minimumHeight = 50
            colorView.minimumWidth = 50
            val colorViewDrawable = GradientDrawable()
            colorViewDrawable.setStroke(2, Color.LTGRAY)
            colorViewDrawable.setColor(Color.rgb(color[0], color[1], color[2]))
            colorView.background = colorViewDrawable

            colorView.setOnClickListener {
                val index: Int = it.tag as Int
                mRed = cColorList[index][0]
                mGreen = cColorList[index][1]
                mBlue = cColorList[index][2]
                findViewById<TextView>(redTextView.id).text = mRed.toString().padStart(3, ' ') + "　"
                findViewById<TextView>(greenTextView.id).text = mGreen.toString().padStart(3, ' ') + "　"
                findViewById<TextView>(blueTextView.id).text = mBlue.toString().padStart(3, ' ') + "　"
                findViewById<SeekBar>(redSeekBar.id).progress = mRed
                findViewById<SeekBar>(greenSeekBar.id).progress = mGreen
                findViewById<SeekBar>(blueSeekBar.id).progress = mBlue

                bootColorChangeEvent(false)
                setNewColor()
            }
            colorScrollConstraintLayout.addView(colorView)

            constraintSet = ConstraintSet()
            constraintSet.clone(colorScrollConstraintLayout)
            constraintSet.constrainWidth(colorView.id, ConstraintSet.WRAP_CONTENT)
            constraintSet.constrainHeight(colorView.id, ConstraintSet.WRAP_CONTENT)
            if (cnt == 0) {
                constraintSet.connect(colorView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, sideMargin)
                constraintSet.connect(colorView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topMargin)
            } else if (cnt % 10 == 0) {
                if (cnt / 10 == 6) {
                    topMargin = 40
                    bottomMargin = 20
                }
                constraintSet.connect(colorView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, sideMargin)
                constraintSet.connect(colorView.id, ConstraintSet.TOP, colorViewIdList[cnt - 10], ConstraintSet.BOTTOM, topMargin)
                if (bottomMargin != 0) {
                    constraintSet.connect(colorView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
                }
            } else {
                constraintSet.connect(colorView.id, ConstraintSet.START, colorViewIdList[cnt - 1], ConstraintSet.END, 20)
                constraintSet.connect(colorView.id, ConstraintSet.TOP, colorViewIdList[cnt - 1], ConstraintSet.TOP, 0)
                if (cnt % 10 == 9) {
                    constraintSet.connect(colorView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, sideMargin)
                }
                if (bottomMargin != 0) {
                    constraintSet.connect(colorView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottomMargin)
                }
            }
            constraintSet.applyTo(colorScrollConstraintLayout)

            colorViewIdList.add(cnt, colorView.id)
            cnt++
        }
        //endregion

        setOldColor()
        setNewColor()
    }

    fun setColor(r: Int, g: Int, b: Int) {
        mRed = r
        mGreen = g
        mBlue = b
        setOldColor()
        setNewColor()
    }

    private fun setOldColor() {
        mOldRed = mRed
        mOldGreen = mGreen
        mOldBlue = mBlue
        mOldColorView.setBackgroundColor(Color.rgb(mRed,mGreen,mBlue))
    }
    private fun setNewColor() {
        mNewColorView.setBackgroundColor(Color.rgb(mRed,mGreen,mBlue))
    }

    private fun bootColorChangeEvent(fixFlg: Boolean) {
        bootColorChangeEvent(fixFlg, false)
    }

    private fun bootColorChangeEvent(fixFlg: Boolean, cancelFlg: Boolean) {
        if (onColorChangeListener != null) {
            if (!cancelFlg) {
                onColorChangeListener!!.onColorChangeEvent(mRed, mGreen, mBlue, fixFlg)
            } else {
                onColorChangeListener!!.onColorChangeEvent(mOldRed, mOldGreen, mOldBlue, fixFlg)
            }
        }
    }
}