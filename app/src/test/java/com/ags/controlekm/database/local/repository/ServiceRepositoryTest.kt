package com.ags.controlekm.database.local.repository

import com.ags.controlekm.database.local.daos.ServiceDao
import com.ags.controlekm.database.local.repositories.ServiceRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.*
import org.junit.Assert.assertEquals
import com.ags.controlekm.database.models.Service

@RunWith(MockitoJUnitRunner::class)
class ServiceRepositoryTest {

    // Mocking ServiceDao
    @Mock
    private lateinit var mockServiceDao: ServiceDao

    // Class under test
    private lateinit var serviceRepository: ServiceRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        serviceRepository = ServiceRepository(mockServiceDao)
    }

    @Test
    fun getServicesCurrentUser_shouldReturnFlowOfServices() {
        // Arrange
        val mockFlow = flowOf<List<Service>>(listOf())
        `when`(mockServiceDao.getServicesCurrentUser(anyString())).thenReturn(mockFlow)

        // Act
        val result = serviceRepository.getServicesCurrentUser()

        // Assert
        assertEquals(mockFlow, result)
    }

    @Test
    fun getCurrentService_shouldReturnFlowOfService() {
        // Arrange
        val mockFlow = flowOf<Service>(Service(/* Mocked service */))
        `when`(mockServiceDao.getCurrentService(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(mockFlow)

        // Act
        val result = serviceRepository.getcurrentService()

        // Assert
        assertEquals(mockFlow, result)
    }

    @Test
    fun insert_shouldCallServiceDaoInsert() = runBlocking {
        // Arrange
        val mockService = mock(Service::class.java)

        // Act
        serviceRepository.insert(mockService)

        // Assert
        verify(mockServiceDao).insert(mockService)
    }

    @Test
    fun update_shouldCallServiceDaoUpdate() = runBlocking {
        // Arrange
        val mockService = mock(Service::class.java)

        // Act
        serviceRepository.update(mockService)

        // Assert
        verify(mockServiceDao).update(mockService)
    }

    @Test
    fun delete_shouldCallServiceDaoDelete() = runBlocking {
        // Arrange
        val mockService = mock(Service::class.java)

        // Act
        serviceRepository.delete(mockService)

        // Assert
        verify(mockServiceDao).delete(mockService)
    }
}