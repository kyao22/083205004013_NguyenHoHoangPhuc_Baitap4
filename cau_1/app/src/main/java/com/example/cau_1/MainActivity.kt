package com.example.cau_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

val poppinsMediumFont = FontFamily(Font(R.font.poppins_medium))
val poppinsRegularFont = FontFamily(Font(R.font.poppins_regular))
val robotoBold = FontFamily(Font(R.font.roboto_bold))
val robotoSemiBold = FontFamily(Font(R.font.roboto_semi_bold))
val robotoRegular = FontFamily(Font(R.font.roboto_regular))

@Preview(showBackground = true)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Started") {
        composable("Started") { Started(navController) }
        composable("List") { List(navController) }
        composable("Detail/{message}") { backStackEntry ->
            val mess = backStackEntry.arguments?.getString("message") ?: "No Data"
            Detail(mess, navController)
        }
    }
}

@Composable
fun Started(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().scale(1.25f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.jetpack_compose),
            contentDescription = "Jetpack_compose",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(216.dp, 233.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Navigation",
            fontFamily = poppinsMediumFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "is a framework that simplifies the implementation of navigation between different UI components (activities, fragments, or composables) in an app",
            fontFamily = poppinsRegularFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 19.6.sp,
            color = Color(0xFF4A4646),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Button(
            onClick = { navController.navigate("List")},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).height(48.dp)
        ) {
            Text(
                text = "PUSH",
                fontFamily = robotoBold,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 25.sp
            )
        }
    }
}


@Composable
fun List(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("Started") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                )

            ) {
                Image(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "Chevron_left",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(40.dp, 40.dp)
                )
            }

            Text(
                text = "Lazy Column",
                fontFamily = robotoSemiBold,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 37.5.sp,
                color = Color(0xFF2196F3),
                modifier = Modifier.align(Alignment.TopCenter).padding(9.dp)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .padding(4.dp),

            ) {
            items(999999) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .size(350.dp, 113.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2196F3).copy(alpha = 0.3f)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    var text by remember { mutableStateOf("The only way to do great work is to love what you do.") }

                    if(index < 9) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(0.75f)
                                .clip(RoundedCornerShape(16.dp))
                                .align(Alignment.CenterVertically),
                            value = text,
                            onValueChange = { text = it },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            label = {
                                Text(
                                    text = "0${index+1}",
                                    fontFamily = robotoRegular,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 22.5.sp,
                                )
                            }
                        )
                    }
                    else {
                        TextField(
                            modifier = Modifier.fillMaxWidth(0.75f)
                                .clip(RoundedCornerShape(16.dp))
                                .align(Alignment.CenterVertically),
                            value = text,
                            onValueChange = { text = it },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            label = {
                                Text(
                                    text = "${index+1}",
                                    fontFamily = robotoRegular,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 22.5.sp,
                                )
                            }
                        )
                    }

                    Button(
                        onClick = { navController.navigate("Detail/$text") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.chevron_right),
                            contentDescription = "Chevron_right",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(38.dp, 38.dp).align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Detail(mess: String, navController: NavHostController){
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("List") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "Chevron_left",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(40.dp, 40.dp)
                )
            }

            Text(
                text = "Detail",
                fontFamily = robotoSemiBold,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 37.5.sp,
                color = Color(0xFF2196F3),
                modifier = Modifier.align(Alignment.TopCenter).padding(9.dp)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "“The only way to do great work \n" +
                    "is to love what you do”",
            fontFamily = robotoRegular,
            fontSize = 18.sp,
            lineHeight = 22.5.sp,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
                text = mess,
                fontFamily = robotoBold,
                fontSize = 45.sp,
                lineHeight = 60.sp,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                modifier = Modifier.background(Color(0xFF2196F3).copy(alpha = 0.3f))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = { navController.navigate("Started")},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).height(48.dp)
                ) {
                    Text(
                        text = "BACK TO ROOT",
                        fontFamily = robotoBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 25.sp
                    )
                }
            }
        }
    }
