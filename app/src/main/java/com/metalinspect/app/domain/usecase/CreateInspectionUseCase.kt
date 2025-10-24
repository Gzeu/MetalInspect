package com.metalinspect.app.domain.usecase

import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.repository.InspectionRepository
import javax.inject.Inject

/**
 * Use case for creating a new inspection
 */
class CreateInspectionUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(inspection: Inspection) = repository.createInspection(inspection)
}