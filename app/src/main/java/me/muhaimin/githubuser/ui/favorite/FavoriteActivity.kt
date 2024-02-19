package me.muhaimin.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.muhaimin.githubuser.databinding.ActivityFavoriteBinding
import me.muhaimin.githubuser.factory.ViewModelFactory
import me.muhaimin.githubuser.model.User
import me.muhaimin.githubuser.ui.detail.DetailActivity


private const val APPBAR_TITLE = "Favorite User"

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup app bar
        supportActionBar?.apply {
            title = APPBAR_TITLE
            setDisplayHomeAsUpEnabled(true)
        }

        val factory = ViewModelFactory.getInstance(applicationContext)
        val viewModel by viewModels<UserFavoriteViewModel> { factory }

        // set up view
        val layoutManager = LinearLayoutManager(this)
        binding.rvListUserFavorite.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListUserFavorite.addItemDecoration(itemDecoration)

        viewModel.getUserFavorite().observe(this) { listUser ->
            val user = listUser.map {
                User(
                    it.username, it.avatarUrl
                )
            }

            val adapter = ListUserFavoriteAdapter(user)
            adapter.submitList(user)

            binding.rvListUserFavorite.adapter = adapter

            adapter.setOnItemClickCallback(object : ListUserFavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    // navigate to user detail
                    val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USERNAME, user.username.toString())

                    startActivity(intent)
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}