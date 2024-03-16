package com.example.homework2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var buttonLoadCards : Button
    private lateinit var textViewDisplayCards : TextView

    private val API_CARDS_STRING : String = "http://api.magicthegathering.io/v1/cards"
    private var pageIndex : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonLoadCards = findViewById(R.id.buttonLoadCards)
        textViewDisplayCards = findViewById(R.id.textViewDisplayCards)

        buttonLoadCards.setOnClickListener{
            buttonLoadCards.isEnabled = false
            clearTextViewDisplayCards()
            lifecycleScope.launch(Dispatchers.IO){
                val json = retrieveCardsJsonFromURL()
            }
        }
    }

    fun displayCardsOnTextView(cardsAsString : String){
        textViewDisplayCards.text = cardsAsString
    }

    fun clearTextViewDisplayCards() : Boolean{
        if(textViewDisplayCards == null)
            return false
        if(textViewDisplayCards.text.toString().isEmpty())
            return false
        textViewDisplayCards.text = ""
        return true
    }

    suspend fun retrieveCardsJsonFromURL() : String?{
        var urlConnection : HttpURLConnection? = null
        var bufferedReader : BufferedReader? = null
        var json : String? = null

        try{
            val url = URL("$API_CARDS_STRING?page=$pageIndex")
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            val inputStream : InputStream = urlConnection.inputStream
            val buffer = StringBuffer()

            if(inputStream == null)
                return null

            bufferedReader = BufferedReader(InputStreamReader(inputStream))

            var line : String? = bufferedReader.readLine()
            while (line != null){
                buffer.append(line+"\n")
                line = bufferedReader.readLine()
            }

            if(buffer.isEmpty()){
                return null
            }

            json = buffer.toString()
        }catch (e : Exception){
            e.message
        }finally {
            urlConnection?.disconnect()
            bufferedReader?.close()
        }

        return json
    }


    suspend fun parseJsonToCard(jsonString : String) : List<Card>{
        val cards = mutableListOf<Card>()
        val jsonArray = JSONArray(jsonString)

        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val colors = jsonObject.getJSONArray("colors")
            val colorList = mutableListOf<String>()
            for(j in 0 until colors.length()){
                colorList.add(colors.getString(j))
            }
            cards.add(Card(name,colorList))
        }

        return cards
    }

    suspend fun sortCards(cards : List<Card>) : List<Card>{
        return cards.sortedBy { it.name }
    }

}