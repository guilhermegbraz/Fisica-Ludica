package br.edu.ufabc.fisicaludica.view.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.fisicaludica.databinding.FragmentAuthBinding
import br.edu.ufabc.fisicaludica.viewmodel.FragmentWindow
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        viewModel.currentFragmentWindow.value = FragmentWindow.AuthenticationFragment
        launcher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) {result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                launchTaskList()
            }
            else {
                Snackbar.make(binding.root, "Failed to sing-in. Please try again", Snackbar.LENGTH_LONG).show()
            }
        }
        authenticate()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authFragmentStartImageButton.setOnClickListener {
            launchAuthUi()
        }
    }

    private fun authenticate() {
        if(FirebaseAuth.getInstance().currentUser != null) {
            launchTaskList()
        }
    }

    private fun launchTaskList() {
        viewModel.enableNextGameLevel(0).observe(viewLifecycleOwner) {status->
            when(status) {
                is MainViewModel.Status.Failure -> {
                }
                MainViewModel.Status.Loading -> {

                }
                is MainViewModel.Status.Success -> {
                    AuthFragmentDirections.goToHome().let{
                        findNavController().navigate(it)
                    }
                }
            }
        }

    }

    private fun launchAuthUi() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        launcher.launch(intent)
    }

}