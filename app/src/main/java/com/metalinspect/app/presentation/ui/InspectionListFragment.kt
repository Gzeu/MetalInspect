package com.metalinspect.app.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.metalinspect.app.R
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.presentation.viewmodel.InspectionListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InspectionListFragment : Fragment() {
    private val viewModel: InspectionListViewModel by viewModels()
    private val adapter = InspectionAdapter { inspection ->
        val action = com.metalinspect.app.R.id.action_list_to_detail
        val args = Bundle().apply { putString("inspectionId", inspection.id) }
        findNavController().navigate(action, args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inspection_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerInspections)
        recycler.adapter = adapter

        view.findViewById<View>(R.id.fabAdd).setOnClickListener {
            findNavController().navigate(R.id.action_list_to_editor)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                adapter.submitList(state.inspections)
            }
        }
    }
}

private class InspectionAdapter(
    val onClick: (Inspection) -> Unit
) : ListAdapter<Inspection, InspectionVH>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_inspection, parent, false)
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
        itemView.findViewById<TextView>(R.id.textTitle).text = item.vesselName
        itemView.findViewById<TextView>(R.id.textSubtitle).text = item.cargoDescription
        itemView.setOnClickListener { onClick(item) }
    }
}
