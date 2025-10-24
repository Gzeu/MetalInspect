package com.metalinspect.app.domain.usecase

import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.repository.InspectionRepository
import javax.inject.Inject

/**
 * Use case for adding a defect to an inspection
 */
class AddDefectUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(defect: Defect) = repository.createDefect(defect)
}