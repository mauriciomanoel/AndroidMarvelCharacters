package com.mauricio.marvel.network

import com.mauricio.marvel.characters.models.CharactersResult
import com.mauricio.marvel.utils.Constant.DEFAULT_ORDER
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("v1/public/series/{seriesId}/characters")
    suspend fun getCharactersInSeries(@Path("seriesId") seriesId: String, @Query("orderBy") orderBy: String = DEFAULT_ORDER): CharactersResult

    @GET("v1/public/characters/{characterId}/events")
    suspend fun getCharacterEvents(@Path("characterId") characterId: Long, @Query("offset") offset: Int, @Query("limit") limit: Int, @Query("orderBy") orderBy: String = DEFAULT_ORDER): CharactersResult
}