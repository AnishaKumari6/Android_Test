package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AttendancePrortal()
        }
    }
}

data class Student(
    val name: String,
    val status: Boolean
)

class StudentAttendanceViewModel : ViewModel() {

    var absent by mutableStateOf(4)
    var present by mutableStateOf(0)

    var studentData = mutableStateListOf(
        Student("Anisha", false),
        Student("Arpit", false),
        Student("Anshul", false),
        Student("Kaalu", false)
    )

        private set
    fun Attendance(index: Int) {
        studentData[index] = studentData[index].copy(
            status = !studentData[index].status
        )
    }
    fun markPresent(index: Int) {

        if (!studentData[index].status) {
            present++
            absent--
        }

        studentData[index] = studentData[index].copy(
            status = true
        )
    }
    fun markAbsent(index: Int) {

        if (studentData[index].status) {
            present--
            absent++
        }

        studentData[index] = studentData[index].copy(
            status = false
        )
    }

    fun totalPresent(): Int {
        return studentData.count {
            it.status
        }
    }

    fun totalAbsent(): Int {
        return studentData.count {
            !it.status
        }
    }
}

@Composable
fun AttendancePrortal(
    viewModel: StudentAttendanceViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Student Attendance",
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 39.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(viewModel.studentData) { index, item ->

                StudentRow(
                    name = item.name,
                    status = item.status,
                    PresentStatus = {
                        viewModel.markPresent(index)
                    },
                    AbsentStatus = {
                        viewModel.markAbsent(index)
                    }
                )
            }
        }

        Text(
            text = "Present: ${viewModel.totalPresent()}",
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "Absent: ${viewModel.totalAbsent()}"
        )
    }
}

@Composable
fun StudentRow(
    name: String,
    status: Boolean,
    PresentStatus: () -> Unit,
    AbsentStatus: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = name,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = if (status) "Present" else "Absent"
        )

        Button(
            onClick = {
                if (status) {
                    AbsentStatus()
                } else {
                    PresentStatus()
                }
            }
        ) {
            Text(
                text = if (status) "Mark Absent" else "Mark Present"
            )
        }
    }
}