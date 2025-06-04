package com.app.filmfeed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.screen.CatalogScreen
import com.app.filmfeed.presentation.screen.MainScreen
import com.app.filmfeed.presentation.screen.MyScreen
import com.app.filmfeed.presentation.screen.SearchScreen
import com.app.filmfeed.presentation.screen.member.MemberScreen
import com.app.filmfeed.presentation.screen.member.MembersScreen
import com.app.filmfeed.presentation.screen.movie.AboutScreen
import com.app.filmfeed.presentation.screen.movie.MovieScreen

@Composable
fun Navigation(
    movieViewModel: MovieViewModel,
    padding: PaddingValues,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Route.Main.route
    ){
        composable(Route.Main.route){
            MainScreen(movieViewModel,padding,navController)
        }
        composable(Route.Catalog.route){
            CatalogScreen()
        }
        composable(Route.My.route){
            MyScreen()
        }
        composable(Route.Search.route){
            SearchScreen()
        }
        composable(
            route = Route.AboutMovie.route,
            arguments = listOf(navArgument("id"){type = NavType.LongType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 2
            AboutScreen(id)
        }
        composable(
            route = Route.Movie.route,
            arguments = listOf(navArgument("id"){type = NavType.LongType})
        ){
            MovieScreen(it.arguments?.getLong("id") ?: 0)
        }
        composable(
            route = Route.Member.route,
            arguments = listOf(navArgument("id"){type = NavType.LongType})
        ){
            MemberScreen(it.arguments?.getLong("id") ?: 0)
        }
        composable(
            route = Route.Members.route,
            arguments = listOf(navArgument("id"){type = NavType.LongType})
        ){
            MembersScreen(it.arguments?.getLong("id") ?: 0)
        }

    }
}

sealed class Route(val route: String){
    object Main: Route("main")
    object Catalog: Route("catalog")
    object My: Route("my")
    object Search: Route("search")
    object Movie: Route("movie/{id}"){
        fun createRoute(id: Long) = "movie/${id}"
    }
    object Member: Route("member/{id}"){
        fun createRoute(id: Long) = "member/${id}"
    }
    object Members: Route("members/{id}"){
        fun createRoute(id: Long) = "members/${id}"
    }
    object AboutMovie: Route("aboutMovie/{id}"){
        fun createRoute(id: Long) = "aboutMovie/$id"
    }
}