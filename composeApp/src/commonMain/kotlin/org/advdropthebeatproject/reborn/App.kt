package org.advdropthebeatproject.reborn

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.*

val reborn_red = Color(0xFFB40404)

enum class Screen {
    Start, Login, Home, Mypage, Upload, Select, Loading, Result, Question, Laundry
}

val RobotoFontFamily
    @Composable get() = FontFamily(
        Font(Res.font.Roboto_Bold, weight = FontWeight.Bold),
        Font(Res.font.Roboto_Light, weight = FontWeight.Light)
    )

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Start) }
    when (currentScreen) {
        Screen.Start -> StartScreen(
            onStartClick = { currentScreen = Screen.Login }
        )
        Screen.Login -> LoginScreen(
            onLoginClick = { currentScreen = Screen.Home },
            onBackClick = { currentScreen = Screen.Start }
        )
        Screen.Home -> HomeScreen(
            onHomeClick = { currentScreen = Screen.Home },
            onUploadClick = { currentScreen = Screen.Upload },
            onMyPageClick = { currentScreen = Screen.Mypage },
            onLaundryClick = { currentScreen = Screen.Laundry }
        )
        Screen.Mypage -> MypageScreen(
            onHomeClick = { currentScreen = Screen.Home },
            onUploadClick = { currentScreen = Screen.Upload },
            onMyPageClick = { currentScreen = Screen.Mypage },
            onLaundryClick = { currentScreen = Screen.Laundry }
        )
        Screen.Upload -> UploadScreen(
            onHomeClick = { currentScreen = Screen.Home },
            onUploadClick = { currentScreen = Screen.Upload },
            onMyPageClick = { currentScreen = Screen.Mypage },
            onLaundryClick = { currentScreen = Screen.Laundry },
            onContinueClick = { currentScreen = Screen.Select}
        )
        Screen.Select -> SelectScreen(
            onContinueClick = { currentScreen = Screen.Question },
            onHomeClick = { currentScreen = Screen.Home },
            onUploadClick = { currentScreen = Screen.Upload },
            onMyPageClick = { currentScreen = Screen.Mypage },
            onLaundryClick = { currentScreen = Screen.Laundry }
        )
        Screen.Loading -> LoadingScreen(
            onLoadingFinished = { currentScreen = Screen.Result }
        )
        Screen.Result -> ResultScreen(
            onSaveClick = { currentScreen = Screen.Home },
            onAgainClick = { currentScreen = Screen.Upload },
            onLaundryClick = { currentScreen = Screen.Laundry },
            onHomeClick = { currentScreen = Screen.Home },
            onMyPageClick = { currentScreen = Screen.Mypage },
            onUploadClick = { currentScreen = Screen.Upload },
            currentTab = String()
        )
        Screen.Question -> QuestionScreen(
            onContinueClick = { currentScreen = Screen.Loading },
            onSkipClick = { currentScreen = Screen.Loading },
            onHomeClick = { currentScreen = Screen.Home },
            onUploadClick = { currentScreen = Screen.Upload },
            onMyPageClick = { currentScreen = Screen.Mypage },
            onLaundryClick = { currentScreen = Screen.Laundry }
        )
        Screen.Laundry -> LaundryScreen(
            onHomeClick = { currentScreen = Screen.Home },
            onUploadClick = { currentScreen = Screen.Upload },
            onMyPageClick = { currentScreen = Screen.Mypage },
            onLaundryClick = { currentScreen = Screen.Laundry }
        )
    }
}

@Composable
fun StartScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Reborn",
                fontFamily = RobotoFontFamily,
                color = reborn_red,
                fontSize = 40.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 100.dp)
            )

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp)
            ) {
                Text(
                    text = "start",
                    fontFamily = RobotoFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginClick: () -> Unit, onBackClick: () -> Unit) {
    var id by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Login",
                fontFamily = RobotoFontFamily,
                color = reborn_red,
                fontSize = 40.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                placeholder = { Text("ID") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = reborn_red,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = reborn_red,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = "Log in",
                    fontFamily = RobotoFontFamily ,
                    color = Color.White,
                    fontWeight = FontWeight.Light
                )
            }

            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = "back",
                    fontFamily = RobotoFontFamily,
                    color = Color.White,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    onHomeClick: () -> Unit,
    onUploadClick: () -> Unit,
    onMyPageClick: () -> Unit,
    onLaundryClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding() // 내비게이션 바 높이만큼 여유 공간 추가
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onHomeClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_home_on_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Home",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onUploadClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_reborn_icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Reborn",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onMyPageClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_mypage_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "My Page",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onLaundryClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_laundry_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Laundry",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.background_image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp))

                Text(
                    text = "Reborn",
                    fontFamily = RobotoFontFamily,
                    color = reborn_red,
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "안 입고 옷장에만 쌓이는옷들\n이제는 새롭게 태어날 시간\nReborn으로 옷에게 새 삶을",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                Column {
                    Button(
                        onClick = onUploadClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column {
                            Image(
                                painter = painterResource(resource = Res.drawable.reborn_home_icon),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp)
                            )
                            Text(
                                text = "Reborn >",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(70.dp))

                    Button(
                        onClick = onMyPageClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column {
                            Image(
                                painter = painterResource(resource = Res.drawable.mypage_home_icon),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp)
                            )
                            Text(
                                text = "MY page >",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(70.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun MypageScreen(
    onHomeClick: () -> Unit,
    onUploadClick: () -> Unit,
    onMyPageClick: () -> Unit,
    onLaundryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MY Reborn",
                    fontFamily = RobotoFontFamily,
                    color = reborn_red,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "CNU 님",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "최근",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (i in 1..2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            for (j in 1..2) {
                                if (i == 1 && j == 1) {
                                    Image(
                                        painter = painterResource(resource = Res.drawable.image1),
                                        contentDescription = "Reborn Image",
                                        modifier = Modifier
                                            .size(150.dp)
                                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                            .padding(8.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Gray)
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding() // 내비게이션 바 높이만큼 여유 공간 추가
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onHomeClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_home_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Home",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onUploadClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_reborn_icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Reborn",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onMyPageClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_mypage_on_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "My Page",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onLaundryClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_laundry_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Laundry",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun UploadScreen(
    onHomeClick: () -> Unit,
    onUploadClick: () -> Unit,
    onMyPageClick: () -> Unit,
    onLaundryClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalPlatformContext.current

    var byteArray by remember { mutableStateOf(ByteArray(0)) }

    val pickerLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files ->
            scope.launch {
                files.firstOrNull()?.let {
                    byteArray = it.readByteArray(context)
                }
            }
        }
    )

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding() // 내비게이션 바 높이만큼 여유 공간 추가
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onHomeClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_home_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Home",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onUploadClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_reborn_on_icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Reborn",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onMyPageClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_mypage_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "My Page",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onLaundryClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_laundry_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Laundry",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.background_image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "Reborn",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Light,
                    color = reborn_red
                )
                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.1f))
                        .clickable {
                            pickerLauncher.launch()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (byteArray.isNotEmpty()) {
                        AsyncImage(
                            model = byteArray,
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Upload",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = { byteArray = ByteArray(0) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp)
                    ) {
                        Text(text = "again", color = Color.Black)
                    }

                    Button(
                        onClick = onContinueClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = reborn_red),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp)
                    ) {
                        Text(text = "continue", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SelectScreen(
    onHomeClick: () -> Unit,
    onUploadClick: () -> Unit,
    onMyPageClick: () -> Unit,
    onLaundryClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    var selectedItems by remember { mutableStateOf(setOf<String>()) }

    val items = listOf(
        "반팔", "긴팔", "민소매", "아우터",
        "치마", "반바지", "긴바지", "신발",
        "원피스", "점프수트", "목도리", "모자",
        "장갑", "가방", "애견용", "유아용"
    )
    val itemIcons = listOf(
        Res.drawable.icon_shirt, Res.drawable.icon_long_shirt, Res.drawable.icon_tank_top, Res.drawable.icon_outerwear,
        Res.drawable.icon_skirt, Res.drawable.icon_shorts, Res.drawable.icon_pants, Res.drawable.icon_shoes,
        Res.drawable.icon_dress, Res.drawable.icon_jumpsuit, Res.drawable.icon_scarf, Res.drawable.icon_hat,
        Res.drawable.icon_gloves, Res.drawable.icon_bag, Res.drawable.icon_pet, Res.drawable.icon_baby
    )

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding() // 내비게이션 바 높이만큼 여유 공간 추가
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onHomeClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_home_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Home",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onUploadClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_reborn_on_icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Reborn",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onMyPageClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_mypage_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "My Page",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onLaundryClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_laundry_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Laundry",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Reborn",
                fontFamily = RobotoFontFamily,
                color = reborn_red,
                fontSize = 40.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "-------- 리폼할 카테고리를 선택해주세요 --------",
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                //modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(-20.dp),  // Column 간 가로 간격
                verticalArrangement = Arrangement.spacedBy(15.dp),    // Row 간 세로 간격
                contentPadding = PaddingValues(horizontal = 5.dp, vertical = 30.dp)
            ) {
                items(items.size) { index ->
                    val item = items[index]
                    val isSelected = selectedItems.contains(item)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                selectedItems = if (isSelected) {
                                    selectedItems.toMutableSet().apply { remove(item) }
                                } else {
                                    selectedItems.toMutableSet().apply { add(item) }
                                }
                            }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = Color.White,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) reborn_red else Color.Gray,
                                    shape = CircleShape
                                )
                        ) {
                            Image(
                                painter = painterResource(resource = itemIcons[index]),
                                contentDescription = item,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item,
                            color = if (isSelected) reborn_red else Color.Black,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Skip action */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(1.dp, reborn_red),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Skip", color = reborn_red)
                }

                Button(
                    onClick = onContinueClick,
                    enabled = selectedItems.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedItems.isNotEmpty()) reborn_red else Color.LightGray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun QuestionScreen(
    onContinueClick: () -> Unit,
    onSkipClick: () -> Unit,
    onHomeClick: () -> Unit,
    onUploadClick: () -> Unit,
    onMyPageClick: () -> Unit,
    onLaundryClick: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.White,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onHomeClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_home_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "Home",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Button(
                    onClick = onUploadClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_reborn_on_icon),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Reborn",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Button(
                    onClick = onMyPageClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_mypage_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "My Page",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Button(
                    onClick = onLaundryClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_laundry_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "Laundry",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Title Section
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = "Reborn",
                fontFamily = RobotoFontFamily,
                color = reborn_red,
                fontSize = 60.sp,
                fontWeight = FontWeight.Light
            )

            // Input Section
            Spacer(modifier = Modifier.height(200.dp))
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("원하는 스타일을 입력해주세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    textColor = Color.Black,
                    cursorColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )
            Text(
                text = "예) 캐주얼, 심플, 스포티 등",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Action Buttons
            Spacer(modifier = Modifier.height(100.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Button(
                    onClick = onSkipClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                ) {
                    Text(text = "skip", color = Color.Black)
                }

                Button(
                    onClick = onContinueClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = reborn_red),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                ) {
                    Text(text = "continue", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(onLoadingFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onLoadingFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Reborn - ing",
                fontFamily = RobotoFontFamily,
                color = reborn_red,
                fontSize = 32.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = Color.Gray)
        }
    }
}
@Composable
fun ResultScreen(
    onSaveClick: () -> Unit,
    onAgainClick: () -> Unit,
    onHomeClick: () -> Unit,
    onUploadClick: () -> Unit,
    onMyPageClick: () -> Unit,
    onLaundryClick: () -> Unit,
    currentTab: String

) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.background_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    Row {
                        Text(
                            text = "Reborn",
                            color = reborn_red,
                            fontFamily = RobotoFontFamily,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Light
                        )

                        Spacer(modifier = Modifier.height(60.dp))

                        Text(
                            text = "되었습니다",
                            color = reborn_red,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Image(
                        painter = painterResource(resource = Res.drawable.image1),
                        contentDescription = "Reborn Image",
                        modifier = Modifier
                            .size(300.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Button(
                            onClick = onAgainClick,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .width(120.dp)
                                .height(40.dp)
                        ) {
                            Text(text = "again", color = Color.Black)
                        }

                        Button(
                            onClick = { showDialog = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = reborn_red),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .width(120.dp)
                                .height(40.dp)
                        ) {
                            Text(text = "save", color = Color.White)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onHomeClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_home_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "Home",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Button(
                    onClick = onUploadClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_reborn_on_icon),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Reborn",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Button(
                    onClick = onMyPageClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_mypage_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "My Page",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Button(
                    onClick = onLaundryClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(resource = Res.drawable.bottom_laundry_icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "Laundry",
                            fontFamily = RobotoFontFamily,
                            color = Color.Black,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("저장하시겠습니까?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onSaveClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("네")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("아니요")
                }
            }
        )
    }
}


@Composable
fun LaundryScreen(
    onHomeClick: () -> Unit,
    onUploadClick: () -> Unit,
    onMyPageClick: () -> Unit,
    onLaundryClick: () -> Unit
) {
    val reformCompanies = listOf(
        Triple("숨고 Soomgo", "맞춤옷제작, 여성원피스, 남성정장,\n데일리룩 등 내 몸에 딱 맞춘 옷제작 사이트", "https://www.soomgo.com"),
        Triple("에프앤드에이 F&A", "빠르고 친절한 서비스와 좋은 품질로\n고객의 생활을 더 윤택하게 만드는\n대한민국 수선 리폼 전문기업", "http://fna-reform.co.kr/"),
        Triple("아다모스튜디오 ADAMO STUDIO", "지속적 가치를 위하여 옷을 만들고\n고치는 작업공간으로서 고객분들의\n의류를 전문적으로 수선하고\n옷의 제조방법을 연구", "https://www.adamostudio.co.kr/"),
        Triple("나인포리즘 NINEFOURISM", "핸드메이드 상품을 판매, 위주수선서비스", "https://ninefourism.com/")
    )

    Scaffold(
        topBar = {
            Text(
                text = "온라인 리폼 업체",
                fontFamily = RobotoFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = reborn_red,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding() // 내비게이션 바 높이만큼 여유 공간 추가
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onHomeClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_home_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Home",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onUploadClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_reborn_icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Reborn",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onMyPageClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_mypage_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "My Page",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onLaundryClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.bottom_laundry_on_icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Laundry",
                                fontFamily = RobotoFontFamily,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            reformCompanies.forEach { (title, description, url) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = Color(0xFFF5F5F5)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = title,
                                fontFamily = RobotoFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = description,
                                fontFamily = RobotoFontFamily,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        val uriHandler = LocalUriHandler.current
                        Button(
                            onClick = { uriHandler.openUri(url) },
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF5F5F5)),
                            modifier = Modifier
                                .height(50.dp)
                                .width(50.dp),
                            elevation = ButtonDefaults.elevation(0.dp) // 그림자를 제거

                        ) {
                            Icon(
                                painter = painterResource(resource = Res.drawable.icon_arrow),
                                contentDescription = "Arrow Icon",
                                tint = Color.Black,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}