package me.muhaimin.githubuser.ui.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.muhaimin.githubuser.R
import me.muhaimin.githubuser.databinding.ActivityDetailBinding
import me.muhaimin.githubuser.ui.detail.follow.FollowFragment
import me.muhaimin.githubuser.ui.detail.follow.FollowPagerAdapter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)!!

        viewModel.updateUsername(username)
        viewModel.username.observe(this) {
            Log.d("USER DETAIL", "username: $it")
            lifecycleScope.launch(Dispatchers.Default) {
                viewModel.getDetailUser()
            }
        }

        // fill data
        viewModel.userDetail.observe(this) {
            binding.tvName.text = it.fullName
            binding.tvUsername.text = it.username
            Glide.with(binding.root)
                .load(it.avatarUrl)
                .into(binding.iwUser)

            binding.tvFollowers.text = it.followersCount.toString()
            binding.tvFollowing.text = it.followingCount.toString()

            // view pager follower and following
            val followPagerAdapter = FollowPagerAdapter(this)
            followPagerAdapter.username = it.username.toString()

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
                Snackbar.make(binding.root, "Failed: ${it.getContentIfNotHandled()}", Snackbar.LENGTH_SHORT)
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
    }
}