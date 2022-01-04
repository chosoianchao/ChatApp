package com.rikkei.tranning.basekotlin.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.rikkei.tranning.basekotlin.R
import com.rikkei.tranning.basekotlin.base.BaseFragment
import com.rikkei.tranning.basekotlin.databinding.FrgModifyInformationBinding
import com.rikkei.tranning.basekotlin.showToastLong
import com.rikkei.tranning.basekotlin.viewmodel.ModifyInformationVM
import dagger.hilt.android.AndroidEntryPoint
import java.io.*


@AndroidEntryPoint
class ModifyInformationFrg : BaseFragment<FrgModifyInformationBinding>() {
    private var getImageFromCamera: ActivityResultLauncher<Intent>? = null
    private var requestPermission: ActivityResultLauncher<String>? = null

    override val layoutResource: Int
        get() = R.layout.frg_modify_information
    override val viewModel: ModifyInformationVM by viewModels()

    override fun initData() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ModifyInformationFrg.viewModel
        }
    }

    override fun initViews() {
        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                context?.showToastLong(getString(R.string.permission_denied))
                findNavController().popBackStack()
            }
        }
        requestPermission?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        viewModel.getInformation(::getInformation)
        viewBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        getImageFromCamera =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK && it.data != null) {
                    val bundle: Bundle? = it.data!!.extras
                    val bitmap: Bitmap? = bundle?.get(DATA) as Bitmap?

                    val file = File(context?.cacheDir, CHILD)

                    file.delete()
                    file.createNewFile()
                    val fileOutputStream = file.outputStream()
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val bytearray = byteArrayOutputStream.toByteArray()
                    fileOutputStream.write(bytearray)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    byteArrayOutputStream.close()

                    val uri = file.toUri()
                    viewBinding.ivAvatar.setImageURI(uri)
                    viewBinding.ivAvatar.tag = uri
                }
            }

        viewBinding.ivSelectImage.setOnClickListener {
            selectImage()
        }
        viewBinding.tvSave.setOnClickListener {
            viewBinding.loadingInformation.visibility = View.VISIBLE
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    androidx.appcompat.R.anim.abc_popup_enter
                )
            )
            val name = viewBinding.edtFullName.text.toString()
            val phone = viewBinding.edtPhone.text.toString()
            val date = viewBinding.edtDate.text.toString()
            val desc = viewBinding.edtDesc.text.toString()
            when (viewBinding.ivAvatar.tag) {
                null -> viewModel.upload(name, phone, date, desc, ::loadingSuccess)
                else -> viewModel.updateProfile(
                    name,
                    phone,
                    date,
                    viewBinding.ivAvatar.tag as Uri,
                    desc,
                    ::loadingSuccess
                )
            }
        }
    }

    private fun loadingSuccess() {
        viewBinding.loadingInformation.visibility = View.GONE
        context?.showToastLong(getString(R.string.success))
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun selectImage() {
        val options = arrayOf<CharSequence>(TAKE_PHOTO, IMAGE_FROM_GALLERY, CANCEL)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(ADD_PHOTO)
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == TAKE_PHOTO -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    activity?.packageManager.let {
                        if (intent.resolveActivity(it!!) != null) {
                            getImageFromCamera?.launch(intent)
                        }
                    }
                }
                options[item] == IMAGE_FROM_GALLERY -> {
                    getImageFromGallery.launch(IMAGE)
                }
                options[item] == CANCEL -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private val getImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewBinding.ivAvatar.setImageURI(uri)
            viewBinding.ivAvatar.tag = uri
        }

    private fun getInformation(name: Any?, phone: Any?, date: Any?, photo: Any?, desc: Any?) {
        viewBinding.loadingInformation.visibility = View.GONE
        when (photo) {
            null, "" -> {
                viewBinding.edtFullName.setText(name.toString())
                viewBinding.edtPhone.setText(phone.toString())
                viewBinding.edtDate.setText(date.toString())
                viewBinding.edtDesc.setText(date.toString())
                viewBinding.ivAvatar.setImageResource(R.drawable.ic_user)
            }
            else -> {
                viewBinding.edtFullName.setText(name.toString())
                viewBinding.edtPhone.setText(phone.toString())
                viewBinding.edtDate.setText(date.toString())
                viewBinding.edtDesc.setText(desc.toString())
                context?.let { Glide.with(it).load(photo).into(viewBinding.ivAvatar) }
            }
        }
    }

    companion object {
        private const val IMAGE_FROM_GALLERY: String = "Choose from Gallery"
        private const val ADD_PHOTO: String = "Add photo"
        private const val CANCEL: String = "Cancel"
        private const val TAKE_PHOTO: String = "Take Photo"
        private const val IMAGE: String = "image/*"
        private const val DATA: String = "data"
        private const val CHILD: String = "CUSTOM NAME"
    }
}

