package com.rikkei.tranning.basekotlin.fragment

import android.app.AlertDialog
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.CommonUtils
import com.rikkei.tranning.basekotlin.LocaleHelper
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgPersonalPageBinding
import com.rikkei.tranning.basekotlin.viewmodel.PersonalPageVM
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PersonalPageFrg : BaseFragment<FrgPersonalPageBinding>() {
    private var currentPosition: Int = 0
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initViews() {
        LocaleHelper().loadLocale(context)
        if (CommonUtils.instance?.getPref("text_language") != null) {
            viewBinding.tvLanguage2.text = CommonUtils.instance?.getPref("text_language")
        }
        val notify = CommonUtils.instance?.getPref("text_notify")
        if (notify != null) {
            viewBinding.tvNotify2.text = CommonUtils.instance?.getPref("text_notify")
            if (notify == context?.getString(R.string.enable)) {
                viewModel.enableNotification()
            } else {
                viewModel.disableNotification()
            }
        }
        viewModel.getInformation(::loadingInformation)

        viewBinding.tvLogout.setOnClickListener {
            viewModel.logOutAccount()
            action = PersonalPageFrgDirections.actionPersonalPageFrgToLoginFrg()
            findNavController().navigate(action!!)
        }
        viewBinding.ivPencil.setOnClickListener {
            action = PersonalPageFrgDirections.actionPersonalPageFrgToModifyInformationFrg()
            findNavController().navigate(action!!)
        }
        viewBinding.tvLanguage2.setOnClickListener {
            val listItems = arrayOf(getString(R.string.english), getString(R.string.text_vn))
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.text_choose_language))
            builder.setSingleChoiceItems(
                listItems,
                currentPosition
            ) { _, which -> currentPosition = which }
            builder.setPositiveButton(
                getString(R.string.text_apply)
            ) { dialog, _ ->
                if (currentPosition == 0) {
                    LocaleHelper().setLocale(context, "en")
                    viewBinding.tvLanguage2.text = getString(R.string.english)
                    CommonUtils.instance?.savePref(
                        "text_language",
                        viewBinding.tvLanguage2.text.toString()
                    )
                    recreate(requireActivity())
                } else {
                    LocaleHelper().setLocale(context, "vi")
                    viewBinding.tvLanguage2.text = getString(R.string.text_vn)
                    CommonUtils.instance?.savePref(
                        "text_language",
                        viewBinding.tvLanguage2.text.toString()
                    )
                    recreate(requireActivity())
                }
                dialog.dismiss()
            }
            builder.setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
        viewBinding.tvNotify2.setOnClickListener {
            val listItems = arrayOf(getString(R.string.enable), getString(R.string.disable))
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.text_notify))
            builder.setSingleChoiceItems(
                listItems,
                currentPosition
            ) { _, which -> currentPosition = which }
            builder.setPositiveButton(
                getString(R.string.text_apply)
            ) { dialog, _ ->
                if (currentPosition == 0) {
                    viewModel.enableNotification()
                    viewBinding.tvNotify2.text = getString(R.string.enable)
                } else {
                    viewModel.disableNotification()
                    viewBinding.tvNotify2.text = getString(R.string.disable)
                }
                CommonUtils.instance?.savePref(
                    "text_notify",
                    viewBinding.tvNotify2.text.toString()
                )
                dialog.dismiss()
            }
            builder.setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
    }

    private fun loadingInformation(email: Any?, name: Any?, photo: Any?) {
        viewBinding.loadingPage.visibility = View.GONE
        when (photo) {
            "" -> {
                viewBinding.ivAvatar.setImageResource(R.drawable.ic_user)
                viewBinding.ivViewPersonal.setImageResource(R.drawable.ic_personal_page)
                viewBinding.tvEmail.text = email.toString()
                viewBinding.tvName.text = name.toString()
            }
            else -> {
                viewBinding.tvEmail.text = email.toString()
                viewBinding.tvName.text = name.toString()
                context?.let { Glide.with(it).load(photo).into(viewBinding.ivViewPersonal) }
                context?.let { Glide.with(it).load(photo).into(viewBinding.ivAvatar) }
            }
        }
    }
}
