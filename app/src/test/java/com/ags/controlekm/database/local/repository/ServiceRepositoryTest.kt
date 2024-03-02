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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner::class)
class ServiceRepositoryTest {
    @Mock
    private lateinit var mockServiceDao: ServiceDao
    private lateinit var serviceRepository: ServiceRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        serviceRepository = ServiceRepository(mockServiceDao)
    }

    @Test
    fun `getAllServices should return services from dao`() = runBlocking {
        val services = listOf(
            Service(id = "1", serviceAddress = "Address 1"),
            Service(id = "2", serviceAddress = "Address 2"),
            Service(id = "3", serviceAddress = "Address 3")
        )

        `when`(mockServiceDao.getAllServices()).thenReturn(flow { emit(services) })

        val result = serviceRepository.getAllServices()

        result.collect {
            assertEquals(services, it)
        }

        verify(mockServiceDao).getAllServices()

        verifyNoMoreInteractions(mockServiceDao)
    }

    @Test
    fun `getServicesCurrentUser should return services with the same technicianId as currentUserId`() = runBlocking {
        // Configuração do cenário de teste
        val currentUserId = "user_01"
        val services = listOf(
            Service(id = "1", technicianId = "user_01", statusService = "Pending"),
            Service(id = "2", technicianId = "user_01", statusService = "Completed"),
            Service(id = "3", technicianId = "user_02", statusService = "Pending"),
            Service(id = "4", technicianId = "user_03", statusService = "Completed")
        )

        `when`(mockServiceDao.getServicesCurrentUser(currentUserId))
            .thenReturn(flowOf(services.filter { it.technicianId == currentUserId }))

        var result = emptyList<Service>()
         serviceRepository.getServicesCurrentUser(currentUserId).collect{
            result = it
        }

        assertTrue(result.isNotEmpty())
        assertTrue(result.all { it.technicianId == currentUserId })


        verify(mockServiceDao).getServicesCurrentUser(currentUserId)

        verifyNoMoreInteractions(mockServiceDao)
    }

    @Test
    fun `getCurrentService should return service in pending`() = runBlocking {
        // Configuração do cenário de teste
        val currentUserId = "user_01"
        val statusExpected = "Em rota"
        val services = listOf(
            Service(id = "1", technicianId = "user_01", statusService = statusExpected),
            Service(id = "2", technicianId = "user_01", statusService = "Completed"),
            Service(id = "3", technicianId = "user_02", statusService = "Completed"),
            Service(id = "4", technicianId = "user_03", statusService = "Completed")
        )

        `when`(mockServiceDao.getCurrentService(
            currentUserId,
            "Em rota",
            "Em andamento",
            "Em rota, retornando"))
            .thenReturn(flowOf(services.find { it.statusService == statusExpected }) as Flow<Service>?)

        val result = serviceRepository.getCurrentService(currentUserId)

        val expected = services.filter { it.statusService == statusExpected }

        assertNotNull(result)

        result!!.collect{
            assertEquals(expected, listOf(it))
        }

        verify(mockServiceDao).getCurrentService(
            currentUserId,
            "Em rota",
            "Em andamento",
            "Em rota, retornando")

        verifyNoMoreInteractions(mockServiceDao)
    }

}