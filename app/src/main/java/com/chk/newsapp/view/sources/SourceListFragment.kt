package com.chk.newsapp.view.sources

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chk.newsapp.R
import com.chk.newsapp.data.api.utils.Resource
import com.chk.newsapp.data.api.utils.Status
import com.chk.newsapp.data.datamodel.SourceEntity
import com.chk.newsapp.data.datamodel.Topic
import com.chk.newsapp.databinding.FragmentSourcesBinding
import com.chk.newsapp.view.BaseFragment
import com.chk.newsapp.view.customcomponents.AutoClearedValue
import com.chk.newsapp.view.customcomponents.FragmentDataBindingComponent
import com.chk.newsapp.view.headlines.HeadlinesViewModel

import java.util.Collections

import javax.inject.Inject

import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Created by chira on 19-08-2017.
 */

class SourceListFragment : BaseFragment() {

    @Inject
    internal var mViewModelFactory: ViewModelProvider.Factory? = null
    private val mDataBindingComponent = FragmentDataBindingComponent(this)
    private var mBinding: AutoClearedValue<FragmentSourcesBinding>? = null
    private var mAdapter: AutoClearedValue<SourcesListAdapter>? = null
    private var mSourcesViewModel: SourcesViewModel? = null
    private var mHeadlinesViewModel: HeadlinesViewModel? = null

    override fun create(savedInstanceState: Bundle) {}

    override fun createView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View {
        val fragmentTopicsBinding = DataBindingUtil.inflate<FragmentSourcesBinding>(inflater, R.layout.fragment_sources, container, false, mDataBindingComponent)
        mBinding = AutoClearedValue(this, fragmentTopicsBinding)
        return fragmentTopicsBinding.root
    }

    override fun activityCreated(savedInstanceState: Bundle) {
        mSourcesViewModel = ViewModelProviders.of(this, mViewModelFactory!!).get(SourcesViewModel::class.java)
        mHeadlinesViewModel = ViewModelProviders.of(this, mViewModelFactory!!).get(HeadlinesViewModel::class.java)
        if (arguments != null) {
            val topic = arguments.getParcelable<Topic>(TOPIC)
            mSourcesViewModel!!.setCategory(topic!!.category)
        } else {
            mSourcesViewModel!!.setCategory(null)
        }

        val sourcesListAdapter = SourcesListAdapter(mDataBindingComponent
        ) { source ->
            mSourcesViewModel!!.setSubscriprionDetails(source).subscribe(object : io.reactivex.Observer<SourceEntity> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(sourceEntity: SourceEntity) {
                    Timber.i("Source subscribed : " + sourceEntity.name)
                }

                override fun onError(e: Throwable) {
                    Timber.i("Error subscribing to source: " + source.name)

                }

                override fun onComplete() {

                }
            })
        }
        this.mAdapter = AutoClearedValue(this, sourcesListAdapter)
        this.mBinding!!.get().sourcesList.adapter = sourcesListAdapter
        this.mBinding!!.get().sourcesList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        mSourcesViewModel!!.sources.observe(this, Observer { listResource ->
            mBinding!!.get().resource = listResource
            mBinding!!.get().executePendingBindings()
            if (listResource != null && listResource.data != null) {
                mAdapter!!.get().replace(listResource.data)
            } else {
                mAdapter!!.get().replace(emptyList())
            }
        })
    }

    companion object {

        private val TOPIC = "topic"

        fun create(topic: Topic?): Fragment {
            if (topic == null) {
                throw NullPointerException("Topic cannot be null")
            }
            val sourceListFragment = SourceListFragment()
            val bundle = Bundle()
            bundle.putParcelable(TOPIC, topic)
            sourceListFragment.arguments = bundle
            return sourceListFragment
        }
    }
}
