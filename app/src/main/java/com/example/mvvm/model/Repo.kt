package com.example.mvvm.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    primaryKeys = ["name, owner_login"],
    // Al igual que en SQL, crear indices en room aumenta la velocidad en consultas
    // SELECT pero relantiza operaciones de insercción y modificación
    indices = [
        Index("id"),
        Index("owner_login")]
)
data class Repo(
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("full_name")
    val fullName: String?,
    @field:SerializedName("description")
    val description: String?,
    //Con esto conseguimos que los campos de owner empiecen
    // con el prefijo dado
    @field:Embedded(prefix = "owner_")
    @field:SerializedName("owner")
    val owner: Owner?,
    @field:SerializedName("startgazers_count")
    val stars: Int
) {
    data class Owner(
        @field:SerializedName("login")
        val login: String,
        @field:SerializedName("url")
        val url: String?
    )

    companion object {
        const val UNKNOWN_ID = -1
    }

}