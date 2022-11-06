package com.example.stalk.login.account_initial

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.example.stalk.R
import com.example.stalk.common.Comm
import com.example.stalk.databinding.FragmentAccountInitialBinding
import com.example.stalk.home.HomeActivity
import com.example.stalk.home.ui.setting.SettingFragment


class AccountInitialFragment : Fragment() {
    private var _binding: FragmentAccountInitialBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AccountInitialViewModel

    private var hasImage: Boolean = false
    private var uriImage: Uri? = null
    private lateinit var phoneNum: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNum = arguments?.getString("phoneNum")!!
        viewModel = ViewModelProvider(this, AccountInitialViewModelFactory())
            .get(AccountInitialViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountInitialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAvatar.setOnClickListener {
            if (checkPermission()){
                setAvatar(binding)
            } else {
                requestPermission()
            }
        }

        binding.etFirstNameRequired.afterTextChanged {
            viewModel.getStatusForm(
                binding.etFirstNameRequired.text.toString(),
                binding.etLastNameRequired.text.toString(),
                hasImage
            )
        }

        binding.etLastNameRequired.apply {
            afterTextChanged {
                viewModel.getStatusForm(
                    binding.etFirstNameRequired.text.toString(),
                    binding.etLastNameRequired.text.toString(),
                    hasImage)
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> (
                            createUserData(binding.etFirstNameRequired.text.toString(),
                                binding.etLastNameRequired.text.toString(), uriImage!!, phoneNum))
                }
                false
            }

            binding.btnCreateDetailAcc.setOnClickListener{
                createUserData(binding.etFirstNameRequired.text.toString(),
                    binding.etLastNameRequired.text.toString(), uriImage!!, phoneNum)
                binding.prbLoad.visibility = View.VISIBLE
                Comm().hideKeyBoard(requireActivity())
            }
        }

        viewModel.formState.observe(requireActivity()) {
            Log.d(
                "hai.bd",
                it.avatarError.toString() + it.firstnameError.toString() + it.lastnameError + it.isDataValid
            )
            binding.btnCreateDetailAcc.isEnabled = it.isDataValid
            if (it.firstnameError != null) {
                binding.etFirstNameRequired.error = getString(it.firstnameError)
            }
            if (it.lastnameError != null) {
                binding.etLastNameRequired.error = getString(it.lastnameError)
            }
        }

        viewModel.initUserResult.observe(requireActivity()) {
            if (it.equals("success")) {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()
            } else if (it.equals("fail")) {
                Toast.makeText(requireContext(), R.string.fail_to_create_user, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setAvatar(binding: FragmentAccountInitialBinding) {
        val kwikPicker = KwikPicker.Builder(requireContext(),
            imageProvider = { imageView, uri ->
                Glide.with(requireContext())
                    .load(uri)
                    .into(imageView)
            },
            onImageSelectedListener = {
                Glide.with(requireContext())
                    .load(it)
                    .circleCrop()
                    .into(binding.ivAvatar)
                hasImage = true
                uriImage = it
                viewModel.getStatusForm(
                    binding.etFirstNameRequired.text.toString(),
                    binding.etLastNameRequired.text.toString(),
                    hasImage
                )
            },
            peekHeight = 1200)
            .create(requireContext())
        kwikPicker.show(parentFragmentManager)
    }

    private fun checkPermission(): Boolean{
        return Comm().checkPermission(requireActivity())
    }
    private fun requestPermission() {
        val permission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permission, SettingFragment.REQUEST_PERMISSIONS_CODE_WRITE_STORAGE)
    }

    private fun createUserData(etFirstName: String, etLastName: String, uriImage: Uri, phoneNum: String) {
        viewModel.createUserData(etFirstName, etLastName, uriImage, phoneNum)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SettingFragment.REQUEST_PERMISSIONS_CODE_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                setAvatar(binding)
            }
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}