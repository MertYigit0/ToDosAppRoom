package com.example.todosapproom.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.example.todosapproom.R
import com.example.todosapproom.data.ToDo
import com.example.todosapproom.databinding.FragmentAddBinding
import com.example.todosapproom.viewmodel.ToDoViewModel
import com.example.todosapproom.viewmodel.ToDoViewModelFactory
import com.example.todosapproom.repository.ToDoRepository
import com.example.todosapproom.data.ToDoDatabase
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel
    private val messageHandler = Handler(Looper.getMainLooper())
    private var messageRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
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
        
        setupObservers()
        setupClickListeners()
        clearInputFields()
    }

    private fun setupObservers() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                showMessage(message)
                viewModel.clearMessage()
                
                // If it's a success message, clear fields and go back to list
                if (message.contains("successfully")) {
                    clearInputFields()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            saveToDo()
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun clearInputFields() {
        _binding?.let { binding ->
            binding.etTodoName.text?.clear()
            binding.todoNameLayout.error = null
            binding.tvMessage.visibility = View.GONE
        }
    }

    private fun saveToDo() {
        val todoName = binding.etTodoName.text?.toString()?.trim() ?: ""
        
        if (todoName.isEmpty()) {
            binding.todoNameLayout.error = getString(R.string.error_empty_todo_name)
            return
        }

        binding.todoNameLayout.error = null

        // Add new todo
        viewModel.addToDo(todoName)
    }

    private fun showMessage(message: String) {
        _binding?.let { binding ->
            binding.tvMessage.text = message
            binding.tvMessage.visibility = View.VISIBLE
            
            // Cancel any existing message hide operation
            messageRunnable?.let { messageHandler.removeCallbacks(it) }
            
            // Create new runnable to hide message
            messageRunnable = Runnable {
                _binding?.let { safeBinding ->
                    safeBinding.tvMessage.visibility = View.GONE
                }
            }
            
            // Schedule message hide after 3 seconds
            messageRunnable?.let { messageHandler.postDelayed(it, 3000) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel any pending message hide operations
        messageRunnable?.let { messageHandler.removeCallbacks(it) }
        messageRunnable = null
        _binding = null
    }

    companion object {
        fun newInstance(): AddFragment {
            return AddFragment()
        }
    }
} 