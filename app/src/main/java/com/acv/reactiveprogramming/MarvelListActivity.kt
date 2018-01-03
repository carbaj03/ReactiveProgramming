package com.acv.reactiveprogramming

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.acv.reactiveprogramming.interactor.PaginationExampleView
import com.acv.reactiveprogramming.interactor.bindPaginationExample
import com.acv.reactiveprogramming.interactor.requestMore
import com.acv.reactiveprogramming.interactor.subscribePaginationExample
import com.acv.reactiveprogramming.state.PaginationExampleState
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_marvel_list.*
import kotlinx.android.synthetic.main.content_marvel_list.*


class MarvelListActivity : BaseActivity(), PaginationExampleView {
    private val THRESHOLD = 5
    private val state: PaginationExampleState = PaginationExampleState()
    private var endOfPagePRelay: PublishRelay<None> = PublishRelay.create()

    init {
        this.subscribePaginationExample(state, { integer -> requestMore(integer) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marvel_list)
        setSupportActionBar(toolbar)
        initRv()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun initRv() {
        val layoutManager = LinearLayoutManager(this)
        rvMarvel.layoutManager = layoutManager
        rvMarvel.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    private var previousTotalItemCount = 0
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) = with(layoutManager) {
                        if (itemCount < previousTotalItemCount) {
                            previousTotalItemCount = itemCount
                        }
                        if (itemCount > previousTotalItemCount) {
                            previousTotalItemCount = itemCount
                        }
                        if (findFirstVisibleItemPosition() + childCount + THRESHOLD >= itemCount) {
                            endOfPagePRelay.accept(None)
                        }
                    }
                }
        )
        rvMarvel.adapter = ListExampleAdapter { a, b -> a == b }
    }

    override fun onAttachBinders() = bindPaginationExample(this, state)

    override fun updateElements(elements: List<String>) =
            getCastedAdapter().swap(elements)

    private fun getCastedAdapter(): ListExampleAdapter =
            rvMarvel.adapter as ListExampleAdapter

    override fun endOfPage(): Observable<None> =
            endOfPagePRelay.hide()

}



