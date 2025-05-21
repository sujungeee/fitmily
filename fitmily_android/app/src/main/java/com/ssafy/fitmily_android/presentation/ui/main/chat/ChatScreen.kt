package com.ssafy.fitmily_android.presentation.ui.main.chat

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.presentation.ui.MainActivity
import com.ssafy.fitmily_android.presentation.ui.components.EmptyContentText
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatBottomBar
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatContentField
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatGalleryBar
import com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatTopBar
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType

private const val TAG = "ChatScreen_fitmily"
@Composable
fun ChatScreen(
    navController: NavHostController
    , chatViewModel: ChatViewModel = hiltViewModel()
) {
    // 키보드 설정
    val context = LocalContext.current
    val activity = context as? Activity
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    val uiState by chatViewModel.chatUiState.collectAsState()
    val images = remember { mutableStateListOf<Uri>() }

    LaunchedEffect(Unit) {
        val authDataStore = MainApplication.getInstance().getDataStore()
        val familyId = authDataStore.getFamilyId()
        if (familyId != 0) {
            chatViewModel.getFamilyInfo(familyId)
            chatViewModel.initStomp()
        }
    }

    BackHandler {
        navController.navigate("home/main") {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    var showGalleryBar by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        , containerColor = Color(0xFFD2E1FF)
        , topBar = {
            ChatTopBar(navController, uiState.familyName, uiState.familyCount)
        }
    ) { innerPadding ->
        if (uiState.familyCount != -1) {
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
                        .imePadding()
                )

                if (showGalleryBar && images.isNotEmpty()) {
                    ChatGalleryBar(
                        modifier = Modifier
                        , onGalleryClose = {
                            showGalleryBar = false
                        }
                        , images = images
                    )
                } else {
                    ChatBottomBar(
                        modifier = Modifier
                        , onGalleryOpen = {
                            galleryOpen(context) { selectedUris ->
                                images.clear()
                                images.addAll(selectedUris)
                                if (images.size > 0) {
                                    showGalleryBar = true
                                }
                            }
                        }
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
                    "가족에 참여하지 않았어요", "가족에 참여한 후 채팅을 이용해보세요"
                )
            }
        }
    }
}

// 갤러리 열기
private fun galleryOpen(context: Context, onImagesSelected: (List<Uri>) -> Unit) {
    if (isPermissionGranted(Manifest.permission.CAMERA, context)) {
        selectImages(context, onImagesSelected)
    } else {
        ActivityCompat.requestPermissions(
            context as MainActivity,
            arrayOf(Manifest.permission.CAMERA),
            1
        )
    }
}

private fun selectImages(context: Context, onImagesSelected: (List<Uri>) -> Unit) {
    TedImagePicker.with(context)
        .max(9, "최대 9장까지 선택이 가능합니다.")
        .mediaType(MediaType.IMAGE)
        .startMultiImage { uris ->
            val filteredUris = uris.filter { uri ->
                val mimeType = context.contentResolver.getType(uri)
                mimeType == "image/jpeg" || mimeType == "image/png"
            }
            Toast.makeText(context, "jpeg, png 이외의 확장자는 제외됩니다.", Toast.LENGTH_SHORT).show()
            onImagesSelected(filteredUris)
        }
}

fun isPermissionGranted(permission: String, context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}