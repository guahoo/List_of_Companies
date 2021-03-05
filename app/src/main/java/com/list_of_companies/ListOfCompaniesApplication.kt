package com.list_of_companies

import android.app.Application
import com.list_of_companies.network.ApiServiceCompanyList
import com.list_of_companies.ui.companies_list.CompaniesListViewModelFactory
import com.list_of_companies.ui.current_company.CurrentCompanyViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ListOfCompaniesApplication : Application(),KodeinAware {
    override val kodein: Kodein
        get() = Kodein.lazy {
            import(androidXModule(this@ListOfCompaniesApplication))
            bind() from singleton { ApiServiceCompanyList() }
            bind() from provider { CompaniesListViewModelFactory() }
            bind() from provider { CurrentCompanyViewModelFactory() }
        }

}