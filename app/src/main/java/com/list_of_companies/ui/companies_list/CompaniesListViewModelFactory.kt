package com.list_of_companies.ui.companies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class  CompaniesListViewModelFactory(): ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CompaniesListViewModel() as T
        }
    }

