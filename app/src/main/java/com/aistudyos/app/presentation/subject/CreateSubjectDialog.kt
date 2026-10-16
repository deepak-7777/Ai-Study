package com.aistudyos.app.presentation.subject

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.aistudyos.app.databinding.DialogCreateSubjectBinding

class CreateSubjectDialog(
    private val onCreate: (name: String, description: String?, color: String?) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogCreateSubjectBinding.inflate(layoutInflater)

        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        dialog.setCancelable(true)

        dialog.setOnShowListener {
            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // ✅ Transparent background (no black corners)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // ✅ Button actions
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnCreate.setOnClickListener {
            val name  = binding.etSubjectName.text.toString()
            val desc  = binding.etDescription.text.toString().ifBlank { null }
            val color = binding.etColor.text.toString().ifBlank { null }

            onCreate(name, desc, color)
            dialog.dismiss()
        }

        return dialog
    }
}