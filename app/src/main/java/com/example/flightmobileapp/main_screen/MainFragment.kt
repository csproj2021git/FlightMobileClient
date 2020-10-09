package com.example.flightmobileapp.main_screen

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightmobileapp.R
import com.example.flightmobileapp.Repository
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.Math.*
import kotlin.math.hypot
import kotlin.math.pow


class MainFragment : Fragment() {

    companion object {
        fun newInstance() =
            MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this,
            MainViewModelFactory(activity.application)
        )
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.image.observe(viewLifecycleOwner, Observer {
            imageView.setImageBitmap(it);
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            if(it == Repository.ApiStatus.ERROR){
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
            }
        })

        throttleBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                viewModel.throttleValue = (i / 100.0);
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        rudderBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                viewModel.rudderValue = (i / 100.0);
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        imageView2.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

                if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                    val innerRad = imageView3.width / 2.0
                    val targetX = motionEvent.rawX - innerRad
                    val targetY = motionEvent.rawY - 3.0 *innerRad
                    val centerX = 460.0
                    val centerY = 1050.0
                    val dx = centerX - targetX
                    val dy = centerY- targetY
                    val outerRad = view.width / 2.0
                    val maxval = outerRad-50.0
                    if(sqrt((dx).pow(2)+(dy).pow(2)) <= maxval){
                        imageView3.x = targetX.toFloat()
                        imageView3.y = targetY.toFloat()
                        viewModel.aileronValue = (-dx / maxval)
                        viewModel.elevatorValue = (dy / maxval)
                    }

                }
                if(motionEvent.action == MotionEvent.ACTION_UP){
                    imageView3.x = 460.0.toFloat()
                    imageView3.y = 1050.0.toFloat()
                    viewModel.aileronValue = 0.0
                    viewModel.elevatorValue = 0.0
                }
                return true
            }
        })

        var timer = object : CountDownTimer(500000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.connect()
            }

            override fun onFinish() {
                try {
                } catch (e: Exception) {
                    Log.e("Error", "Error: $e")
                }
            }
        }.start()

 //       viewModel.connect()
    }

}
