package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment(), MenuProvider {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = AsteroidsAdapter(AsteroidsAdapter.OnClickListener {
            viewModel.showAsteroidDetails(it)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner) {
            it?.let {
                adapter.asteroids = it
                Timber.d("VM list observer: list size is: ${it.size}")
            }
        }

        viewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner) {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.showAsteroidDetailsComplete()
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        return when (menuItem.itemId) {
            R.id.menu_show_week -> {
                Timber.d("fragment: show week selected")
                viewModel.updateFilter(Filter.WEEK)
                true
            }
            R.id.menu_show_today -> {
                Timber.d("fragment: show today selected")
                viewModel.updateFilter(Filter.TODAY)
                true
            }
            R.id.menu_show_saved -> {
                Timber.d("fragment: show saved selected")
                viewModel.updateFilter(Filter.ALL)
                true
            }
            else -> return false
        }
    }
}
