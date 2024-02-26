package com.example.Assignment.Activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.Assignment.Adapters.CategoryListAdapter
import com.example.Assignment.Adapters.FilmListAdapter
import com.example.Assignment.Adapters.SliderAdapters
import com.example.Assignment.Domain.GenresItem
import com.example.Assignment.Domain.ListFilm
import com.example.Assignment.Domain.SliderItems
import com.example.project155.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private var adapterBestMovies: RecyclerView.Adapter<*>? = null
    private var adapterUpComming: RecyclerView.Adapter<*>? = null
    private var adapterCategory: RecyclerView.Adapter<*>? = null
    private var recyclerViewBestMovies: RecyclerView? = null
    private var recyclerviewUpcomming: RecyclerView? = null
    private var recyclerviewCategory: RecyclerView? = null
    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private var mStringRequest2: StringRequest? = null
    private var mStringRequest3: StringRequest? = null
    private var loading1: ProgressBar? = null
    private var loading2: ProgressBar? = null
    private var loading3: ProgressBar? = null
    private var viewPager2: ViewPager2? = null
    private val slideHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        banners()
        sendRequestBestMovies()
        sendRequestUpComming()
        sendRequestCategory()
    }

    private fun sendRequestBestMovies() {
        mRequestQueue = Volley.newRequestQueue(this)
        loading1!!.visibility = View.VISIBLE
        mStringRequest = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/movies?page=1",
            { response: String? ->
                val gson = Gson()
                loading1!!.visibility = View.GONE
                val items = gson.fromJson(response, ListFilm::class.java)
                adapterBestMovies = FilmListAdapter(items)
                recyclerViewBestMovies!!.adapter = adapterBestMovies
            }) { error: VolleyError ->
            loading1!!.visibility = View.GONE
            Log.i("UiLover", "onErrorResponse: $error")
        }
        mRequestQueue!!.add(mStringRequest)
    }

    private fun sendRequestUpComming() {
        mRequestQueue = Volley.newRequestQueue(this)
        loading3!!.visibility = View.VISIBLE
        mStringRequest3 = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/movies?page=2",
            { response: String? ->
                val gson = Gson()
                loading3!!.visibility = View.GONE
                val items = gson.fromJson(response, ListFilm::class.java)
                adapterUpComming = FilmListAdapter(items)
                recyclerviewUpcomming!!.adapter = adapterUpComming
            }) { error: VolleyError ->
            loading3!!.visibility = View.GONE
            Log.i("UiLover", "onErrorResponse: $error")
        }
        mRequestQueue!!.add(mStringRequest3)
    }

    private fun sendRequestCategory() {
        mRequestQueue = Volley.newRequestQueue(this)
        loading2!!.visibility = View.VISIBLE
        mStringRequest2 = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/genres",
            { response: String? ->
                val gson = Gson()
                loading2!!.visibility = View.GONE
                val catList = gson.fromJson<ArrayList<GenresItem>>(
                    response,
                    object : TypeToken<ArrayList<GenresItem?>?>() {}.type
                )
                adapterCategory = CategoryListAdapter(catList)
                recyclerviewCategory!!.adapter = adapterCategory
            }) { error: VolleyError ->
            loading2!!.visibility = View.GONE
            Log.i("UiLover", "onErrorResponse: $error")
        }
        mRequestQueue!!.add(mStringRequest2)
    }

    private fun banners() {
        val sliderItems: MutableList<SliderItems> = ArrayList()
        sliderItems.add(SliderItems(R.drawable.wide))
        sliderItems.add(SliderItems(R.drawable.wide1))
        sliderItems.add(SliderItems(R.drawable.wide3))
        viewPager2!!.adapter = SliderAdapters(sliderItems, viewPager2!!)
        viewPager2!!.clipToPadding = false
        viewPager2!!.clipChildren = false
        viewPager2!!.offscreenPageLimit = 3
        viewPager2!!.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager2!!.setPageTransformer(compositePageTransformer)
        viewPager2!!.currentItem = 1
        viewPager2!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideHandler.removeCallbacks(sliderRunnable)
            }
        })
    }

    private val sliderRunnable =
        Runnable { viewPager2!!.currentItem = viewPager2!!.currentItem + 1 }

    override fun onPause() {
        super.onPause()
        slideHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        slideHandler.postDelayed(sliderRunnable, 2000)
    }

    private fun initView() {
        viewPager2 = findViewById(R.id.viewpagerSlider)
        recyclerViewBestMovies = findViewById(R.id.view1)
        recyclerviewUpcomming = findViewById(R.id.view3)
        recyclerviewCategory = findViewById(R.id.view2)
        loading1 = findViewById(R.id.progressBar1)
        loading2 = findViewById(R.id.progressBar2)
        loading3 = findViewById(R.id.progressBar3)

        recyclerViewBestMovies?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerviewUpcomming?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerviewCategory?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}