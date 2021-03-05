package com.list_of_companies.ui.current_company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class  CurrentCompanyViewModelFactory(): ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CurrentCompanyViewModel() as T
        }
    }

