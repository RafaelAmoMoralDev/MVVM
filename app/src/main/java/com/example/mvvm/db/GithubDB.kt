package com.example.mvvm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvm.model.Contributor
import com.example.mvvm.model.Repo
import com.example.mvvm.model.RepoSearchResult
import com.example.mvvm.model.User

@Database(
    entities = [
        User::class,
        Repo::class,
        Contributor::class,
        RepoSearchResult::class
    ],
    version = 1
)
abstract class GithubDB: RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun repoDao(): RepoDAO
}