package com.mauricio.marvel.network

import com.mauricio.marvel.characters.CharactersResult
import retrofit2.http.GET

interface RetrofitApiService {

    @GET("v1/public/series/13320/characters")
    suspend fun getCharactersInSeries(): CharactersResult
//
//    @GET("v1/images/search")
//    suspend fun getBreedsById(@Query("breed_id") breedId: String, @Query("limit") limit: Int = 100): BreedsByIdResult
}