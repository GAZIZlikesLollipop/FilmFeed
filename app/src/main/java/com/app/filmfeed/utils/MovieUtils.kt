package com.app.filmfeed.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.filmfeed.data.MovieRepository
import com.app.filmfeed.presentation.MovieViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class MovieViewModelFactory(private val movieRepository: MovieRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun formatWithSpaces(number: Long): String {
    val symbols = DecimalFormatSymbols.getInstance().apply {
        groupingSeparator = ' ' // задаёт пробел в роли разделителя тысяч
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(number)
}