package com.mauricio.marvel.characters.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.characters.repositories.CharacterRepository.Companion.DEFAULT_PAGE_INDEX
import com.mauricio.marvel.network.RetrofitApiService
import com.mauricio.marvel.utils.Constant
import retrofit2.HttpException
import java.io.IOException

class CharacterPagingSource(
    private val apiService: RetrofitApiService,
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: DEFAULT_PAGE_INDEX

        return try {
            val response = apiService.getCharactersInSeries(Constant.SERIES_ID, page, params.loadSize)
            LoadResult.Page(
                response.data.results,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.data.results.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}