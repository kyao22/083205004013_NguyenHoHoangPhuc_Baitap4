package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val poppinsMediumFont = FontFamily(Font(R.font.poppins_medium))
val poppinsRegularFont = FontFamily(Font(R.font.poppins_regular))
val robotoBold = FontFamily(Font(R.font.roboto_bold))
val robotoSemiBold = FontFamily(Font(R.font.roboto_semi_bold))
val robotoRegular = FontFamily(Font(R.font.roboto_regular))


@Serializable
data class ApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<Task>
)

@Serializable
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val priority: String,
    val category: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
    val subtasks: List<Subtask>,
    val attachments: List<Attachment>,
    val reminders: List<Reminder>
)

@Serializable
data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

@Serializable
data class Attachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String
)

@Serializable
data class Reminder(
    val id: Int,
    val time: String,
    val type: String
)

class TaskViewModel : ViewModel() {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    var tasks by mutableStateOf<List<Task>>(emptyList())
        private set
    var selectedtask by mutableStateOf<Task?>(null)
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response: ApiResponse = client.get("https://amock.io/api/researchUTH/tasks").body()
                if (response.isSuccess) {
                    tasks = response.data
                    error = null
                } else {
                    error = response.message
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    override fun onCleared() {
        client.close()
        super.onCleared()
    }
}


@Composable
fun TaskItem(task: Task, navController: NavHostController, viewModel: TaskViewModel) {
    var isChecked by remember { mutableStateOf(false) }
    Button(
        onClick = {viewModel.selectedtask = task
            navController.navigate("Detail")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
        when (task.status) {
            "Completed" -> Color(0xFFC9E311)
            "In Progress" -> Color(0xFFB7E9FF)
            else -> Color(0xFFE1BBC1)
            }
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row (verticalAlignment = Alignment.CenterVertically){
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )

                Column {
                    Text(
                        text = task.title,
                        fontFamily = robotoBold,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = task.description,
                        fontFamily = robotoRegular,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF333333)
                    )
                }
            }
            Spacer(modifier = Modifier.height(9.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Status: ${task.status}",
                    fontFamily = robotoBold,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = Color(0xFF333333)
                )

                Text(
                    text = "Due: ${task.dueDate}",
                    fontFamily = robotoRegular,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = Color(0xFF333333),
                )
            }

        }
    }
}

@Composable
fun Subtask(subtask: Subtask){
    var isChecked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFE6E6E6), shape = RoundedCornerShape(9.dp)),
        verticalAlignment = Alignment.CenterVertically
    ){
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )

        Text(text = subtask.title,
            fontFamily = robotoRegular,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = Color(0xFF333333)
        )
    }
}

@Composable
fun Attachments(attachment: Attachment){
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 6.dp)
            .background(Color(0xFFE6E6E6), shape = RoundedCornerShape(9.dp)),
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(modifier = Modifier.width(12.dp))

        Image(
            painter = painterResource(id = R.drawable.attachments),
            contentDescription = "attachments",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(28.dp, 28.dp)

        )

        Text(text = attachment.fileName,
            fontFamily = robotoRegular,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(12.dp),
            verticalAlignment = Alignment.Top
            ) {
            Image(
                painter = painterResource(id = R.drawable.logo_uth),
                contentDescription = "logo_uth",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(52.dp, 36.dp)
                    .scale(1.25f)
                    .align(Alignment.CenterVertically)
            )
            Column {
                Text(
                    text = "SmartTasks",
                    fontFamily = robotoSemiBold,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "A simple and efficient to-do app",
                    fontFamily = robotoRegular,
                    fontSize = 12.sp,
                    lineHeight = 9.sp,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.padding(horizontal = 16.dp).padding(end = 40.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "bell",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(27.dp, 27.dp)
                    .scale(1.25f)
                    .align(Alignment.CenterVertically)
            )
            Box{
                Image(
                    painter = painterResource(id = R.drawable.element),
                    contentDescription = "element",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            viewModel.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            viewModel.error != null -> {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding (24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Image(
                        painter = painterResource(id = R.drawable.error_image),
                        contentDescription = "error_image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(350.dp, 255.dp)
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(viewModel.tasks) { task ->
                        TaskItem(task, navController, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailTask(navController: NavHostController, task: Task){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.chevron_left),
                contentDescription = "Chevron_left",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp, 40.dp)
                    .clickable{
                        navController.navigate(BottomNavItem.Task.route){
                            popUpTo(0) { inclusive = true } }
                    }
                )


            Text(
                text = "Detail",
                fontFamily = robotoSemiBold,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 37.5.sp,
                color = Color(0xFF2196F3),
            )

            Image(
                painter = painterResource(id = R.drawable.bin),
                contentDescription = "bin",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp, 40.dp)
            )
        }

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = task.title,
            fontFamily = robotoBold,
            fontSize = 22.sp,
            lineHeight = 26.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = task.description,
            fontFamily = robotoRegular,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.125f)
                .background(Color(0xFFE1BBC1), shape = RoundedCornerShape(9.dp))
                .padding(8.dp).padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row()
            {
                Image(
                    painter = painterResource(id = R.drawable.category),
                    contentDescription = "category",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(28.dp, 28.dp)
                )

                Column {
                    Text(
                        text = "Category",
                        fontFamily = robotoRegular,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                    )

                    Text(
                        text = task.category,
                        fontFamily = robotoBold,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                    )
                }
            }

            Row()
            {
                Image(
                    painter = painterResource(id = R.drawable.status),
                    contentDescription = "status",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(28.dp, 28.dp)
                )

                Column(){
                    Text(
                        text = "Status",
                        fontFamily = robotoRegular,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = task.status,
                        fontFamily = robotoBold,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Row()
            {
                Image(
                    painter = painterResource(id = R.drawable.priority),
                    contentDescription = "priority",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(28.dp, 28.dp)
                )

                Column(){
                    Text(
                        text = "Priority",
                        fontFamily = robotoRegular,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = task.priority,
                        fontFamily = robotoBold,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Subtasks",
            fontFamily = robotoBold,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(task.subtasks) { subtask ->
                Subtask(subtask)
            }
        }

        Text(
            text = "Attachments",
            fontFamily = robotoBold,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(task.attachments) { attachment ->
                Attachments(attachment)
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val iconFilled: ImageVector, val iconOutlined: ImageVector) {
    object Task : BottomNavItem("task", Icons.Filled.Home, Icons.Outlined.Home)
    object Calendar : BottomNavItem("calendar", Icons.Filled.CalendarToday, Icons.Outlined.CalendarToday)
    object Document : BottomNavItem("document", Icons.AutoMirrored.Filled.InsertDriveFile, Icons.AutoMirrored.Outlined.InsertDriveFile)
    object Settings : BottomNavItem("settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Task,
        BottomNavItem.Calendar,
        BottomNavItem.Document,
        BottomNavItem.Settings
    )

    BottomAppBar(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            IconButton(
                onClick = { navController.navigate(item.route) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (isSelected) item.iconFilled else item.iconOutlined,
                    contentDescription = null,
                    tint = if (isSelected) Color.Blue else Color.Gray
                )
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val hideBottomNavScreens = listOf("Details")
    val viewModel = TaskViewModel()
    var currentRoute by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute !in hideBottomNavScreens) {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Xử lý khi nhấn nút */ },
                shape = CircleShape,
                modifier = Modifier
                    .offset(y = 30.dp),
                containerColor = Color.Blue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        val modifier = if (currentRoute in hideBottomNavScreens) {
            Modifier.fillMaxSize()
        } else {
            Modifier.padding(paddingValues)
        }

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Task.route,
            modifier = modifier
        ) {
            composable(BottomNavItem.Task.route) { TaskScreen(viewModel, navController) }
            composable(BottomNavItem.Calendar.route) { CalendarScreen() }
            composable(BottomNavItem.Document.route) { DocumentScreen() }
            composable(BottomNavItem.Settings.route) { SettingsScreen() }
            composable("Detail") {
                val task = viewModel.selectedtask ?: return@composable
                DetailTask(navController, task) }
        }
    }
}

@Composable
fun CalendarScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Calendar", fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun DocumentScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Document", fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Settings", fontSize = 20.sp, color = Color.Black)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}