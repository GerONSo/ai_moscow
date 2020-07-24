package sak.urat.ree

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.max

class GameActivity : AppCompatActivity() {

    val GAME_TIME = 500L

    lateinit var timer: Timer

    var money = 100
    var flowers = 0
    var showed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_game)
        timer = timer(period = GAME_TIME) {
            runOnUiThread {
                addFlower()
            }
        }
    }


    fun addFlower() {
        val set = ConstraintSet()
        val flowerView: ImageView? = ImageView(this)
        val flowerSize = resources.getDimension(R.dimen.flower_size).toInt()
        val flowerViewParams = ConstraintLayout.LayoutParams(flowerSize, flowerSize)
        flowerViewParams.horizontalBias = ((0..100).random() / 100f)
        flowerView?.apply {
            setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.sakuraflowers))
            id = View.generateViewId()
            layoutParams = flowerViewParams
        }
        game_layout.addView(flowerView, 0)
        set.apply {
            clone(game_layout)
            connect(flowerView?.id!!, ConstraintSet.BOTTOM, game_layout.id, ConstraintSet.TOP)
            connect(flowerView.id, ConstraintSet.LEFT, game_layout.id, ConstraintSet.LEFT)
            connect(flowerView.id, ConstraintSet.RIGHT, game_layout.id, ConstraintSet.RIGHT)
            applyTo(game_layout)
        }
        val animation = ObjectAnimator.ofFloat(flowerView, "translationY", 1080f).apply {
            duration = 2000
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
//                    TODO("Not yet implemented")
                }

                override fun onAnimationEnd(p0: Animator?) {
                    flowerView?.let {
                        game_layout?.removeView(flowerView)
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
//                    game_layout.removeView(flowerView)
                }

                override fun onAnimationStart(p0: Animator?) {
//                    TODO("Not yet implemented")
                }
            })
            interpolator = AccelerateInterpolator()
            doOnEnd {
                money -= 20
                tv_money?.text = "$money"
                if (money < 0) {
                    timer.purge()
                    timer.cancel()
                    game_layout?.removeAllViews()
                    if (!showed) {
                        EndDialog(
                            {
                                finish()
                            },
                            flowers
                        ).show(supportFragmentManager, "yesno")
                        showed = true
                    }
                }
            }
            start()
        }
        flowerView?.setOnClickListener {
            money += 30
            flowers++
            animation.end()
            tv_money.text = "$money"
        }
    }
}
