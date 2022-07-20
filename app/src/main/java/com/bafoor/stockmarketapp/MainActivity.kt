package com.bafoor.stockmarketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bafoor.stockmarketapp.presentation.company_info.CompanyInfoScreen
import com.bafoor.stockmarketapp.presentation.company_listings.CompanyListingsScreen

import com.bafoor.stockmarketapp.ui.theme.StockMarketAppTheme
import com.bafoor.stockmarketapp.util.Screen


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockMarketAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CompanyListingScreen.route
                    ){
                        composable(
                            route = Screen.CompanyListingScreen.route
                        ) {
                            CompanyListingsScreen(navController)
                        }
                        composable(
                            route = Screen.CompanyInfoScreen.route +"/{symbol}"
                        ) {
                            CompanyInfoScreen()
                        }
                    }

                }
            }
        }
    }
}
