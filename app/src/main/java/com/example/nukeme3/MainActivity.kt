package com.example.nukeme3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.nukeme3.screens.GridScreen
import com.example.nukeme3.screens.Screen
import com.example.nukeme3.ui.theme.NukeMe3Theme
import kotlinx.serialization.Serializable


@Serializable()
object HomeRoute: NavKey

@Serializable()
data class SubScreenRoute(val pokemon:String): NavKey

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backstack = rememberNavBackStack(HomeRoute)
            NukeMe3Theme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Small Top App Bar")
                            },
                            navigationIcon = {
                                if(backstack.size>1){
                                    IconButton (
                                        onClick = {
                                            // Same logic as your NavDisplay onBack handler
                                            backstack.removeLastOrNull()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Navigate back"
                                        )
                                    }
                                }
                            }
                        )
                    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(  innerPadding )){


                        NavDisplay(backstack, onBack={ backstack.removeLastOrNull()}, entryProvider = {key->
                            when(key){
                                is HomeRoute -> NavEntry(key){
                                    GridScreen({pokemon:String ->backstack.add(SubScreenRoute(pokemon))})
                                }
                                is SubScreenRoute -> NavEntry(key){
                                    Screen(key.pokemon)
                                }
                                else -> NavEntry(key){
                                    Text("Error")
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}




