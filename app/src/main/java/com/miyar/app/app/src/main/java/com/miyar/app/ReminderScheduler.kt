package com.miyar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.miyar.app.ui.theme.MiyarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ReminderScheduler.scheduleDailyReminders(this)
        
        setContent {
            MiyarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppScaffold()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مِعيار - إدارة المواضع والماليات", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1B5E20)) // أخضر غامق فاخر
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFE8F5E9)) {
                NavigationBarItem(
                    icon = { Text("🏠") },
                    label = { Text("الرئيسية") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Text("📋") },
                    label = { Text("العمليات") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Text("💰") },
                    label = { Text("الماليات") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Text("⚙️") },
                    label = { Text("المزيد") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (selectedTab) {
                0 -> HomeScreenModern()
                1 -> FinanceScreensPlaceholder("إدارة العمليات والأنشطة")
                2 -> FinanceScreensPlaceholder("التقارير والماليات الشاملة")
                3 -> FinanceScreensPlaceholder("الإعدادات ومعلومات التطبيق")
            }
        }
    }
}

@Composable
fun HomeScreenModern() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "مرحباً بك في ميار 🌿", style = MaterialTheme.colorScheme.headlineMedium, color = Color(0xFF1B5E20))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "شريكك الذكي في إدارة مواضعك الزراعية والحسابات بدقة متناهية.", color = Color.Gray)
    }
}

@Composable
fun FinanceScreensPlaceholder(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, style = MaterialTheme.colorScheme.titleLarge)
    }
}
