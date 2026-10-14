package com.example.nukeme3.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.example.nukeme3.api.PokemonApi
import com.example.nukeme3.api.PokemonInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.map

sealed interface UiState {
    object Loading : UiState
    data class Success(val data: PokemonInfo) : UiState
    data class Error(val message: String) : UiState
}

class PokemonViewModel(private val pokemon:String) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val api = PokemonApi()

    val uiState = _uiState.asStateFlow()


    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            try {
                val details = api.getPokemon(pokemon) // If this is a suspend function

                _uiState.update{ UiState.Success(details) }
            } catch (e: Exception) {
                Log.e("Screen", "Error loading pokemon", e)
                _uiState.update{ UiState.Error("Failed to Load Pokemon: ${e.message}") }
            }
        }
    }
}

@Composable
fun Screen(pokemon: String, model: PokemonViewModel = PokemonViewModel(pokemon)) {
    val uiState by model.uiState.collectAsState()

    Column (modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                Text("Loading")
            }
            is UiState.Success -> {
                Text(state.data.id.toString())
                Text(state.data.name)
                state.data.abilities.forEach{ ability ->
                    Text(ability.ability.name)
                }
                Log.d("Url Image", "${PokemonApi.URLS.Art}/${state.data.id}",)
                AsyncImage(
                    model= "${PokemonApi.URLS.Art}/${state.data.id}.png",
                    contentDescription = "Image of ${state.data.name}",
                    modifier = Modifier.size(200.dp)
                )
            }
            is UiState.Error -> {
                Text(state.message) // Display the actual error message instead of static text

            }
        }
    }
}