package com.metalinspect.app.presentation.inspection.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.metalinspect.app.databinding.FragmentInspectionDetailBinding
import com.metalinspect.app.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionDetailFragment : BaseFragment() {
    
    private var _binding: FragmentInspectionDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: InspectionDetailViewModel by viewModels()
    private val args: InspectionDetailFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInspectionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun setupViews() {
        binding.btnTakePhoto.setOnClickListener {
            // Navigate to camera
            val action = InspectionDetailFragmentDirections
                .actionDetailToCamera(args.inspectionId)
            findNavController().navigate(action)
        }
        
        binding.btnAddDefect.setOnClickListener {
            // Show add defect dialog
            viewModel.showAddDefectDialog()
        }
        
        binding.btnCompleteInspection.setOnClickListener {
            viewModel.completeInspection(args.inspectionId)
        }
    }
    
    override fun observeViewModel() {
        viewModel.inspectionDetails.collectWithLifecycle { details ->
            details?.let {
                binding.textViewLotNumber.text = it.inspection.lotNumber
                binding.textViewStatus.text = it.inspection.status.name
                binding.textViewPhotoCount.text = it.photoCount.toString()
                binding.textViewDefectCount.text = it.totalDefects.toString()
            }
        }
        
        viewModel.isLoading.collectWithLifecycle { isLoading ->
            // Handle loading state
        }
        
        viewModel.error.collectWithLifecycle { error ->
            showError(error)
        }
        
        viewModel.message.collectWithLifecycle { message ->
            showSuccess(message)
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadInspectionDetails(args.inspectionId)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}