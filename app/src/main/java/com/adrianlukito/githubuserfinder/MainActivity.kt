package com.adrianlukito.githubuserfinder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adrianlukito.githubuserfinder.adapter.UserAdapter
import com.adrianlukito.githubuserfinder.api.IAPI
import com.adrianlukito.githubuserfinder.api.RestClient
import com.adrianlukito.githubuserfinder.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    internal lateinit var api: IAPI
    val PER_PAGE = 30
    var TOTAL_PAGES = 1
    var currentPage = 1
    var isLoading = false
    var isLastPage = false
    var userAdapter: UserAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RestClient().getClient()
        api = retrofit.create(IAPI::class.java)

        tvSearchNotFound.gone()
        progressBarCenter.gone()
        progressBarBottom.gone()
        btnSearch.setOnClickListener {
            searchUsers()
            txtName.hideKeyboard()
        }
        userAdapter = UserAdapter(this)
        linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = userAdapter
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager!!) {
            override fun totalPageCount(): Int{
                return TOTAL_PAGES
            }

            override fun isLoading(): Boolean{
                return isLoading
            }

            override fun isLastPage(): Boolean{
                return isLastPage
            }

            override fun loadMoreItems() {
                if(layoutManager.itemCount >= PER_PAGE) {
                    isLoading = true
                    currentPage += 1

                    progressBarBottom.visible()
                    Handler().postDelayed(Runnable { loadNextPage() }, 1000)
                }
            }
        })
    }

    fun searchUsers() {
        currentPage = 1
        isLastPage = false
        userAdapter?.clear()
        progressBarCenter.visible()
        api.getUserList(txtName.text.toString(), page = currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user ->
                progressBarCenter.gone()
                tvSearchNotFound.gone()
                val results = user.items
                if(results?.size == 0) {
                    recyclerView.gone()
                    tvSearchNotFound.visible()
                } else {
                    recyclerView.visible()
                    tvSearchNotFound.gone()

                    TOTAL_PAGES = Math.ceil(user.totalCount.toDouble() / PER_PAGE).toInt()
                    userAdapter?.addAll(results!!)

                    if (currentPage > TOTAL_PAGES)
                        isLastPage = true
                }
            }, { error->
                displayError()
                Log.e("Error", error.message)
            })
    }

    fun loadNextPage() {
        api.getUserList(txtName.text.toString(), page = currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                isLoading = false
                progressBarBottom.gone()
                val results = response.items
                userAdapter?.addAll(results!!)

                if (currentPage == TOTAL_PAGES)
                    isLastPage = true

            }, { error->
                displayError()
                Log.e("Error", error.message)
            })
    }

    fun displayError() {
        tvSearchNotFound.visible()
        progressBarCenter.gone()
        recyclerView.gone()
    }

    fun View.hideKeyboard(): Boolean {
        try {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } catch (ignored: RuntimeException) { }
        return false
    }

    fun View.visible() : View {
        visibility = View.VISIBLE
        return this
    }

    fun View.gone() : View {
        visibility = View.GONE
        return this
    }
}
