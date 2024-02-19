package me.muhaimin.githubuser.ui.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.muhaimin.githubuser.R
import me.muhaimin.githubuser.databinding.ActivityDetailBinding
import me.muhaimin.githubuser.factory.ViewModelFactory
import me.muhaimin.githubuser.ui.detail.follow.FollowPagerAdapter
import me.muhaimin.githubuser.ui.favorite.FavoriteActivity

private const val APPBAR_TITLE = "User Details"

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup app bar
        supportActionBar?.apply {
            title = APPBAR_TITLE
            elevation = 0f
            setDisplayHomeAsUpEnabled(true)
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)!!

        viewModel.updateUsername(username)
        viewModel.username.observe(this) {
            lifecycleScope.launch(Dispatchers.Default) {
                viewModel.getDetailUser()
            }
        }

        // fill data
        viewModel.userDetail.observe(this) { user ->
            binding.tvName.text = user.fullName
            binding.tvUsername.text = user.username
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .into(binding.iwUser)

            binding.tvFollowers.text = user.followersCount.toString()
            binding.tvFollowing.text = user.followingCount.toString()

            // view pager follower and following
            val followPagerAdapter = FollowPagerAdapter(this)
            followPagerAdapter.username = user.username.toString()

            binding.viewPagerFollow.adapter = followPagerAdapter
            TabLayoutMediator(binding.tabs, binding.viewPagerFollow) { tab, position ->
                when(position) {
                    0 -> tab.text = "Followers"
                    1 -> tab.text = "Following"
                }
            }.attach()
        }

        // when error to fetch data
        viewModel.errorMessage.observe(this) {
            if (!it.hasBeenHandled) {
                Snackbar.make(binding.root, "${it.getContentIfNotHandled()}", Snackbar.LENGTH_SHORT)
                    .setTextColor(Color.RED)
                    .show()
            }
        }

        // progress bar
        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            }else {
                View.GONE
            }
        }

        viewModel.isFavorite.observe(this) {isFavorite ->
            if (isFavorite) {
                binding.fabAddToFavorite.setImageDrawable(
                    AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_favorite_24)
                )

            } else {
                binding.fabAddToFavorite.setImageDrawable(
                    AppCompatResources.getDrawable(applicationContext, R.drawable.baseline_favorite_border_24)
                )
            }
        }

        // FAB add to favorite
        binding.fabAddToFavorite.setOnClickListener {
            var message = ""
            if (viewModel.isFavorite.value == true) {
                viewModel.removeUserFavorite()
                message = "deleted from favorites"

            } else {
                viewModel.addUserFavorite()
                message = "added to favorites"
            }

            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
                .setAction("see...") {
                    val intent = Intent(this@DetailActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                }.show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT,
                        "Hi, look at ${viewModel.username.value} on GitHub it has ${viewModel.userDetail.value?.followersCount} followers. Check this out https://github.com/${viewModel.userDetail.value?.username}"
                    )
                    type ="text/plain"
                }

                startActivity(Intent.createChooser(intent, "Share with"))

                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}