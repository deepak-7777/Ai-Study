package com.aistudyos.app.presentation.subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aistudyos.app.databinding.BottomSheetCreateSubjectBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateSubjectBottomSheet(
    private val onCreate: (name: String, description: String?, color: String?) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCreateSubjectBinding? = null
    private val binding get() = _binding!!

    private val colorOptions = listOf("#6C63FF","#FF6584","#43C6AC","#F7971E","#12C2E9","#C471ED")
    private var selectedColor = colorOptions[0]

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = BottomSheetCreateSubjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorAdapter = ColorPickerAdapter(colorOptions) { color -> selectedColor = color }
        binding.rvColors.adapter = colorAdapter

        binding.btnCreate.setOnClickListener {
            val name = binding.etSubjectName.text.toString()
            val desc = binding.etSubjectDesc.text.toString().takeIf { it.isNotBlank() }
            if (name.isBlank()) {
                binding.tilSubjectName.error = "Name required"
                return@setOnClickListener
            }
            onCreate(name, desc, selectedColor)
            dismiss()
        }
        binding.btnCancel.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
