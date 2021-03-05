package com.list_of_companies.ui.companies_list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.list_of_companies.R
import com.list_of_companies.data.CompaniesResponseItem
import com.list_of_companies.ui.current_company.GLIDE_URL
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_companies_list.*


class CompaniesListItem(val entry: CompaniesResponseItem, private val context: Context) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.apply {
            updateCompanyId(entry.id)
            updateCompanyName(entry.name)
            updateCompanyImage(entry.img)

        }

    }

    override fun getLayout(): Int = R.layout.item_companies_list


    private fun ViewHolder.updateCompanyId(id: String) {
        tv_id.text = id
    }

    private fun ViewHolder.updateCompanyName(name: String) {
        tv_company_name.text = name
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun ViewHolder.updateCompanyImage(imgUrl: String) {
        val mDefaultBackground: Drawable = context.resources.getDrawable(R.drawable.ic_no_logo)
        Glide.with(containerView)
            .load("$GLIDE_URL${imgUrl}")
            .error(mDefaultBackground)
            .into(small_company_label)

    }


}

