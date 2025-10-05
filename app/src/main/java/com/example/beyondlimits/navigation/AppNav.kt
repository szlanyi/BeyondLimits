package com.example.beyondlimits.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.beyondlimits.R
import com.example.beyondlimits.feature_auth.AuthViewModel
import com.example.beyondlimits.feature_auth.LoginScreen
import com.example.beyondlimits.feature_home.HomeScreen
import com.example.beyondlimits.feature_profile.ProfileScreen
import com.example.beyondlimits.feature_splash.SplashScreen
import com.example.beyondlimits.feature_traning.common.ProgressScreen
import com.example.beyondlimits.feature_traning.cycling.CyclingScreen
import com.example.beyondlimits.feature_traning.running.RunningScreen
import com.example.beyondlimits.feature_traning.swimming.SwimmingScreen
import com.example.beyondlimits.feature_traning.triathlon.TriathlonScreen
import com.example.beyondlimits.ui.theme.*
import com.example.beyondlimits.util.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(vm: AuthViewModel = viewModel()) {
    val parentNavController = rememberNavController()

    NavHost(
        navController = parentNavController,
        startDestination = Splash
    ) {
        // ---------- SPLASH ----------
        composable<Splash> {
            SplashScreen {
                if (vm.currentUser == null) {
                    parentNavController.navigate(AuthNav) {
                        popUpTo(Splash) { inclusive = true }
                    }
                } else {
                    parentNavController.navigate(Main) {
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            }
        }

        // ---------- AUTH FLOW ----------
        navigation<AuthNav>(startDestination = Login) {
            composable<Login> {
                LoginScreen(
                    viewModel = vm,
                    onAuthSuccess = {
                        parentNavController.navigate(Main) {
                            popUpTo(AuthNav) { inclusive = true }
                        }
                    }
                )
            }
        }

        // ---------- MAIN APP ----------
        navigation<Main>(startDestination = Home) {
            composable<Home> {
                MainDrawerScreen(
                    vm = vm,
                    parentNavController = parentNavController
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDrawerScreen(vm: AuthViewModel, parentNavController: NavController) {
    val mainNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerItems = listOf(
        DrawerItem("Home", R.drawable.baseline_home_24, Home),
        DrawerItem("Profile", R.drawable.baseline_account_circle_24, Profile),
        DrawerItem("Progress", R.drawable.outline_trending_up_24, Progress)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SurfaceDark
            ) {
                DrawerContent(
                    vm = vm,
                    drawerItems = drawerItems,
                    mainNavController = mainNavController,
                    drawerState = drawerState,
                    scope = scope,
                    parentNavController = parentNavController
                )
            }
        }
    ) {
        val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .width(56.dp) // ← standard IconButton width
                                .height(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentRoute != Home::class.qualifiedName) {
                                IconButton(onClick = { mainNavController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = TextWhite
                                    )
                                }
                            }
                        }
                    },
                    title = {
                        Text(
                            buildAnnotatedString {
                                append("Go ")
                                withStyle(style = SpanStyle(color = ButtonRed)) { append("Beyond") }
                                append(" !")
                            },
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center // ✅ true center alignment
                        )
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Open Drawer",
                                    tint = TextWhite
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TopBarDark,
                        titleContentColor = TextWhite
                    )
                )

            },
            containerColor = BackgroundDark
        ) { innerPadding ->
            // ---------- INNER NAV HOST ----------
            NavHost(
                navController = mainNavController,
                startDestination = Home,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Home> {
                    HomeScreen(
                        vm = viewModel(),
                        chosenTraining = { route ->
                            mainNavController.navigate(route)
                        }
                    )
                }
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

@Composable
private fun DrawerContent(
    vm: AuthViewModel,
    drawerItems: List<DrawerItem>,
    mainNavController: NavController,
    drawerState: DrawerState,
    parentNavController: NavController,
    scope: kotlinx.coroutines.CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(SurfaceDark)
            .padding(16.dp)
    ) {
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
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "R",
                        color = TextWhite,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = "Robert", color = TextWhite, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "john.mclean@example.com",
                        color = TextWhite,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

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
                selected = mainNavController.currentBackStackEntryAsState().value?.destination?.route ==
                        item.route::class.qualifiedName,
                onClick = {
                    mainNavController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo(Home)
                    }
                    scope.launch { drawerState.close() }
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = ButtonRed.copy(alpha = 0.3f),
                    unselectedContainerColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                vm.logout()
                parentNavController.navigate(AuthNav) {
                    popUpTo(Main) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonRed),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Logout", color = TextWhite, fontWeight = FontWeight.Bold)
        }
    }
}
