package com.example.listfetcher

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.listfetcher.data.DataObj
import com.example.listfetcher.util.OnlineHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (OnlineHelper.isOnline(this)) {
            subscribeUi()
        } else {
            setContent { DataRetrievalErrorDialog() }
        }
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                setContent { MainScreen(it.dataList) }
            }
        }
    }

    @Composable
    fun MainScreen(dataList: List<DataObj?>) {
        remember { dataList }
        if (dataList.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight(0.75F)
                    .fillMaxWidth(1F)
                    .background(Color.Black)
                    .padding(
                        horizontal = 0.dp,
                        vertical = 100.dp
                    )
            )
            {
                Text(
                    text = "Loading...",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                IndeterminateCircularIndicator()
            }
        } else {
            Column(
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = 28.dp
                    )
                    .background(Color.DarkGray)
            ) {
                Header()
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth(1F)
                        .fillMaxHeight(1F)

                ) {
                    itemsIndexed(dataList) { index, item ->
                        val backgroundColor =
                            if (index % 2 == 0) Color.LightGray else Color.White
                        DataItem(item, backgroundColor)
                    }
                }
            }
        }
    }

    @Composable
    fun IndeterminateCircularIndicator() {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = Color.Cyan,
            trackColor = Color.LightGray,
        )
    }

    @Composable
    fun DataRetrievalErrorDialog(
    ) {
        val onDismissRequest = { viewModel.syncData() }

        AlertDialog(
            icon = {
                Icon(Icons.Default.Warning, contentDescription = "Warning Icon")
            },
            title = {
                Text(text = "Uh-oh!")
            },
            text = {
                Text(text = "Unable to retrieve data. Please check your connection and try again.")
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Try again")
                }
            }
        )
    }

    @Composable
    fun Header() {
        PrettyRow(Color.Black) {
            HeaderTextItem(text = "Item ID")
            HeaderTextItem(text = "List ID")
            HeaderTextItem(text = "Item Name")
        }
    }

    @Composable
    fun DataItem(obj: DataObj?, color: Color) {
        if (obj != null) {
            PrettyRow(color) {
                TextItem(text = obj.id.toString())
                TextItem(text = obj.listId.toString())
                TextItem(text = obj.name.toString())

            }
        }
    }

    @Composable
    fun HeaderTextItem(text: String) {
        Text(
            text = text,
            color = Color.Cyan,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 24.dp),
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun TextItem(text: String) {
        Text(
            text = text,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun PrettyRow(color: Color, content: @Composable () -> Unit) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1.0F)
                .background(color = color)
        ) {
            content()
        }
    }

}