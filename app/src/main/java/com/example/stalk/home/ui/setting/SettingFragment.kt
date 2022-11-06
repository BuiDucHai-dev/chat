package com.example.stalk.home.ui.setting

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stalk.R
import com.example.stalk.common.Comm
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.User
import com.example.stalk.databinding.FragmentSettingBinding
import com.example.stalk.login.LoginActivity

@Suppress("DEPRECATION")
class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null

    private val binding get() = _binding!!
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentUser()

        viewModel.user.observe(requireActivity()){
            bindUser(it)
            user = it
        }

        binding.tvUpdateAvatar.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_PERMISSIONS_CODE_WRITE_STORAGE)
            } else {
                setAvatar()
            }
        }

        binding.lnUser.setOnClickListener {
            openDialog(SELECT_NAME, user?.fullName)
        }
        binding.lnBio.setOnClickListener {
            openDialog(SELECT_BIO, user?.bio)
        }

        binding.logout.setOnClickListener {
            showDialogLogout()
        }
    }

    private fun showDialogLogout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.are_you_sure))
        builder.setMessage(getString(R.string.confirm_logout))
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            MyFirebase().auth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
        builder.setNegativeButton("No"){ d: DialogInterface, _: Int ->
            d.cancel()}
        builder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindUser(it: User) {
        binding.tvPhoneNumber.text = it.phoneNum
        binding.tvUserName.text = it.fullName
        binding.tvName.text = it.fullName
        if (it.bio.isNullOrBlank()){
            binding.tvBio.text = getString(R.string.add_a_few_words_about_yourself)
        } else {
            binding.tvBio.text = it.bio
        }

        Glide.with(requireActivity())
            .load(it.avatar)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.imAvatar)
    }

    private fun getCurrentUser() {
        viewModel.getCurrentUser()
    }

    @SuppressLint("CutPasteId")
    private fun openDialog(options: Int, content: String?){
        var hint = ""
        var desc = ""
        when (options){
            SELECT_NAME -> {
                hint = getString(R.string.your_username)
                desc = getString(R.string.desc_change_user_name)
            }
            SELECT_BIO -> {
                hint = getString(R.string.bio)
                desc = getString(R.string.add_a_few_words_about_yourself)
            }
        }

        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_info, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialog)

        val mAlertDialog = builder.show()
        dialog.findViewById<EditText>(R.id.et_field).hint = hint
        if (content != ""){
            dialog.findViewById<EditText>(R.id.et_field).text =
                Editable.Factory.getInstance().newEditable(content)
        }
        dialog.findViewById<TextView>(R.id.tv_desc).text = desc

        dialog.findViewById<Button>(R.id.btn_ok).apply {
            setOnFocusChangeListener{_, hasFocus ->
                if (!hasFocus){
                    Comm().hideKeyBoard(requireActivity())
                }}
            setOnClickListener {
                updateProfile(options, dialog.findViewById<EditText>(R.id.et_field).text.toString())
                mAlertDialog.dismiss()
            }
        }
    }

    private fun updateProfile(options: Int, content: String) {
        if (options == SELECT_NAME && content.isBlank()){
            Toast.makeText(requireContext(), getString(R.string.username_is_required)
                , Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.updateProfile(options, content)
    }

    private fun setAvatar() {
        val kwikPicker = KwikPicker.Builder(requireContext(),
            imageProvider = { imageView, uri ->
                Glide.with(requireContext())
                    .load(uri)
                    .into(imageView)
            },
            onImageSelectedListener = {
                showDialogImage(it)
            },
            peekHeight = 1200)
            .create(requireContext())
        kwikPicker.show(parentFragmentManager)
    }

    private fun showDialogImage(uri: Uri) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.are_you_sure))
        builder.setMessage(getString(R.string.confirm_change_photo))
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int -> viewModel.updateAvatar(uri) }

        builder.setNegativeButton("No"){ d: DialogInterface, _: Int -> d.cancel()}

        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setAvatar()
            }
        }
    }

    companion object{
        const val SELECT_NAME = 0
        const val SELECT_BIO = 1
        const val REQUEST_PERMISSIONS_CODE_WRITE_STORAGE = 100
    }
}