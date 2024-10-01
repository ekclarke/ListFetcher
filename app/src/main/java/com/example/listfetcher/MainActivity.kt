package com.example.listfetcher

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.listfetcher.data.DataObj
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
        subscribeUi()
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
        remember {
            dataList
        }
        if (dataList.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight(0.75F)
                    .fillMaxWidth(1F)
                    .padding(
                        horizontal = 0.dp,
                        vertical = 100.dp
                    )
            )
            { IndeterminateCircularIndicator() }
        }
        if (dataList.isNotEmpty() && dataList.first() == null) {
            DataRetrievalErrorDialog() //TODO: handle?
        } else {
            Column(
                Modifier.padding(
                    horizontal = 0.dp,
                    vertical = 28.dp
                )
            ) {
                Header()
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(1F)
                        .fillMaxHeight(1F)

                ) {
                    items(dataList) { dataObj ->
                        DataItem(dataObj)
                    }
                }
            }
        }
    }

    @Composable
    fun IndeterminateCircularIndicator() {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
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
        Row(Modifier.fillMaxWidth(1.0F)) {
            TextItem(text = "Item ID", weight = FontWeight.Bold)
            TextItem(text = "List ID", weight = FontWeight.Bold)
            TextItem(text = "Item Name", weight = FontWeight.Bold)
        }
    }

    @Composable
    fun DataItem(obj: DataObj?) {
        if (obj != null) {
            Row(Modifier.fillMaxWidth(1.0F)) {
                TextItem(text = obj.id.toString(), weight = FontWeight.Normal)
                TextItem(text = obj.listId.toString(), weight = FontWeight.Normal)
                TextItem(text = obj.name.toString(), weight = FontWeight.Normal)

            }
        }
    }

    @Composable
    fun TextItem(text: String, weight: FontWeight) {
        Text(
            text = text,
            color = Color.Cyan,
            fontWeight = weight,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
    }

}