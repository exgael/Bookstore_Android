@file:OptIn(ExperimentalMaterialApi::class)

package com.example.bookstore_android.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.bookstore_android.services.Book

@ExperimentalMaterialApi
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val books by homeViewModel.books.observeAsState(initial = emptyList())
    val error by homeViewModel.error.observeAsState("")
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    val isRefreshing by homeViewModel.isRefreshing.observeAsState(false)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {  homeViewModel.refreshBooks() })
    var showDialog by remember { mutableStateOf(false) }

    if (selectedBook != null) {
        BookDetailScreen(
            book = selectedBook!!,
            onSave = { updatedBook ->
                homeViewModel.updateBook(updatedBook.id, updatedBook)
                selectedBook = null
            },
            onDelete = { book ->
                homeViewModel.deleteBook(book.id)
                selectedBook = null
            },
            onDismiss = { selectedBook = null }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Bookstore") },
                    actions = {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Book")
                        }
                    }
                )
            },
            content = { innerPadding ->
                Box(Modifier.pullRefresh(pullRefreshState)) {
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                            .zIndex(1f)
                    )
                    Column(modifier = Modifier.padding(innerPadding)) {
                        BookList(books) { book ->
                            selectedBook = book
                        }
                        if (error.isNotEmpty()) {
                            Text(
                                text = error,
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        )

        if (showDialog) {
            AddBookDialog(homeViewModel = homeViewModel, onDismissRequest = { showDialog = false })
        }
    }
}

@Composable
fun BookList(books: List<Book>, onBookClick: (Book) -> Unit) {
    LazyColumn {
        items(books) { book ->
            BookRow(book, onBookClick)
        }
    }
}

@Composable
fun BookRow(book: Book, onBookClick: (Book) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBookClick(book) },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            book.title?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Author: ${book.author}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Published year: ${book.publishedYear}")

        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookDetailScreen(book: Book, onSave: (Book) -> Unit, onDelete: (Book) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf(book.title ?: "") }
    var author by remember { mutableStateOf(book.author ?: "") }
    var publishedYear by remember { mutableStateOf(book.publishedYear.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Edit Book",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = publishedYear,
                    onValueChange = { publishedYear = it },
                    label = { Text("Published Year") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onSave(Book(book.id, title, author, publishedYear.toInt())) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                    ) {
                        Text("Save Changes", color = Color.White)
                    }
                    Button(
                        onClick = { onDelete(book) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text("Delete Book", color = Color.White)
                    }
                }
            }
        }
    )
}

@Composable
fun AddBookDialog(homeViewModel: HomeViewModel, onDismissRequest: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var publishedYear by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.LightGray.copy(alpha = 0.8f)
                        )
                    )
                )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add a New Book", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                TextField(value = author, onValueChange = { author = it }, label = { Text("Author") })
                TextField(value = publishedYear, onValueChange = { publishedYear = it }, label = { Text("Published Year") })
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && author.isNotEmpty() && publishedYear.isNotEmpty()) {
                                homeViewModel.addBook(Book(0, title, author, publishedYear.toInt()))
                                onDismissRequest()
                            }
                        }
                    ) {
                        Text("Add")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

