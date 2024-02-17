package com.ags.controlekm.di

import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.ags.controlekm.viewModels.CurrentUserViewModel
import com.ags.controlekm.viewModels.login.LoginViewModel
import com.ags.controlekm.viewModels.login.validateFields.ValidateFieldsLogin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    @ViewModelScoped
    fun loginViewModel(
        validateFieldsLogin: ValidateFieldsLogin
    ): LoginViewModel{ return LoginViewModel(validateFieldsLogin) }
    @Provides
    @ViewModelScoped
    fun validateFieldsLogin(): ValidateFieldsLogin {
        return ValidateFieldsLogin()
    }
}