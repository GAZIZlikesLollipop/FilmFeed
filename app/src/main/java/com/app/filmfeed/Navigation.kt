package com.app.filmfeed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.screen.MainScreen
import com.app.filmfeed.presentation.screen.MyScreen
import com.app.filmfeed.presentation.screen.SearchScreen
import com.app.filmfeed.presentation.screen.member.MemberScreen
import com.app.filmfeed.presentation.screen.member.MembersScreen
import com.app.filmfeed.presentation.screen.movie.AboutScreen
import com.app.filmfeed.presentation.screen.movie.WatchScreen

@Composable
fun Navigation(
    movieViewModel: MovieViewModel,
    padding: PaddingValues,
    navController: NavHostController
){
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    LaunchedEffect(currentRoute) {
        if(
            currentRoute == Route.Main.route ||
            currentRoute == Route.My.route ||
            currentRoute == Route.Search.route
        ){
            movieViewModel.previousRoute = currentRoute
        }
    }
    NavHost(
        navController = navController,
        startDestination = Route.Main.route
    ){
        composable(Route.Main.route){
            MainScreen(movieViewModel,navController,padding)
        }
        composable(Route.My.route){
            MyScreen(movieViewModel,navController,padding)
        }
        composable(Route.Search.route){
            SearchScreen(
                viewModel = movieViewModel,
                paddingValues = padding,
                navController = navController
            )
        }
        composable(
            route = Route.AboutMovie.route,
            arguments = listOf(navArgument("id"){type = NavType.LongType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 2
            AboutScreen(id,navController,movieViewModel)
        }
        composable(
            route = Route.Watch.route,
            arguments = listOf(navArgument("movieId"){type = NavType.LongType})
        ){
            WatchScreen(
                movieId = it.arguments?.getLong("movieId") ?: 0,
                viewModel = movieViewModel
            )
        }
        composable(
            route = Route.Member.route,
            arguments = listOf(
                navArgument("memberId"){type = NavType.LongType},
                navArgument("movieId"){type = NavType.LongType}
            )
        ){
            MemberScreen(
                movieId = it.arguments?.getLong("movieId") ?: 0,
                memberId = it.arguments?.getLong("memberId") ?: 0,
                paddingValues = padding,
                viewModel = movieViewModel,
                navController = navController
            )
        }
        composable(
            route = Route.Members.route,
            arguments = listOf(navArgument("id"){type = NavType.LongType})
        ){
            MembersScreen(it.arguments?.getLong("id") ?: 0,padding,navController,movieViewModel)
        }
    }
}

sealed class Route(val route: String){
    object Main: Route("main")
    object My: Route("my")
    object Search: Route("search")
    object Watch: Route("watch/{movieId}"){
        fun createRoute(movieId: Long) = "watch/$movieId"
    }
    object Members: Route("member/{id}"){
        fun createRoute(id: Long) = "member/${id}"
    }
    object Member: Route("members/{movieId}/{memberId}"){
        fun createRoute(movieId: Long,memberId: Long) = "members/$movieId/$memberId"
    }
    object AboutMovie: Route("aboutMovie/{id}"){
        fun createRoute(id: Long) = "aboutMovie/$id"
    }
}