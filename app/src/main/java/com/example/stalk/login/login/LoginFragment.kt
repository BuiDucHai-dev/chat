package com.example.stalk.login.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stalk.R
import com.example.stalk.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginFrgViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginFrgViewModelFactory())
            .get(LoginFrgViewModel::class.java)
        //(activity as LoginActivity).findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var phoneNum: String? = null
        binding.btnNext.setOnClickListener {
            binding.ccp.registerCarrierNumberEditText(binding.etPhone)
            phoneNum = binding.ccp.fullNumberWithPlus.replace(" ", "")
            loginViewModel.login(requireActivity(), phoneNum!!)
            binding.progressBar.visibility = View.VISIBLE
        }

        loginViewModel.loginVerificationId.observe(requireActivity()) {
            if (it != null) {
                if (it == "getOtpFail") {
                    showLoginMsg(requireActivity().getString(R.string.re_check_phone_number))
                } else {
                    val bundle = bundleOf(
                        "verificationId" to it,
                        "phoneNum" to phoneNum
                    )
                    findNavController().navigate(
                        R.id.action_LoginFragment_to_VerificationFragment,
                        bundle
                    )
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showLoginMsg(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}