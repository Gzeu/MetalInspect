package com.metalinspect.app.presentation.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metalinspect.app.R
import com.metalinspect.app.data.pdf.PdfReportGenerator
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.presentation.viewmodel.ExportState
import com.metalinspect.app.presentation.viewmodel.PdfExportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectionDetailFragment : Fragment() {
    private val exportVM: PdfExportViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inspection_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = view.findViewById<com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton>(R.id.fabExportPdf)

        // Placeholder inspection data until wiring ViewModel
        val inspection = Inspection(
            id = "demo",
            vesselName = "Vessel",
            cargoDescription = "Steel coils",
            inspectionDate = java.util.Date(),
            inspectorName = "Inspector",
            location = "Constanța",
            weatherConditions = "Clear",
            cargoQuantity = 1000.0,
            cargoUnit = "MT",
            inspectionType = "loading",
            qualityGrade = null,
            moistureContent = null,
            contaminationLevel = null,
            overallCondition = "Good",
            notes = "N/A"
        )
        val defects: List<Defect> = emptyList()

        val generator = PdfReportGenerator(requireContext())

        fab.setOnClickListener {
            val fileName = "Inspection_${inspection.vesselName}_${java.text.SimpleDateFormat("yyyyMMdd").format(inspection.inspectionDate)}_${inspection.id}.pdf"
            exportVM.export(fileName) { out ->
                generator.generate(out, inspection, defects, emptyList<Bitmap>())
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(androidx.lifecycle.DefaultLifecycleObserver { })

        // Minimal state observer (could be StateFlow to LiveData via asLiveData)
        androidx.lifecycle.lifecycleScope.launchWhenStarted {
            exportVM.exportState.collect { state ->
                when (state) {
                    ExportState.Idle -> Unit
                    ExportState.Running -> Toast.makeText(requireContext(), "Exporting...", Toast.LENGTH_SHORT).show()
                    is ExportState.Success -> Toast.makeText(requireContext(), "Saved: ${state.uri}", Toast.LENGTH_LONG).show()
                    is ExportState.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
