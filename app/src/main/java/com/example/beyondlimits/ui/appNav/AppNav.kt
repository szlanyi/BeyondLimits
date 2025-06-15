package com.example.beyondlimits.ui.appNav

import HomeScreen
import SlideshowScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.beyondlimits.R
import com.example.beyondlimits.ui.auth.LoginScreen
import com.example.beyondlimits.ui.gallery.GalleryScreen
import com.example.beyondlimits.util.DrawerItem
import com.example.beyondlimits.util.Gallery
import com.example.beyondlimits.util.Home
import com.example.beyondlimits.util.Login
import com.example.beyondlimits.util.Register
import com.example.beyondlimits.util.Slideshow
import kotlinx.coroutines.launch
import androidx.navigation.compose.navigation
import com.example.beyondlimits.ui.auth.AuthViewModel
import com.example.beyondlimits.util.AuthNav
import com.example.beyondlimits.util.Main


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(vm: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerItems = listOf(
        DrawerItem("Home", R.drawable.baseline_home_24, Home),
        DrawerItem("Gallery", R.drawable.ic_menu_gallery, Gallery),
        DrawerItem("Slideshow", R.drawable.ic_menu_slideshow, Slideshow)
    )

    // Observe current backstack route for drawer selection
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(navController = navController, startDestination = AuthNav) {
        navigation<AuthNav>(startDestination = Login) {
            composable<Login> {
                LoginScreen(
                    viewModel = viewModel(),
                    onAuthSuccess = {
                        navController.navigate(Main) {
                        popUpTo(AuthNav) { inclusive = true } // removes login from backstack
                    }}
                )
            }
        }
        navigation<Main>(startDestination = Home) {
            composable<Home> { HomeScreen(vm = viewModel()) }
            composable<Gallery> { GalleryScreen(vm = viewModel()) }
            composable<Slideshow> { SlideshowScreen(vm = viewModel()) }
        }
    }
    val navController2 = rememberNavController()

    // Show drawer + scaffold only when in main routes
    if (currentRoute in drawerItems.map { it.route::class.qualifiedName }) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(text = "Felhasználónév: Robert", color = Color.Black)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Email: john.mclean@examplepetstore.com",
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    drawerItems.forEach { item ->
                        NavigationDrawerItem(
                            label = { Text(item.label) },
                            icon = {
                                Icon(
                                    painter = painterResource(item.iconRes),
                                    contentDescription = item.label
                                )
                            },
                            selected = currentRoute == item.route::class.qualifiedName,
                            onClick = {
                                navController2.navigate(item.route) {
                                    launchSingleTop = true
                                    popUpTo(Home)
                                }
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        vm.logout()
                        navController.navigate(AuthNav) {
                            popUpTo(Main) { inclusive = true }
                        }
                    }) {
                        Text("Logout")
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(Modifier.fillMaxWidth().padding(start = 50.dp),horizontalArrangement = Arrangement.Center) {
                                Text("Beyond Limits")
                            }

                        },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Black,
                            titleContentColor = Color.White
                        )
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController2,
                    startDestination = Home,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Home> { HomeScreen(vm = viewModel()) }
                    composable<Gallery> { GalleryScreen(vm = viewModel()) }
                    composable<Slideshow> { SlideshowScreen(vm = viewModel()) }
                }
            }
        }
    }
}





