package com.list_of_companies.ui.companies_list

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.list_of_companies.R
import com.list_of_companies.data.CompaniesResponseItem
import com.list_of_companies.network.ApiServiceCompanyList
import com.list_of_companies.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.companies_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CompaniesListFragment : ScopedFragment(),KodeinAware{

    override val kodein by closestKodein()
    private val viewModelFactory : CompaniesListViewModelFactory by instance()

    private lateinit var viewModel: CompaniesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.companies_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CompaniesListViewModel::class.java)
        bindUI()

    }

    private fun bindUI()  {
        if (checkInternet(context!!.applicationContext)) {
            val apiService = ApiServiceCompanyList()
            GlobalScope.launch(Dispatchers.Main) {
                initRecyclerView(apiService.getCompanyListAsync().await().toListItems())
                group_loading.visibility = View.GONE
            }
        } else{
            group_loading.visibility = View.GONE
            tv_error_message.visibility = View.VISIBLE

        }

    }

    private fun List<CompaniesResponseItem>.toListItems() : List<CompaniesListItem> {
        return this.map {
            CompaniesListItem(it,context!!.applicationContext)
        }
    }

    private fun initRecyclerView(items: List<CompaniesListItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CompaniesListFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? CompaniesListItem)?.let {
                showCompaniesDetailDetailedInfo(it.entry.id, view)

            }
        }
    }

    private fun showCompaniesDetailDetailedInfo(id: String, view: View){
        val actionDetail = CompaniesListFragmentDirections.actionDetail(id)

        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun checkInternet(context: Context): Boolean {
        val connect = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return !(!wifi.isConnected && !mobile.isConnected)
    }

}