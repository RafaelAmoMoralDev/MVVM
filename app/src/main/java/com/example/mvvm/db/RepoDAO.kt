package com.example.mvvm.db

import android.util.SparseArray
import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvm.model.Contributor
import com.example.mvvm.model.Repo
import com.example.mvvm.model.RepoSearchResult
import com.example.mvvm.model.User
import java.util.*

@Dao
abstract class RepoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg repos: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContributors(contributors: List<Contributor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repositories: List<Repo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createRepoIfNoExists(repos: Repo): Long

    @Query("SELECT * FROM Repo WHERE owner_login = :ownerLogin AND name = :name")
    abstract fun get(ownerLogin: String, name: String): LiveData<Repo>

    @Query(
        "SELECT login, avatarUrl, repoName, repoOwner, contributions FROM contributor WHERE" +
                " repoName = :name AND repoOwner = :owner ORDER BY contributions DESC"
    )
    abstract fun getContributors(owner: String, name: String): LiveData<List<Contributor>>

    @Query("SELECT * FROM Repo WHERE owner_login = :owner ORDER BY stars DESC")
    abstract fun getRepositories(owner: String): LiveData<List<Repo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(result: RepoSearchResult)

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    abstract fun search(query: String): LiveData<RepoSearchResult>

    fun getOrdered(repoIds: List<Int>): LiveData<List<Repo>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(repoIds)) { repositories ->
            Collections.sort(repositories) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            repositories
        }
    }

    @Query("SELECT * FROM Repo WHERE id in(:repoIds)")
    protected abstract fun loadById(repoIds: List<Int>): LiveData<List<Repo>>

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    protected abstract fun findSearchResult(query: String): RepoSearchResult?

}