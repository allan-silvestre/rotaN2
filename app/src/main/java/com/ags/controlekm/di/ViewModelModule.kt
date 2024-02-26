package com.ags.controlekm.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ags.controlekm.database.local.repositories.CurrentUserRepository
import com.ags.controlekm.database.remote.repositories.FirebaseCurrentUserRepository
import com.ags.controlekm.ui.views.app.viewModel.AppViewModel
import com.ags.controlekm.ui.views.login.viewModel.LoginViewModel
import com.ags.controlekm.ui.views.login.viewModel.validateFields.ValidateFieldsLogin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    @ViewModelScoped
    fun loginViewModel(
        validateFieldsLogin: ValidateFieldsLogin
    ): LoginViewModel { return LoginViewModel(validateFieldsLogin) }

    @Provides
    @ViewModelScoped
    fun validateFieldsLogin(): ValidateFieldsLogin {
        return ValidateFieldsLogin()
    }
}