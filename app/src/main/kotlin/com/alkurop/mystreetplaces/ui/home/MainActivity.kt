package com.alkurop.mystreetplaces.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseMvpActivity<MainActivityView>() {
    companion object {

        val MODEL_KEY = "model"
    }

    @Inject lateinit var presenter: MainActivityPresenter

    var backStackListener = FragmentManager.OnBackStackChangedListener {
        val fragmentList = supportFragmentManager.fragments
        fragmentList?.filter { it != null }?.forEach {
            presenter.onFragmentShowed(it.tag)
            return@forEach
        }
    }

    override fun getSubject(): Observable<MainActivityView> {
        return presenter.viewBus
    }

    override fun getNavigation(): Observable<NavigationAction> {
        return presenter.navBus
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_main)
        supportActionBar?.setHomeButtonEnabled(true)
        initDrawerComponents()
        initSearchView()
        supportFragmentManager.addOnBackStackChangedListener(backStackListener)
        presenter.currentModel = savedInstanceState?.getParcelable(MODEL_KEY)
        presenter.start()
        toolbarTitleTv.text = getString(R.string.app_name)
    }

    private fun initSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    }

    private fun initDrawerComponents() {
        /*navigationView.setNavigationItemSelectedListener {
            presenter.onDrawerAction(it.itemId)
            drawer_layout.closeDrawers()
            true
        }

        val mDrawerToggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                getToolbar(),
                R.string.app_name,
                R.string.app_name
        )
        drawer_layout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()*/
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val bundle = outState ?: Bundle()
        bundle.putParcelable(MODEL_KEY, presenter.currentModel)
        super.onSaveInstanceState(bundle)
    }

    override fun renderView(viewModel: MainActivityView) {
        with(viewModel) {
           // viewModel.toolbarTitleRes.takeIf { it != -1 }?.let { toolbarTitleTv.text = getString(viewModel.toolbarTitleRes) }
            searchView.visibility = if (viewModel.shouldShowSearch) View.VISIBLE else View.GONE
        }
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        supportFragmentManager.removeOnBackStackChangedListener(backStackListener)
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager.fragments
                ?.filter { it != null && it.isAdded }
                ?.forEach { fragment ->
                    fragment.onActivityResult(requestCode, resultCode, data)
                    fragment.childFragmentManager.fragments
                            ?.filter { it != null && it.isAdded }
                            ?.forEach { child ->
                                child.onActivityResult(requestCode, resultCode, data)
                            }
                }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            if (query != null) sendSearchResult(query, SearchTopic.MAP_QUERY)
            val id = intent.data?.lastPathSegment
            if (id != null) sendSearchResult(id, SearchTopic.MAP_ITEM_ID)

        }
    }

    private fun sendSearchResult(query: String, topic: SearchTopic) {
        supportFragmentManager.fragments
                ?.filter { it != null && it.isAdded }
                ?.filter { it is Searchable }
                ?.forEach { fragment ->
                    (fragment as Searchable).onSearch(topic, query)
                    fragment.childFragmentManager.fragments
                            ?.filter { it != null && it.isAdded }
                            ?.filter { it is Searchable }
                            ?.forEach { child ->
                                (child as Searchable).onSearch(topic, query)
                            }
                }
    }
}