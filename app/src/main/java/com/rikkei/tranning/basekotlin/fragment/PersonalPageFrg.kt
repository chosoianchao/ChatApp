package com.rikkei.tranning.basekotlin.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgPersonalPageBinding
import com.rikkei.tranning.basekotlin.viewmodel.PersonalPageVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalPageFrg : BaseFragment<FrgPersonalPageBinding>() {
    private var action: NavDirections? = null

    override val layoutResource: Int
        get() = R.layout.frg_personal_page

    override val viewModel: PersonalPageVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@PersonalPageFrg.viewModel
        }
    }

    override fun initViews() {
        viewModel.getInformation(::information)

        viewBinding.tvLogout.setOnClickListener {
            viewModel.logOutAccount()
            action = PersonalPageFrgDirections.actionPersonalPageFrgToLoginFrg()
            findNavController().navigate(action!!)
        }
        viewBinding.ivPencil.setOnClickListener {
            action = PersonalPageFrgDirections.actionPersonalPageFrgToModifyInformationFrg()
            findNavController().navigate(action!!)
        }
    }

    private fun information(email: Any?, name: Any?) {
        if (email == null || name == null) {
            viewBinding.tvEmail.text = ""
            viewBinding.tvName.text = ""
        } else {
            viewBinding.tvEmail.text = email.toString()
            viewBinding.tvName.text = name.toString()
        }
    }
}