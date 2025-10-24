package com.metalinspect.app.presentation.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.metalinspect.app.R
import com.metalinspect.app.data.pdf.PdfReportGenerator
import com.metalinspect.app.presentation.viewmodel.ExportState
import com.metalinspect.app.presentation.viewmodel.InspectionDetailViewModel
import com.metalinspect.app.presentation.viewmodel.PdfExportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class InspectionDetailFragment : Fragment() {
    private val args by navArgs<InspectionDetailFragmentArgs>()
    private val vm: InspectionDetailViewModel by viewModels()
    private val exportVM: PdfExportViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_inspection_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.load(args.inspectionId)

        val fab = view.findViewById<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton>(R.id.fabExportPdf)
        val generator = PdfReportGenerator(requireContext())

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.uiState.collectLatest { state ->
                // TODO bind views (title, meta, cargo, notes, defects recycler)
                fab.isEnabled = state.inspection != null && !state.isLoading
                if (state.error != null) {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                }

                fab.setOnClickListener {
                    val insp = state.inspection ?: return@setOnClickListener
                    val fileName = "Inspection_${insp.vesselName}_${java.text.SimpleDateFormat("yyyyMMdd").format(insp.inspectionDate)}_${insp.id}.pdf"
                    exportVM.export(fileName) { out ->
                        generator.generate(out, insp, state.defects, emptyList<Bitmap>())
                    }
                }

                // Bind defects list (if you have an adapter)
                view.findViewById<RecyclerView>(R.id.recyclerDefects).adapter?.notifyDataSetChanged()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            exportVM.exportState.collectLatest { st ->
                when (st) {
                    ExportState.Idle -> Unit
                    ExportState.Running -> Toast.makeText(requireContext(), getString(R.string.label_export_pdf), Toast.LENGTH_SHORT).show()
                    is ExportState.Success -> Toast.makeText(requireContext(), "Saved: ${st.uri}", Toast.LENGTH_LONG).show()
                    is ExportState.Error -> Toast.makeText(requireContext(), st.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
