package com.example.nukeme3.api

import android.util.Log
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.StructureKind
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import kotlin.code

@Serializable
data class PokemonInfo(
    @SerialName("id") val id:Int,
    @SerialName("name") val name:String,
    @SerialName( "abilities") val abilities: Array<PokemonAbilities>
)

@Serializable
data class PokemonAbilities(
    @SerialName("ability") val ability: Ability
)

@Serializable
data class Ability(
    @SerialName("name") val name:String
)

@Serializable
data class PokemonList(
    @SerialName("results") val results:PokemonListItem
)

@Serializable
data class PokemonListItem(
    @SerialName("name") val name:String,
    @SerialName("url") val url:String
)

class PokemonApi {

    companion object URLS {
        val Info:String  = "https://pokeapi.co/api/v2/pokemon"
        val Art:String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork"
        val Sprite:String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val parseJson = Json { ignoreUnknownKeys = true }

    suspend fun getManyPokemon() = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("${URLS.Info}?limit=10&offset=10")
            .build()
        okHttpClient.newCall(request).execute().use { response ->
            Log.d("requesting", "respond ${response.isSuccessful}")
            if (!response.isSuccessful) {
                throw IOException("Server returned error code: ${response.code}")
            }

            // Safely read the body string
            val body = response.body
                ?: throw IOException("Response body was null")


            val bodyString = body.string()
            Log.d("t", bodyString)
            parseJson.decodeFromString<PokemonList>(bodyString)
        }
    }


    suspend fun getPokemon(name: String): PokemonInfo = withContext(Dispatchers.IO) {
        // Add explicit timeouts to prevent the network call from hanging forever

        Log.d("url","$URLS.Info$name")

        val request = Request.Builder()
            .url("${URLS.Info}/$name")
            .build()

        Log.d("requesting", "Requesxting1")
        okHttpClient.newCall(request).execute().use { response ->
            Log.d("requesting", "respond ${response.isSuccessful}")
            if (!response.isSuccessful) {
                throw IOException("Server returned error code: ${response.code}")
            }

            // Safely read the body string
            val body = response.body
                ?: throw IOException("Response body was null")


            val bodyString = body.string()
            Log.d("t", bodyString)
            parseJson.decodeFromString<PokemonInfo>(bodyString)
        }
    }


}



