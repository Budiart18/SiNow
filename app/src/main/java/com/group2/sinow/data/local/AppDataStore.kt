package com.group2.sinow.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.appDataStore by preferencesDataStore(
    name = "SinowDataStore"
)
