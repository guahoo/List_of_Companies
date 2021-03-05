package com.list_of_companies.ui.current_company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.list_of_companies.R
import com.list_of_companies.data.CompanyDetailResponseItem
import com.list_of_companies.internal.WorkaroundMapFragment
import com.list_of_companies.network.ApiServiceCompanyList
import com.list_of_companies.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.company_detail_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


const val GLIDE_URL = "https://lifehack.studio/test_task/"
private lateinit var adress: LatLng

class CurrentCompanyFragment : ScopedFragment(), KodeinAware, OnMapReadyCallback {
    private val args: CurrentCompanyFragmentArgs by navArgs()
    private lateinit var mMap: GoogleMap
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentCompanyViewModelFactory by instance()
    private lateinit var viewModel: CurrentCompanyViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.company_detail_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrentCompanyViewModel::class.java)


        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as WorkaroundMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            mMap.uiSettings.isZoomControlsEnabled = true
            (childFragmentManager.findFragmentById(R.id.mapFragment) as WorkaroundMapFragment)
                    .setListener(object : WorkaroundMapFragment.OnTouchListener {
                        override fun onTouch() {
                            main_scrollView.requestDisallowInterceptTouchEvent(true)
                        }
                    })
        }
        MapsInitializer.initialize(activity?.applicationContext)

        bindUI()
    }

    private fun bindUI() {
        val apiService = ApiServiceCompanyList()
        GlobalScope.launch(Dispatchers.Main) {
            val entries: CompanyDetailResponseItem

            try {
                entries = apiService.getCompanyDetailAsync(args.companyName).await()[0]
                updateCompanyTitle(entries.name)
                updateCompanyDescription(entries.description)
                updateCompanyIcon(entries.img)
                updateLocation(entries.lat, entries.lon, entries.name)
                updateCompanyPhone(entries.phone)
                updateCompanyCite(entries.www)

                group_loading_detail.visibility = View.GONE
                main_layout.visibility = View.VISIBLE

            } catch (je: Exception) {
                group_loading_detail.visibility = View.GONE
                tv_error_message.visibility = View.VISIBLE
                println("error in " + args.companyName)
            }
        }
    }

    private fun updateCompanyDescription(description: String) {
        tv_description.text = description
    }

    private fun updateCompanyCite(cite: String) {
        if (cite == "") tv_cite.visibility = View.GONE
        tv_cite.text = ("cite: $cite")
    }

    private fun updateCompanyPhone(phone: String) {
        if (phone == "") tv_phone.visibility = View.GONE
        tv_phone.text = ("phone: $phone")
    }

    private fun updateLocation(latitude: Double, longitude: Double, companyName: String) {
        adress = LatLng(latitude, longitude)

        if (latitude == 0.0 && longitude == 0.0) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            childFragmentManager.beginTransaction().hide(mapFragment).commit()
        }


        mMap.addMarker(MarkerOptions().position(adress).title(companyName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adress, 18f))

    }

    private fun updateCompanyIcon(imgUrl: String) {
        Glide.with(this@CurrentCompanyFragment)
                .load("$GLIDE_URL${imgUrl}")
                .into(company_icon)
    }

    private fun updateCompanyTitle(name: String) {
        tv_company_name.text = name
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }


}