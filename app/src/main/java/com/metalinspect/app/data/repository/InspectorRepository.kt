package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.Inspector

interface InspectorRepository {
    fun getAllInspectors(): Flow<List<Inspector>>
    fun getActiveInspectors(): Flow<List<Inspector>>
    suspend fun getInspectorById(id: String): Inspector?
    suspend fun getActiveInspector(): Inspector?
    fun getInspectorsByCompany(company: String): Flow<List<Inspector>>
    
    suspend fun createInspector(inspector: Inspector): String
    suspend fun updateInspector(inspector: Inspector)
    suspend fun deleteInspector(inspector: Inspector)
    suspend fun setActiveInspector(id: String)
    
    suspend fun getInspectorCount(): Int
}