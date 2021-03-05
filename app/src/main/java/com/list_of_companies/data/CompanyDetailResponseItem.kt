package com.list_of_companies.data




data class CompanyDetailResponseItem(
    val description: String,
    val id: String,
    val img: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val phone: String,
    val www: String
)