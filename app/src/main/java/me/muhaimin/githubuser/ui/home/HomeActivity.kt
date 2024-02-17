package me.muhaimin.githubuser.ui.home

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.muhaimin.githubuser.R
import me.muhaimin.githubuser.databinding.ActivityHomeBinding
import me.muhaimin.githubuser.model.User
import me.muhaimin.githubuser.ui.detail.DetailActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvListGithubUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListGithubUser.addItemDecoration(itemDecoration)

        homeViewModel.listUser.observe(this) {
            if(it.isEmpty()) {
                binding.textView.text = getString(R.string.nothing_to_show)
            }

            val adapter = ListUserAdapter(it)
            adapter.submitList(it)

            binding.rvListGithubUser.adapter = adapter

            adapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    // navigate to user detail
                    val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USERNAME, user.username.toString())

                    startActivity(intent)
                }

            })
        }

        // when error to fetch data
        homeViewModel.errorMessage.observe(this) {
            if (!it.hasBeenHandled) {
                Snackbar.make(binding.root, "Failed: ${it.getContentIfNotHandled()}", Snackbar.LENGTH_SHORT)
                    .setTextColor(Color.RED)
                    .show()
            }
        }

        // search
        homeViewModel.searchQuery.observe(this) {
            binding.textView.text = ""
            lifecycleScope.launch(Dispatchers.Default) {
                homeViewModel.searchUser(it)
            }
        }

        // progress bar
        homeViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            }else {
                View.GONE
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    homeViewModel.updateSearchQuery(searchView.text.toString())
                    false
                }
        }
    }
}