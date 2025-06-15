package com.example.beyondlimits.ui.appNav

import HomeScreen
import ProgressScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.beyondlimits.R
import com.example.beyondlimits.ui.auth.LoginScreen
import com.example.beyondlimits.ui.profile.ProfileScreen
import com.example.beyondlimits.util.DrawerItem
import com.example.beyondlimits.util.Profile
import com.example.beyondlimits.util.Home
import com.example.beyondlimits.util.Login
import com.example.beyondlimits.util.Progress
import kotlinx.coroutines.launch
import androidx.navigation.compose.navigation
import com.example.beyondlimits.ui.auth.AuthViewModel
import com.example.beyondlimits.ui.cycling.CyclingScreen
import com.example.beyondlimits.ui.running.RunningScreen
import com.example.beyondlimits.ui.swimming.SwimmingScreen
import com.example.beyondlimits.ui.theme.ButtonRed
import com.example.beyondlimits.ui.theme.SurfaceDark
import com.example.beyondlimits.ui.theme.TextWhite
import com.example.beyondlimits.ui.theme.TopBarDark
import com.example.beyondlimits.ui.triathlon.TriathlonScreen
import com.example.beyondlimits.util.AuthNav
import com.example.beyondlimits.util.Cycling
import com.example.beyondlimits.util.Main
import com.example.beyondlimits.util.Running
import com.example.beyondlimits.util.Swimming
import com.example.beyondlimits.util.Triathlon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(vm: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerItems = listOf(
        DrawerItem("Home", R.drawable.baseline_home_24, Home),
        DrawerItem("Profile", R.drawable.baseline_account_circle_24, Profile),
        DrawerItem("Progress", R.drawable.outline_trending_up_24, Progress)
    )

    // Observe current backstack route for drawer selection
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(navController = navController, startDestination = Main) {
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
            composable<Home> { HomeScreen(
                vm = viewModel(),
                chosedTraning = { route -> navController.navigate(route) }
            ) }
            composable<Profile> { ProfileScreen(vm = viewModel()) }
            composable<Progress> { ProgressScreen(vm = viewModel()) }
        }
    }
    val navController2 = rememberNavController()

    if (currentRoute in drawerItems.map { it.route::class.qualifiedName }) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = SurfaceDark // Use your barColor as drawer background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(SurfaceDark)
                            .padding(16.dp)
                    ) {
                        // Header Section
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .background(TopBarDark, shape = RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // Profile image placeholder
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray), // Placeholder background
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("R", color = TextWhite, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                                    // Replace with Image() when actual profile image is available
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // User info
                                Column {
                                    Text(text = "Robert", color = TextWhite, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Email: john.mclean@example.com",
                                        color = TextWhite,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }


                        Spacer(Modifier.height(24.dp))

                        // Drawer Items
                        drawerItems.forEach { item ->
                            NavigationDrawerItem(
                                label = { Text(item.label, color = TextWhite) },
                                icon = {
                                    Icon(
                                        painter = painterResource(item.iconRes),
                                        contentDescription = item.label,
                                        tint = TextWhite
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
                                modifier = Modifier
                                    .padding(NavigationDrawerItemDefaults.ItemPadding),
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = ButtonRed.copy(alpha = 0.3f),
                                    unselectedContainerColor = Color.Transparent
                                )
                            )
                        }

                        // Spacer to push Logout button to bottom
                        Spacer(modifier = Modifier.weight(1f))

                        // Logout Button
                        Button(
                            onClick = {
                                vm.logout()
                                navController.navigate(AuthNav) {
                                    popUpTo(Main) { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonRed),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Logout", color = TextWhite)
                        }
                    }
                }

            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(

                        navigationIcon = {
                                IconButton(onClick = {
                                    navController2.popBackStack()
                                    print(currentRoute)
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = TextWhite
                                    )

                            }

                        },
                        title = {

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        append("Go ")
                                        withStyle(style = SpanStyle(color = ButtonRed)) {
                                            append("Beyond")
                                        }
                                        append(" !")
                                    },
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Open Drawer", tint = TextWhite)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = TopBarDark,
                            titleContentColor = TextWhite
                        )
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController2,
                    startDestination = Home,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Home> { HomeScreen(vm = viewModel(), chosedTraning = {route-> navController2.navigate(route)}) }
                    composable<Profile> { ProfileScreen(vm = viewModel()) }
                    composable<Progress> { ProgressScreen(vm = viewModel()) }
                    composable<Running> { RunningScreen(vm = viewModel()) }
                    composable<Cycling> { CyclingScreen(vm = viewModel()) }
                    composable<Swimming> { SwimmingScreen(vm = viewModel()) }
                    composable<Triathlon> { TriathlonScreen(vm = viewModel()) }
                }
            }
        }
    }
}





