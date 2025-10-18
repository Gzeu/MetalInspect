package com.metalinspect.app.presentation.inspection.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.metalinspect.app.databinding.FragmentInspectionListBinding
import com.metalinspect.app.presentation.common.BaseFragment
import com.metalinspect.app.presentation.common.adapters.InspectionListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InspectionListFragment : BaseFragment() {
    
    private var _binding: FragmentInspectionListBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: InspectionListViewModel by viewModels()
    private lateinit var adapter: InspectionListAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInspectionListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun setupViews() {
        setupRecyclerView()
        setupFab()
    }
    
    private fun setupRecyclerView() {
        adapter = InspectionListAdapter(
            onInspectionClick = { inspection ->
                val action = InspectionListFragmentDirections
                    .actionInspectionsToInspectionDetail(inspection.id)
                findNavController().navigate(action)
            },
            onInspectionLongClick = { inspection ->
                viewModel.showInspectionOptions(inspection)
            }
        )
        
        binding.recyclerViewInspections.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@InspectionListFragment.adapter
        }
    }
    
    private fun setupFab() {
        binding.fabCreateInspection.setOnClickListener {
            val action = InspectionListFragmentDirections
                .actionInspectionsToCreateInspection()
            findNavController().navigate(action)
        }
    }
    
    override fun observeViewModel() {
        viewModel.inspections.collectWithLifecycle { inspections ->
            adapter.submitList(inspections)
            
            binding.emptyState.visibility = if (inspections.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        
        viewModel.isLoading.collectWithLifecycle { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.error.collectWithLifecycle { error ->
            showError(error)
        }
        
        viewModel.message.collectWithLifecycle { message ->
            showSuccess(message)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}