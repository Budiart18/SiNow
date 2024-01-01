package com.group2.sinow.model.filter

data class Filter(
    val selectedCategories: List<Int>? = null,
    val selectedLevels: List<String>? = null,
    val sortBy: String? = null
)
