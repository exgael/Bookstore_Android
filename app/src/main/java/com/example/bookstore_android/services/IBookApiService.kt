package com.example.bookstore_android.services


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

public interface IBookApiService {

    // Get all books
    @GET("books")
    fun getAllBooks(): Call<List<Book>>

    // Get a single book by ID
    @GET("books/{id}")
    fun getBookById(@Path("id") id: Int): Call<Book?>

    // Add a new book
    @POST("books")
    fun addBook(@Body book: Book?): Call<Book?>

    // Update an existing book
    @PUT("books/{id}")
    fun updateBook(@Path("id") id: Int, @Body book: Book?): Call<Book?>

    // Delete a book
    @DELETE("books/{id}")
    fun deleteBook(@Path("id") id: Int): Call<Void?>
}
