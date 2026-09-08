package vn.example.ch30compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Đây là project DUY NHẤT trong sách viết bằng Kotlin — Jetpack Compose không
 * có API cho Java, chỉ dùng được với Kotlin. Xem Chương 30 để hiểu vì sao và
 * so sánh trực tiếp với bản XML + ViewBinding tương đương ở Chương 7.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    GreetingScreen()
                }
            }
        }
    }
}

@Composable
fun GreetingScreen() {
    // remember + mutableStateOf: khai báo "đây là trạng thái, khi nó đổi hãy vẽ
    // lại (recompose) đúng phần UI phụ thuộc vào nó" — không có findViewById
    // hay binding.textResult.setText(...) nào ở đây cả.
    var name by remember { mutableStateOf("") }
    var greeting by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập tên của bạn") }
        )

        Button(
            onClick = {
                greeting = if (name.isBlank()) "Bạn chưa nhập tên." else "Xin chào, $name!"
            },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Chào")
        }

        // Text() này chỉ vẽ lại khi "greeting" đổi (do bấm nút) — "name" đổi lúc
        // gõ chữ KHÔNG làm dòng này vẽ lại, đúng hành vi tương đương bản XML ở
        // Chương 7 (chỉ cập nhật textResult khi bấm buttonGreet).
        Text(text = greeting, modifier = Modifier.padding(top = 16.dp))
    }
}
