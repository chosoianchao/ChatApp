package com.rikkei.tranning.basekotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {
    private var _viewBinding: B? = null

    protected val viewBinding: B
        get() = _viewBinding ?: throw Exception(getString(R.string.error_binding))

    @get:LayoutRes
    protected abstract val layoutResource: Int
    protected abstract val viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil
        .inflate<B>(inflater, layoutResource, container, false)
        .apply { _viewBinding = this }
        .root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }
    protected abstract fun initData()
    protected abstract fun initViews()

     fun <T : Any> Fragment.getBackStackData(key: String, result: (T) -> (Unit)) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
            ?.observe(viewLifecycleOwner) {
                result(it)
            }
    }

     fun <T : Any> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = true) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }
}