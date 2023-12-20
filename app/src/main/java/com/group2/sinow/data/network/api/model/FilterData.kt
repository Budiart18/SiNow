package com.group2.sinow.data.network.api.model

data class FilterData(
    val isNew: Boolean,
    val isPopular: Boolean,
    val isPromo: Boolean,
    val selectedCategories: List<String>,
    val selectedLevels: List<String>
)
