package com.example.endlesscats
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
        holder.petId.text = imageData.id
        holder.petUrl.text = imageData.url
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