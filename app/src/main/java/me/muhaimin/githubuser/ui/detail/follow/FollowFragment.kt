package me.muhaimin.githubuser.ui.detail.follow

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.muhaimin.githubuser.databinding.FragmentFollowBinding
import me.muhaimin.githubuser.model.User

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: FollowViewModel
//    private val factory = ViewModelFactory.getInstance(requireContext())
//    private val userDetailVIewModel by viewModels<DetailViewModel> { factory }

    companion object {
        val ARG_POSITION = "arg_position"
        val ARG_USERNAME = "arg_username"
        val RESULT_KEY = "follow_result_key"
        val KEY_USERNAME = "key_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)

        viewModel = FollowViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        val position = arguments?.getInt(ARG_POSITION)

        // setup RecycleView
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        // fill data
        viewModel.listFollow.observe(viewLifecycleOwner) {
            if(it.isEmpty()) {
                binding.textView.text = "nothing to show"
            }

            val adapter = ListFollowAdapter(it)
            adapter.submitList(it)

            binding.rvFollow.adapter = adapter

            adapter.setOnItemClickCallback(object: ListFollowAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
//                    setFragmentResult(RESULT_KEY, bundleOf(KEY_USERNAME to user.username))
                }

            })
        }

        viewModel.updateUsername(username.toString())
        if (position != null) {
            viewModel.updatePosition(position.toInt())
        }

        viewModel.position.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.Default) {
                viewModel.fetchFollow(username.toString(), if(it == 0) {
                    TypeFollow.FOLLOWERS
                } else {
                    TypeFollow.FOLLOWING
                })
            }
        }


        // when error to fetch data
        viewModel.errorMessage.observe(viewLifecycleOwner) {

            it.getContentIfNotHandled()?.let {
                Snackbar.make(view, "onFailure: $it", Snackbar.LENGTH_SHORT)
                    .setTextColor(Color.RED)
                    .show()
            }

        }

        // progress bar
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            }else {
                View.GONE
            }
        }
    }
}