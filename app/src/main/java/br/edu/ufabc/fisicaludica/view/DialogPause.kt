package br.edu.ufabc.fisicaludica.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import br.edu.ufabc.fisicaludica.R
import br.edu.ufabc.fisicaludica.databinding.DialogPauseBinding

class DialogPause(): DialogFragment() {

    lateinit var binding: DialogPauseBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPauseBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        dialog.window?.setGravity(Gravity.CENTER)

        return dialog
    }
}