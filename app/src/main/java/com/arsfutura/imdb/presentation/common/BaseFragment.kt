package com.arsfutura.imdb.presentation.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arsfutura.imdb.data.mapper.ErrorMapper
import com.arsfutura.imdb.presentation.utils.showErrorSnackbar

abstract class BaseFragment : Fragment(), BaseMvp.View {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getPresenter()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
    }

    /**
     * Overridable init presenter
     */
    open fun initPresenter() {
        getPresenter().initialize()
        getPresenter().subscribe()
    }

    abstract fun getPresenter(): BaseMvp.Presenter

    override fun showError(throwable: Throwable) {
        Log.e("Error", throwable.message.toString())
        kotlin.runCatching {
            showErrorSnackbar(requireView(), requireContext(), ErrorMapper().parse(throwable))
        }
    }

    override fun onDestroy() {
        getPresenter().destroy()
        super.onDestroy()
    }
}