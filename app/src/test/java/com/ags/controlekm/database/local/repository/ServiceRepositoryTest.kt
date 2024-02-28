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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import org.junit.runners.JUnit4

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
        // Given
        val fakeServices = listOf(
            Service(id = "1", serviceAddress = "Address 1"),
            Service(id = "2", serviceAddress = "Address 2"),
            Service(id = "3", serviceAddress = "Address 3")
        )

        `when`(mockServiceDao.getAllServices()).thenReturn(flow { emit(fakeServices) })

        // When
        val result = serviceRepository.getAllServices()

        // Then
        result.collect { services ->
            assertEquals(fakeServices, services)
        }
    }
}