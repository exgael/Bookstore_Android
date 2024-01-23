package com.example.bookstore_android.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepository {

    private val bookApiService = BookApiService()

    fun getAllBooks(success: (List<Book>) -> Unit, failure: (Throwable) -> Unit) {
        bookApiService.service.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                val books = response.body()
                if (books != null) {
                    success(books)
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                failure(t)
            }
        })
    }

    fun getBookById(id: Int, success: (Book?) -> Unit, failure: (Throwable) -> Unit) {
        bookApiService.service.getBookById(id).enqueue(object : Callback<Book?> {
            override fun onResponse(call: Call<Book?>, response: Response<Book?>) {
                success(response.body())
            }

            override fun onFailure(call: Call<Book?>, t: Throwable) {
                failure(t)
            }
        })
    }

    fun addBook(book: Book, success: (Book?) -> Unit, failure: (Throwable) -> Unit) {
        bookApiService.service.addBook(book).enqueue(object : Callback<Book?> {
            override fun onResponse(call: Call<Book?>, response: Response<Book?>) {
                success(response.body())
            }

            override fun onFailure(call: Call<Book?>, t: Throwable) {
                failure(t)
            }
        })
    }

    fun updateBook(id: Int, book: Book, success: (Book?) -> Unit, failure: (Throwable) -> Unit) {
        bookApiService.service.updateBook(id, book).enqueue(object : Callback<Book?> {
            override fun onResponse(call: Call<Book?>, response: Response<Book?>) {
                success(response.body())
            }

            override fun onFailure(call: Call<Book?>, t: Throwable) {
                failure(t)
            }
        })
    }

    fun deleteBook(id: Int, success: () -> Unit, failure: (Throwable) -> Unit) {
        bookApiService.service.deleteBook(id).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                success()
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                failure(t)
            }
        })
    }
}