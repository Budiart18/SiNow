package com.group2.sinow.presentation.profile

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityProfileBinding
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import com.shashank.sony.fancytoastlib.FancyToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.InputStream


class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel by viewModel()

    private var selectedImageInputStream: InputStream? = null

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val stream = contentResolver.openInputStream(it)
                selectedImageInputStream = stream
                binding.profileImage.load(it) {
                    crossfade(true)
                    placeholder(R.drawable.profile_image)
                    error(R.drawable.profile_image)
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListener()
        getData()
        observeUserData()
        observeUpdateUserData()
    }

    private fun observeUpdateUserData() {
        viewModel.changeProfileResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnSaveProfile.isVisible = true
                    selectedImageInputStream = null
                    viewModel.toggleEditMode()
                    FancyToast.makeText(
                        this,
                        "Berhasil mengupdate data user",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnSaveProfile.isVisible = true
                    if (it.exception is ApiException) {
                        FancyToast.makeText(
                            this,
                            it.exception.getParsedError()?.message.orEmpty(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnSaveProfile.isVisible = false
                }
            )
        }
    }

    private fun setClickListener() {
        binding.ivEdit.setOnClickListener {
            viewModel.toggleEditMode()
        }
        binding.btnSaveProfile.setOnClickListener {
            updateUserData()
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivEditPhoto.setOnClickListener {
            pickImagesLauncher.launch("image/*")
        }
    }

    private fun createImagePart(inputStream: InputStream?): MultipartBody.Part? {
        inputStream?.let {
            val requestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), inputStream.readBytes())
            return MultipartBody.Part.createFormData("image", "filename.jpg", requestBody)
        }
        return null
    }

    private fun updateUserData() {
        val name = binding.etProfileName.text.toString().trim()
        val phoneNumber = binding.etProfilePhone.text.toString().trim()
        val country = binding.etProfileCountry.text.toString().trim()
        val city = binding.etProfileCity.text.toString().trim()
        viewModel.updateUserData(
            name.toRequestBody("text/plain".toMediaTypeOrNull()),
            phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
            country.toRequestBody("text/plain".toMediaTypeOrNull()),
            city.toRequestBody("text/plain".toMediaTypeOrNull()),
            if (selectedImageInputStream != null) createImagePart(selectedImageInputStream) else null
        )
    }

    private fun setupForm() {
        viewModel.isEditModeEnabled.observe(this) { isEditModeEnabled ->
            if (isEditModeEnabled) {
                binding.ivEdit.load(R.drawable.ic_edit_mode_disable)
                binding.etProfileName.isEnabled = true
                binding.etProfileEmail.isEnabled = false
                binding.etProfilePhone.isEnabled = true
                binding.etProfileCountry.isEnabled = true
                binding.etProfileCity.isEnabled = true
                binding.btnSaveProfile.isEnabled = true
                binding.ivEditPhoto.isVisible = true
                binding.ivEditPhoto.isEnabled = true
            } else {
                binding.ivEdit.load(R.drawable.ic_edit_mode_enabled)
                binding.etProfileName.isEnabled = false
                binding.etProfileEmail.isEnabled = false
                binding.etProfilePhone.isEnabled = false
                binding.etProfileCountry.isEnabled = false
                binding.etProfileCity.isEnabled = false
                binding.btnSaveProfile.isEnabled = false
                binding.ivEditPhoto.isVisible = false
            }
        }
    }

    private fun observeUserData() {
        viewModel.userData.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    it.payload?.let { profileData ->
                        binding.etProfileName.setText(profileData.name)
                        binding.etProfileEmail.setText(profileData.auth?.email)
                        binding.etProfilePhone.setText(profileData.auth?.phoneNumber)
                        binding.etProfileCountry.setText(profileData.country)
                        binding.etProfileCity.setText(profileData.city)
                        binding.profileImage.load(profileData.photoProfileUrl) {
                            crossfade(true)
                            placeholder(R.drawable.profile_image)
                            error(R.drawable.profile_image)
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            )
        }
    }

    private fun getData() {
        viewModel.getUserData()
    }
}