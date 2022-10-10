package com.example.lab12

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.lab12.R
import com.example.lab12.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val sessionViewModel : SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCorrectDataUser()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launchWhenStarted {
            sessionViewModel.logger.collectLatest {
                handleCheckInStatus(it)
            }
        }
    }

    private fun handleCheckInStatus(status: SessionViewModel.logStatus) {
        when(status){
            is SessionViewModel.logStatus.failure -> {
                binding.apply {
                    progressUsers.visibility=View.GONE
                    buttonLogin.visibility = View.VISIBLE
                }
                Toast.makeText(getActivity(), "Credenciales Incorrectas", Toast.LENGTH_LONG).show()
            }
            is SessionViewModel.logStatus.checkData->{
                binding.apply {
                    progressUsers.visibility = View.VISIBLE
                buttonLogin.visibility = View.GONE
                }
            }
            is SessionViewModel.logStatus.notLogged->{
                binding.apply {
                    progressUsers.visibility=View.GONE
                    buttonLogin.visibility=View.VISIBLE
                }
            }
            is SessionViewModel.logStatus.logged->{
                sessionViewModel.regresiveCount()
                requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }
        }
    }

    private fun setListeners() {
        binding.buttonLogin.setOnClickListener {
            binding.apply {
                val correo = textInputEmail.text.toString()
                val password = textInputPassword.text.toString()
                sessionViewModel.setDataUser(correo, password)
            }
        }
    }

    private fun setCorrectDataUser() {
        binding.apply {
            val Data = Correo.text.toString()
            sessionViewModel.setCorrectDataUser(Data)
        }
    }

}