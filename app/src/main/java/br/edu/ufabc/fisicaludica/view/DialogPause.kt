package br.edu.ufabc.fisicaludica.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import br.edu.ufabc.fisicaludica.R
import br.edu.ufabc.fisicaludica.databinding.DialogPauseBinding

class DialogPause: DialogFragment() {

    private lateinit var binding: DialogPauseBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPauseBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        dialog.window?.setGravity(Gravity.CENTER)
        bindEvents()

        dialog.window?.let{window->
            val windowInsetsController =
                WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }

        return dialog
    }

    private fun bindEvents() {
        binding.dialogHintPauseExitButton.setOnClickListener {
            dialog?.dismiss()
        }
        val anterior = binding.dialogHintPauseTextviewHint.text.toString()
        binding.dialogHintPauseNextHint.setOnClickListener {
            binding.dialogHintPauseTextviewHint.text = String.
            format(getString(R.string.dialog_hint_pause_textView_inside_scroll_area), "$anterior NEXT HINT")
        }
        binding.dialogHintPausePreviusHint.setOnClickListener {
            binding.dialogHintPauseTextviewHint.text = String.
            format(getString(R.string.dialog_hint_pause_textView_inside_scroll_area), "$anterior PREVIOUS HINT")
        }
    }
}