package com.miyar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miyar.app.ui.theme.MiyarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // تفعيل عقل التنبيهات الذكي (الساعة 3 عصراً والساعة 8 مساءً)
        ReminderScheduler.scheduleDailyReminders(this)
        
        setContent {
            MiyarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF4F6F4) // خلفية مريحة للعين في الحقل
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
                title = { 
                    Column {
                        Text("معيار", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("إدارة أدق ومردود أوضح", color = Color(0xFFA5D6A7), fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1B5E20))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Text("🏠", fontSize = 20.sp) },
                    label = { Text("الرئيسية") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Text("⚙️", fontSize = 20.sp) },
                    label = { Text("العمليات") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Text("💰", fontSize = 20.sp) },
                    label = { Text("الماليات") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Text("📊", fontSize = 20.sp) },
                    label = { Text("التقارير") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (selectedTab) {
                0 -> ModernHomeScreenDesign()
                1 -> HarvestScreensContent()
                2 -> FinanceScreensContent()
                3 -> ReportsScreenContent()
            }
        }
    }
}

@Composable
fun ModernHomeScreenDesign() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // بطاقة ملخص الأداء المالي والمواضع
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("إجمالي المردود", color = Color(0xFFA5D6A7), fontSize = 14.sp)
                    Text("2,850,000 ر.ي", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }
                Column {
                    Text("إجمالي خرج المواضع", color = Color(0xFFFFCDD2), fontSize = 14.sp)
                    Text("250,000 ر.ي", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // الأزرار السريعة لإضافة المواضع والعمليات
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { /* فتح إضافة موضع */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("🌱 إضافة موضع", color = Color.White)
            }
            Button(
                onClick = { /* فتح إضافة عملية */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C))
            ) {
                Text("📝 إضافة عملية", color = Color.White)
            }
        }
    }
}

@Composable
fun HarvestScreensContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("شاشة إدارة المواضع والأنشطة الزراعية (السموم، الري، القطفات)", color = Color(0xFF1B5E20))
    }
}

@Composable
fun FinanceScreensContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("شاشة الماليات، الإيرادات والمبيعات، والتجار والموردين", color = Color(0xFF1B5E20))
    }
}

@Composable
fun ReportsScreenContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("شاشة التقارير والإحصائيات والنسخ الاحتياطي", color = Color(0xFF1B5E20))
    }
}
