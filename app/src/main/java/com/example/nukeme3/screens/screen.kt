package com.example.nukeme3.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nukeme3.api.getPokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException // <-- FIX: Use java.io.IOException instead of okio

sealed interface UiState {
    object Loading : UiState
    data class Success(val data: String) : UiState
    data class Error(val message: String) : UiState
}

class MyViewModel : ViewModel() {
    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            Log.d("123", "hill")
            try {
                val details = getPokemon("pikachu") // If this is a suspend function
                Log.d("hi", details)
                uiState = UiState.Success(details)
            } catch (e: Exception) {
                Log.e("Screen", "Error loading pokemon", e)
                uiState = UiState.Error("Failed to Load Pokemon: ${e.message}")
            }
        }
    }
}

@Composable
fun Screen(pokemon: String, model: MyViewModel = MyViewModel()) {
    var uiState = model.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                Text("Loading")
            }
            is UiState.Success -> {
                Text(state.data)
            }
            is UiState.Error -> {
                Text(state.message) // Display the actual error message instead of static text
            }
        }
    }
}