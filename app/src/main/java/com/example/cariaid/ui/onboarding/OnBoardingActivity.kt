package com.example.cariaid.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cariaid.R
import com.example.cariaid.data.dataStore.DataStoreImpl
import com.example.cariaid.databinding.ActivityOnBoardingBinding
import com.example.cariaid.ui.auth.AuthActivity
import com.example.cariaid.ui.onboarding.adapter.OnBoardingAdapter
import com.example.cariaid.ui.onboarding.uiState.OnboardingModel
import com.example.cariaid.utils.Constant.USER_ONBOARDING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStoreImpl: DataStoreImpl
    private val listAdapter  = OnBoardingAdapter()

    private lateinit var binding:ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpOnBoarding()
        setUpUI()
    }

    private fun setUpUI() {
        binding.close.setOnClickListener {
            finishAffinity()
        }

        binding.gettingStarted.setOnClickListener {
            lifecycleScope.launch {
                dataStoreImpl.setValue(USER_ONBOARDING,"on_boarding_complete")
            }
            startActivity(Intent(applicationContext,AuthActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun setUpOnBoarding() {
        binding.list.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL,false)
        binding.list.adapter = listAdapter
        val onBoardObject  = listOf(
            OnboardingModel(R.drawable.onbordgraphics1,"Discover Charities To Donate\n To in the UK"),
            OnboardingModel(R.drawable.onbordgraphics2,"Donate Items to These\n charities "),
            OnboardingModel(R.drawable.onbordgraphics3,"Donate to causes that\n matter to you")
        )
        listAdapter.submitList(onBoardObject)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.list)
        binding.indicator.attachToRecyclerView(binding.list,snapHelper)

        binding.list.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLastVisible()){
                    with(binding){
                        Next.visibility = GONE
                        gettingStarted.visibility = VISIBLE
                        Back.visibility= INVISIBLE
                    }

                }

                if (isFirstVisible()){
                   Log.d("###","first")
                }
            }
        })

    }

    fun isLastVisible(): Boolean {
        val layoutManager = binding.list.layoutManager as LinearLayoutManager
        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
        val numItems=listAdapter.itemCount
        return pos >= numItems - 1
    }

    fun isFirstVisible(): Boolean {
        val layoutManager = binding.list.layoutManager as LinearLayoutManager
        val pos = layoutManager.findFirstCompletelyVisibleItemPosition()
        return pos == 0
    }


}