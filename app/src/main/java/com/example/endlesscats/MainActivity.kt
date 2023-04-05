package com.example.endlesscats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import android.widget.Toast

data class ImageData(val id: String, val url: String, val width: Int, val height: Int)

class MainActivity : AppCompatActivity() {
    private lateinit var petList: MutableList<ImageData>
    private lateinit var rvPets: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvPets = findViewById(R.id.pet_list)
        petList = mutableListOf()

        getCatImages()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun getCatImages() {
        val client = AsyncHttpClient()
        val url = "https://api.thecatapi.com/v1/images/search?mime_types=jpg,png,gif&limit=10&api_key=live_BAswC3agQ75aCNGsYdkAXrHQu6vbgOdkjuyEvpF2RlPJztrhy3qM3z1HwXn6ZHiF"
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val imageArray = json.jsonArray
                for (i in 0 until imageArray.length()) {
                    val imageObject = imageArray.getJSONObject(i)
                    val id = imageObject.getString("id")
                    val url = imageObject.getString("url")
                    val width = imageObject.getInt("width")
                    val height = imageObject.getInt("height")
                    val imageData = ImageData(id, url, width, height)
                    petList.add(imageData)
                }
                val adapter = PetAdapter(petList)
                rvPets.adapter = adapter
                rvPets.layoutManager = LinearLayoutManager(this@MainActivity)

                Toast.makeText(this@MainActivity, "Meow!", Toast.LENGTH_SHORT).show()
            }


            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, throwable: Throwable?) {
                Log.d("Cat Error", errorResponse ?: "")
            }
        })
    }

    class PetAdapter(private val petList: List<ImageData>) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)
            return PetViewHolder(view)
        }

        override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
            val imageData = petList[position]
            Glide.with(holder.itemView.context)
                .load(imageData.url)
                .centerCrop()
                .into(holder.petImage)
            holder.petId.text = "ID: ${imageData.id}"
            holder.petUrl.text = "URL: ${imageData.url}"
            holder.petUrl.setOnClickListener {
                val message = "${imageData.id} is the ID for this cat! Meow!"
                (holder.itemView.context as MainActivity).showToast(message)
            }
        }

        override fun getItemCount(): Int {
            return petList.size
        }

        inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val petImage: ImageView = itemView.findViewById(R.id.pet_image)
            val petId: TextView = itemView.findViewById(R.id.pet_id)
            val petUrl: TextView = itemView.findViewById(R.id.pet_url)
        }
    }

}