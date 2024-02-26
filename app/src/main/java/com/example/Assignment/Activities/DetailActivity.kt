package com.example.Assignment.Activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.Assignment.Adapters.ActorsListAdapter
import com.example.Assignment.Adapters.CategoryEachFilmListAdapter
import com.example.Assignment.Domain.FilmItem
import com.example.project155.R
import com.google.gson.Gson

lateinit var  adapterActorList: RecyclerView.Adapter<*>
class DetailActivity : AppCompatActivity() {
    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private var progressBar: ProgressBar? = null
    private var titleTxt: TextView? = null
    private var movieRateTxt: TextView? = null
    private var movieTimeTxt: TextView? = null
    private var movieSummaryInfo: TextView? = null
    private var movieActorsInfo: TextView? = null
    private var idFilm = 0
    private var pic2: ImageView? = null
    private var backImg: ImageView? = null
    private var adapterCategory: RecyclerView.Adapter<*>? = null
    private var recyclerViewActors: RecyclerView? = null
    private var recyclerViewCategory: RecyclerView? = null
    private var scrollView: NestedScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        idFilm = intent.getIntExtra("id", 0)
        initView()
        sendRequest()
    }

    private fun sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this)
        progressBar!!.visibility = View.VISIBLE
        scrollView!!.visibility = View.GONE
        mStringRequest = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/movies/$idFilm",
            { response: String? ->
                val gson = Gson()
                progressBar!!.visibility = View.GONE
                scrollView!!.visibility = View.VISIBLE
                val item = gson.fromJson(response, FilmItem::class.java)
                Glide.with(this@DetailActivity)
                    .load(item.poster)
                    .into(pic2!!)
                titleTxt!!.text = item.title
                movieRateTxt!!.text = item.imdbRating
                movieTimeTxt!!.text = item.runtime
                movieSummaryInfo!!.text = item.plot
                movieActorsInfo!!.text = item.actors
                if (item.images != null) {
                    adapterActorList = ActorsListAdapter(item.images!!)
                    recyclerViewActors!!.adapter = adapterActorList as ActorsListAdapter
                }
                if (item.genres != null) {
                    adapterCategory = CategoryEachFilmListAdapter(item.genres!!)
                    recyclerViewCategory!!.adapter = adapterCategory
                }
            }) { error: VolleyError? -> progressBar!!.visibility = View.GONE }
        mRequestQueue!!.add(mStringRequest)
    }

    private fun initView() {
        titleTxt = findViewById(R.id.movieNameTxt)
        progressBar = findViewById(R.id.progressBarDetail)
        scrollView = findViewById(R.id.scrollView2)
        pic2 = findViewById(R.id.picDetail)
        movieRateTxt = findViewById(R.id.movieStar)
        movieTimeTxt = findViewById(R.id.movieTime)
        movieSummaryInfo = findViewById(R.id.movieSummery)
        movieActorsInfo = findViewById(R.id.movieActorInfo)
        backImg = findViewById(R.id.backImg)
        recyclerViewCategory = findViewById(R.id.genreView)
        recyclerViewActors = findViewById(R.id.imagesRecycler)

        recyclerViewActors!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategory!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        backImg!!.setOnClickListener { finish() }
    }
}
