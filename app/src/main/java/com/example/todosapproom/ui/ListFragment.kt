package com.example.todosapproom.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todosapproom.R
import com.example.todosapproom.data.ToDo
import com.example.todosapproom.databinding.FragmentListBinding
import com.example.todosapproom.viewmodel.ToDoViewModel
import com.example.todosapproom.viewmodel.ToDoViewModelFactory
import com.example.todosapproom.repository.ToDoRepository
import com.example.todosapproom.data.ToDoDatabase
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var viewModel: ToDoViewModel

    private var isSearchActive = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize ViewModel after view is created
        viewModel = activityViewModels<ToDoViewModel> {
            ToDoViewModelFactory(
                ToDoRepository(
                    ToDoDatabase.getDatabase(requireContext()).toDoDao()
                ),
                requireContext()
            )
        }.value
        
        setupRecyclerView()
        setupSearch()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        toDoAdapter = ToDoAdapter(
            onItemClick = { toDo ->
                navigateToUpdateFragment(toDo)
            },
            onDeleteClick = { toDo ->
                showDeleteConfirmationDialog(toDo)
            }
        )

        binding.rvTodos.apply {
            adapter = toDoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                isSearchActive = query.isNotEmpty()
                viewModel.searchToDos(query)
            }
        })
    }

    private fun setupObservers() {
        viewModel.toDos.observe(viewLifecycleOwner) { toDos ->
            if (!isSearchActive) {
                toDoAdapter.submitList(toDos)
                updateEmptyState(toDos.isEmpty())
            }
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            if (isSearchActive) {
                toDoAdapter.submitList(searchResults)
                updateEmptyState(searchResults.isEmpty())
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener {
            navigateToAddFragment()
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.tvEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvTodos.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun navigateToAddFragment() {
        val addFragment = AddFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, addFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToUpdateFragment(toDo: ToDo) {
        val updateFragment = UpdateFragment.newInstance(toDo.id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, updateFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showDeleteConfirmationDialog(toDo: ToDo) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_confirmation_title))
            .setMessage(getString(R.string.delete_confirmation_message, toDo.name))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteToDo(toDo)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 