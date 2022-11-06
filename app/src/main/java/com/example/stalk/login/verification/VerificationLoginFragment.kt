package com.example.stalk.login.verification

import android.content.Intent
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
import com.example.stalk.common.Comm
import com.example.stalk.databinding.FragmentVerificationLoginBinding
import com.example.stalk.home.HomeActivity

class VerificationLoginFragment : Fragment() {

    companion object{
        const val VERIFICATION_RESULT_EXISTED = "existed"
        const val VERIFICATION_RESULT_NON_EXISTED = "non_existed"
        const val VERIFICATION_RESULT_FAIL = "fail2verify"
    }

    private var _binding: FragmentVerificationLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var verificationViewModel: VerificationViewModel
    private lateinit var verificationId: String
    private lateinit var phoneNum: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificationViewModel = ViewModelProvider(this, VerificationViewModelFactory())
            .get(VerificationViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        verificationId = arguments?.getString("verificationId")!!
        phoneNum = arguments?.getString("phoneNum")!!
        _binding = FragmentVerificationLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPinEntry.focus()
        binding.etPinEntry.setOnPinEnteredListener {
            verificationViewModel.signInWithPhoneAuthCredential(verificationId ,it.toString())
            binding.progressBar.visibility = View.VISIBLE
            Comm().hideKeyBoard(requireActivity())
        }

        verificationViewModel.verificationResult.observe(requireActivity()) {
            if (it != null) {
                when (it) {
                    VERIFICATION_RESULT_EXISTED -> {
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                    }
                    VERIFICATION_RESULT_NON_EXISTED -> {
                        val bundle = bundleOf("phoneNum" to phoneNum)
                        findNavController().navigate(
                            R.id.action_VerificationFragment_to_AccountInitialFragment,
                            bundle
                        )
                    }
                    VERIFICATION_RESULT_FAIL -> {
                        binding.etPinEntry.clearComposingText()
                        binding.etPinEntry.focus()
                        Toast.makeText(requireContext(), "Verify Fail", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}