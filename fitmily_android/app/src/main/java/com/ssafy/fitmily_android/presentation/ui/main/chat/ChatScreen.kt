package com.ssafy.fitmily_android.presentation.ui.main.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.components.EmptyContentText
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatBottomBar
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatContentField
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatGalleryBar
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatTopBar

@Composable
fun ChatScreen(
    navController: NavHostController
) {
    BackHandler {
        navController.navigate("home/main") {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    var showGalleryBar by remember { mutableStateOf(false) }
    // TODO: familyCount API로 받자
    val familyCount = 4

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        , containerColor = Color(0xFFD2E1FF)
        , topBar = {
            ChatTopBar(navController, "우리가족족족족족", familyCount)
        }
    ) { innerPadding ->
        if (familyCount != -1) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
            ) {
                ChatContentField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                if (showGalleryBar) {
                    ChatGalleryBar(
                        modifier = Modifier
                        , onGalleryClose = { showGalleryBar = false }
                    )
                } else {
                    ChatBottomBar(
                        modifier = Modifier
                        , onGalleryOpen = { showGalleryBar = true }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                , contentAlignment = Alignment.Center
            ) {
                EmptyContentText(
                    "받은 알림이 없어요", "알림을 켜고 소식을 받아보세요"
                )
            }
        }
    }
}