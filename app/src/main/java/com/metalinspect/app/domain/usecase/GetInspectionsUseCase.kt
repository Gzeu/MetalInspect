package com.metalinspect.app.domain.usecase

import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.repository.InspectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for listing all inspections
 */
class GetInspectionsUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    operator fun invoke(): Flow<List<Inspection>> = repository.getAllInspections()
}