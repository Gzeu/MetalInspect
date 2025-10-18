package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.dao.InspectorDao
import com.metalinspect.app.data.database.entities.Inspector
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectorRepositoryImpl @Inject constructor(
    private val inspectorDao: InspectorDao
) : InspectorRepository {
    
    override fun getAllInspectors(): Flow<List<Inspector>> {
        return inspectorDao.getAllInspectors()
    }
    
    override fun getActiveInspectors(): Flow<List<Inspector>> {
        return inspectorDao.getActiveInspectors()
    }
    
    override suspend fun getInspectorById(id: String): Inspector? {
        return inspectorDao.getInspectorById(id)
    }
    
    override suspend fun getActiveInspector(): Inspector? {
        return inspectorDao.getActiveInspector()
    }
    
    override fun getInspectorsByCompany(company: String): Flow<List<Inspector>> {
        return inspectorDao.getInspectorsByCompany(company)
    }
    
    override suspend fun createInspector(inspector: Inspector): String {
        inspectorDao.insertInspector(inspector)
        return inspector.id
    }
    
    override suspend fun updateInspector(inspector: Inspector) {
        inspectorDao.updateInspector(inspector.copy(updatedAt = System.currentTimeMillis()))
    }
    
    override suspend fun deleteInspector(inspector: Inspector) {
        inspectorDao.deleteInspector(inspector)
    }
    
    override suspend fun setActiveInspector(id: String) {
        // Deactivate all inspectors first
        inspectorDao.deactivateAllInspectors()
        // Then activate the selected one
        inspectorDao.setActiveInspector(id)
    }
    
    override suspend fun getInspectorCount(): Int {
        return inspectorDao.getInspectorCount()
    }
}