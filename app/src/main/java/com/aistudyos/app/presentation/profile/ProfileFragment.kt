package com.aistudyos.app.presentation.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aistudyos.app.R
import com.aistudyos.app.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private var imageUri: Uri? = null
    private var cropUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupClicks()
        observeUser()
        observeLoading()
        observeMessage()

        // 🔥 Open full image
        binding.profileImage.setOnClickListener {
            val bundle = Bundle().apply {
                putString("image_url", viewModel.user.value?.avatarUrl)
            }
            findNavController().navigate(R.id.profileViewFragment, bundle)
        }

    }

    private fun observeMessage() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collect { msg ->
                    msg?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collect { isLoading ->

                    binding.imageLoader.visibility =
                        if (isLoading) View.VISIBLE else View.GONE
                    binding.ivEditImage.visibility =
                        if (isLoading) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun setupClicks() {

        binding.ivEditImage.setOnClickListener {
            checkPermissionAndOpen()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout(requireActivity())
        }

        binding.ProfileToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvName.setOnClickListener {
            showEditDialog("name", binding.tvName.text.toString())
        }

        binding.tvEmail.setOnClickListener {
            Toast.makeText(requireContext(), "Email can't be changed", Toast.LENGTH_SHORT).show()
        }

        binding.etPhone.setOnClickListener {
            showEditDialog("phone", binding.etPhone.text.toString())
        }

        binding.etAddress.setOnClickListener {
            showEditDialog("about", binding.etAddress.text.toString())
        }
    }

    private fun showEditDialog(field: String, oldValue: String) {

        val dialogView = layoutInflater.inflate(R.layout.item_edittext, null)

        val editText = dialogView.findViewById<EditText>(R.id.dialogEditText)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        editText.setText(oldValue)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            val newValue = editText.text.toString().trim()

            if (newValue.isNotEmpty()) {
                viewModel.updateField(field, newValue)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->

                    Glide.with(requireContext())
                        .load(user?.avatarUrl)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(binding.profileImage)

                    binding.tvName.setText(user?.name ?: "")
                    binding.tvEmail.setText(user?.email ?: "")
                    binding.etPhone.setText(user?.phone ?: "")
                    binding.etAddress.setText(user?.about ?: "")
                }
            }
        }
    }

    // 🔥 Permission
    private fun checkPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openImageChooser()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) openImageChooser()
        }

    // 🔥 Image chooser
    private fun openImageChooser() {

        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val file = File.createTempFile(
            "profile_${System.currentTimeMillis()}",
            ".jpg",
            requireContext().cacheDir
        )

        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        val chooser = Intent.createChooser(galleryIntent, "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        chooserLauncher.launch(chooser)
    }

    // 🔥 Chooser result
    private val chooserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: imageUri
                uri?.let { startCrop(it) }
            }
        }

    // 🔥 Crop
    private fun startCrop(uri: Uri) {
        val destinationFile = File(requireContext().cacheDir, "cropped.jpg")
        cropUri = Uri.fromFile(destinationFile)

        UCrop.of(uri, cropUri!!)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)
            .start(requireContext(), this)
    }

    // 🔥 Crop result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {

            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                val compressedFile = compressImage(it)
                viewModel.uploadImage(Uri.fromFile(compressedFile))
            }
        }
    }

    // 🔥 Compress
    private fun compressImage(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val file = File(requireContext().cacheDir, "compressed.jpg")
        val outputStream = file.outputStream()

        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 60, outputStream)

        outputStream.flush()
        outputStream.close()

        return file
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}