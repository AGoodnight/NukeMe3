package com.example.nukeme3.screens

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

val pokemonNames = arrayOf(
    "cyndaquil",
    "marill",
    "umbreon",
    "gardevoir",
    "flygon",
    "lucario",
    "torterra",
    "chandelure",
    "mimikyu"
)

@Composable()
fun GridScreen(navToSub:(pokemon:String)->Unit){
    GridView(pokemonNames, navToSub)
}

@Composable
fun GridView(cards: Array<String>, navToSub:(pokemon:String)->Unit){
    LazyVerticalGrid(
        contentPadding =  PaddingValues(10.dp),
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(cards){ pokemon ->
            MyCell(pokemon,navToSub)
        }
    }
}

@Composable
fun MyCell(pokemon:String,navToSub:(pokemon:String)->Unit){
    ElevatedCard(
        modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            text = "Elevated",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        Button(onClick = { navToSub(pokemon )}) { Text("Button") }
    }
}