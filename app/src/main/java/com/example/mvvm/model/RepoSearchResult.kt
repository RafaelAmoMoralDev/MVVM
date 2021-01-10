package com.example.mvvm.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.mvvm.db.GithubTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(GithubTypeConverters::class)
class RepoSearchResult(
    val query: String,
    val reposIds: List<Int>,
    val totalCount: Int,
    val next: Int
)