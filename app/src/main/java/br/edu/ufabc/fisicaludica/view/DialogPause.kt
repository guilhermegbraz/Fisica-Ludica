package br.edu.ufabc.fisicaludica.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import br.edu.ufabc.fisicaludica.R
import br.edu.ufabc.fisicaludica.databinding.DialogPauseBinding

class DialogPause(messages: List<String>, currentPosition: Int): DialogFragment() {

    private lateinit var binding: DialogPauseBinding
    private val messageList: List<String>
    private var currentPosition: Int

    init {
        messageList = messages
        this.currentPosition = currentPosition
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPauseBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        dialog.window?.setGravity(Gravity.CENTER)
        setupDialog()
        bindEvents()

        dialog.window?.let{window->
            val windowInsetsController =
                WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }

        return dialog
    }

    private fun setupDialog() {
        binding.dialogHintPauseTextviewHint.text = this.messageList[currentPosition]
        if(currentPosition == 0) {
            binding.dialogHintPausePreviusHint.setImageDrawable(
            ContextCompat.getDrawable(requireContext(),R.drawable.baseline_arrow_right_alt_24_desativado))
            binding.dialogHintPausePreviusHint.isEnabled = false
        }
        else {
            binding.dialogHintPausePreviusHint.setImageDrawable(
                ContextCompat.getDrawable(requireContext(),R.drawable.baseline_arrow_right_alt_24))
            binding.dialogHintPausePreviusHint.isEnabled = true
        }

        if(currentPosition == messageList.size - 1){
            binding.dialogHintPauseNextHint.setImageDrawable(
                ContextCompat.getDrawable(requireContext(),R.drawable.baseline_arrow_right_alt_24_desativado))
            binding.dialogHintPauseNextHint.isEnabled = false
        } else {
            binding.dialogHintPauseNextHint.setImageDrawable(
                ContextCompat.getDrawable(requireContext(),R.drawable.baseline_arrow_right_alt_24))
            binding.dialogHintPauseNextHint.isEnabled = true
        }
    }
    private fun bindEvents() {
        binding.dialogHintPauseExitButton.setOnClickListener {
            dialog?.dismiss()
        }
        binding.dialogHintPauseNextHint.setOnClickListener {
            currentPosition ++
            setupDialog()
        }
        binding.dialogHintPausePreviusHint.setOnClickListener {

            currentPosition--
            setupDialog()
        }

    }
}