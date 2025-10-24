package com.metalinspect.app.presentation.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.metalinspect.app.R
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.presentation.viewmodel.InspectionListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InspectionListFragment : Fragment(R.layout.fragment_inspection_list) {
    private val viewModel: InspectionListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerInspections)
        val empty = view.findViewById<TextView>(R.id.viewEmpty)
        val progress = view.findViewById<ProgressBar>(R.id.progressLoading)
        val adapter = InspectionAdapter { _ ->
            findNavController().navigate(R.id.action_list_to_detail)
        }
        recycler.adapter = adapter

        view.findViewById<View>(R.id.fabAdd).setOnClickListener {
            findNavController().navigate(R.id.action_list_to_editor)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                val isEmpty = state.inspections.isEmpty() && !state.isLoading && state.error == null
                empty.visibility = if (isEmpty) View.VISIBLE else View.GONE
                recycler.visibility = if (isEmpty || state.isLoading) View.GONE else View.VISIBLE

                if (state.error != null) {
                    Snackbar.make(view, state.error ?: "", Snackbar.LENGTH_LONG).show()
                }
                adapter.submitList(state.inspections)
            }
        }
    }
}

private class InspectionAdapter(
    val onClick: (Inspection) -> Unit
) : ListAdapter<Inspection, InspectionVH>(diff) {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): InspectionVH {
        val v = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_inspection, parent, false)
        return InspectionVH(v, onClick)
    }
    override fun onBindViewHolder(holder: InspectionVH, position: Int) = holder.bind(getItem(position))
}

private val diff = object : DiffUtil.ItemCallback<Inspection>() {
    override fun areItemsTheSame(oldItem: Inspection, newItem: Inspection) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Inspection, newItem: Inspection) = oldItem == newItem
}

private class InspectionVH(
    itemView: View,
    private val onClick: (Inspection) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Inspection) {
        itemView.findViewById<android.widget.TextView>(R.id.textTitle).text = item.vesselName
        itemView.findViewById<android.widget.TextView>(R.id.textSubtitle).text = item.cargoDescription
        itemView.setOnClickListener { onClick(item) }
    }
}
