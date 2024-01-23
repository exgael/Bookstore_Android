package com.example.bookstore_android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore_android.services.Book
import com.example.bookstore_android.services.BookRepository

class HomeViewModel : ViewModel() {

    private val bookRepository = BookRepository()

    // LiveData for books list
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    // LiveData for error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // LiveData for refreshing indicator
    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        loadBooks()
    }

    fun refreshBooks() {
        _isRefreshing.value = true
        loadBooks()
    }

    private fun loadBooks() {
        bookRepository.getAllBooks(
            success = { booksList ->
                _books.postValue(booksList)
                _isRefreshing.postValue(false)
            },
            failure = { throwable ->
                _error.postValue(throwable.message)
                _isRefreshing.postValue(false)
            }
        )
    }

    fun addBook(book: Book) {
        bookRepository.addBook(book,
            success = { newBook ->
                newBook?.let {
                    loadBooks()  // Refresh the list after adding a book
                }
            },
            failure = { throwable ->
                _error.postValue(throwable.message)
            }
        )
    }

    fun updateBook(id: Int, book: Book) {
        bookRepository.updateBook(id, book,
            success = { updatedBook ->
                updatedBook?.let {
                    loadBooks()  // Refresh the list after updating a book
                }
            },
            failure = { throwable ->
                _error.postValue(throwable.message)
            }
        )
    }

    fun deleteBook(id: Int) {
        bookRepository.deleteBook(id,
            success = {
                loadBooks()  // Refresh the list after deleting a book
            },
            failure = { throwable ->
                _error.postValue(throwable.message)
            }
        )
    }
}