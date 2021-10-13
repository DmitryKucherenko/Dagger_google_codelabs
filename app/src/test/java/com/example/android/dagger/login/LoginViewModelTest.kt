/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.dagger.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.dagger.LiveDataTestUtil
import com.example.android.dagger.settings.UserComponent
import com.example.android.dagger.storage.FakeStorage
import com.example.android.dagger.user.UserManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever

class LoginViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var userManager: UserManager

    @Before
    fun setup() {
        // Return mock userComponent when calling the factory
        val userComponentFactory = Mockito.mock(UserComponent.Factory::class.java)
        val userComponent = Mockito.mock(UserComponent::class.java)
        whenever(userComponentFactory.create()).thenReturn(userComponent)

        val storage = FakeStorage()
        userManager = UserManager(storage, userComponentFactory)
    }

    @Test
    fun `Get username`() {
        whenever(userManager.username).thenReturn("Username")

        val username = viewModel.getUsername()

        assertEquals("Username", username)
    }

    @Test
    fun `Login emits success`() {
        whenever(userManager.loginUser(anyString(), anyString())).thenReturn(true)

        viewModel.login("username", "login")

        assertEquals(LiveDataTestUtil.getValue(viewModel.loginState), LoginSuccess)
    }

    @Test
    fun `Login emits error`() {
        whenever(userManager.loginUser(anyString(), anyString())).thenReturn(false)

        viewModel.login("username", "login")

        assertEquals(LiveDataTestUtil.getValue(viewModel.loginState), LoginError)
    }

    @Test
    fun `Login unregisters`() {
        viewModel.unregister()

        verify(userManager).unregister()
    }
}
