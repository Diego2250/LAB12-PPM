package com.example.lab12

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.lab12.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val sessionViewModel : SessionViewModel by activityViewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launch{
            sessionViewModel.validAuthToken.collectLatest { valueToken ->
                if (!valueToken){
                    requireView().findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                }
            }
        }
        lifecycleScope.launch{
            homeViewModel.status.collectLatest {
                handleCheckInStatus(it)
            }
        }

    }

    private fun handleCheckInStatus(status: HomeViewModel.Status) {
        when (status) {
            is HomeViewModel.Status.default -> {
                lifecycleScope.launch {
                    homeViewModel.progressBar.collectLatest { progressValue ->
                        if (!progressValue) {
                            binding.progressbar.visibility = View.GONE
                            binding.imageView.visibility = View.VISIBLE
                            binding.textStatus.visibility = View.VISIBLE
                            binding.apply {
                                btDefault.isEnabled = true
                                btSuccess.isEnabled = true
                                btEmpty.isEnabled = true
                                btFailure.isEnabled = true
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_default,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        } else {
                            binding.progressbar.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            binding.textStatus.visibility = View.GONE
                            binding.apply {
                                btDefault.isEnabled = true
                                btSuccess.isEnabled = false
                                btEmpty.isEnabled = false
                                btFailure.isEnabled = false
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_default,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        }
                    }
                }
            }
            is HomeViewModel.Status.success -> {
                lifecycleScope.launch {
                    homeViewModel.progressBar.collectLatest { progressValue ->
                        if (!progressValue) {
                            binding.progressbar.visibility = View.GONE
                            binding.imageView.visibility = View.VISIBLE
                            binding.textStatus.visibility = View.VISIBLE
                            binding.apply {
                                btDefault.isEnabled = true
                                btSuccess.isEnabled = true
                                btEmpty.isEnabled = true
                                btFailure.isEnabled = true
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_succes,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        } else {
                            binding.progressbar.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            binding.textStatus.visibility = View.GONE
                            binding.apply {
                                btDefault.isEnabled = false
                                btSuccess.isEnabled = true
                                btEmpty.isEnabled = false
                                btFailure.isEnabled = false
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_succes,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        }
                    }
                }

            }
            is HomeViewModel.Status.failure -> {
                lifecycleScope.launch {
                    homeViewModel.progressBar.collectLatest { progressValue ->
                        if (!progressValue) {
                            binding.progressbar.visibility = View.GONE
                            binding.imageView.visibility = View.VISIBLE
                            binding.textStatus.visibility = View.VISIBLE
                            binding.apply {
                                btDefault.isEnabled = true
                                btSuccess.isEnabled = true
                                btEmpty.isEnabled = true
                                btFailure.isEnabled = true
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_rerror,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        } else {
                            binding.progressbar.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            binding.textStatus.visibility = View.GONE
                            binding.apply {
                                btDefault.isEnabled = false
                                btSuccess.isEnabled = false
                                btEmpty.isEnabled = false
                                btFailure.isEnabled = true
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_rerror,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        }
                    }
                }

            }
            is HomeViewModel.Status.empty -> {
                lifecycleScope.launch {
                    homeViewModel.progressBar.collectLatest { progressValue ->
                        if (!progressValue) {
                            binding.progressbar.visibility = View.GONE
                            binding.imageView.visibility = View.VISIBLE
                            binding.textStatus.visibility = View.VISIBLE
                            binding.apply {
                                btDefault.isEnabled = true
                                btSuccess.isEnabled = true
                                btEmpty.isEnabled = true
                                btFailure.isEnabled = true
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_no_results,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        } else {
                            binding.progressbar.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            binding.textStatus.visibility = View.GONE
                            binding.apply {
                                btDefault.isEnabled = false
                                btSuccess.isEnabled = false
                                btEmpty.isEnabled = true
                                btFailure.isEnabled = false
                                imageView.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_no_results,
                                        null
                                    )
                                )
                                textStatus.text = status.message
                            }
                        }
                    }
                }

            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btMantenersession.setOnClickListener {
                sessionViewModel.stop()
            }
            btCerrarsesion.setOnClickListener {
                sessionViewModel.stop()
                requireView().findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
            btDefault.setOnClickListener {
                homeViewModel.Default()
            }
            btEmpty.setOnClickListener {
                homeViewModel.Empty()
            }
            btFailure.setOnClickListener {
                homeViewModel.Failure()
            }
            btSuccess.setOnClickListener {
                homeViewModel.Success()
            }
        }
    }
}