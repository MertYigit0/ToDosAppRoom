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
import com.example.todosapproom.R
import com.example.todosapproom.data.ToDo
import com.example.todosapproom.databinding.FragmentUpdateBinding
import com.example.todosapproom.viewmodel.ToDoViewModel
import com.example.todosapproom.viewmodel.ToDoViewModelFactory
import com.example.todosapproom.repository.ToDoRepository
import com.example.todosapproom.data.ToDoDatabase
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel
    private var editingToDoId: Int = -1
    private val messageHandler = Handler(Looper.getMainLooper())
    private var messageRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activityViewModels<ToDoViewModel> {
            ToDoViewModelFactory(
                ToDoRepository(
                    ToDoDatabase.getDatabase(requireContext()).toDoDao()
                ),
                requireContext()
            )
        }.value
        
        setupArguments()
        setupObservers()
        setupClickListeners()
        loadToDoData()
    }

    private fun setupArguments() {
        arguments?.let { args ->
            editingToDoId = args.getInt(ARG_TODO_ID, -1)
            if (editingToDoId == -1) {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun setupObservers() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                showMessage(message)
                viewModel.clearMessage()

                if (message.contains("successfully")) {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            updateToDo()
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun loadToDoData() {
        val toDo = viewModel.getToDoById(editingToDoId)
        toDo?.let { todo ->
            binding.etTodoName.setText(todo.name)
        } ?: run {
            Toast.makeText(context, getString(R.string.todo_not_found), Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateToDo() {
        val todoName = binding.etTodoName.text?.toString()?.trim() ?: ""
        
        if (todoName.isEmpty()) {
            binding.todoNameLayout.error = getString(R.string.error_empty_todo_name)
            return
        }

        binding.todoNameLayout.error = null

        val existingToDo = viewModel.getToDoById(editingToDoId)
        existingToDo?.let { todo ->
            val updatedToDo = todo.copy(name = todoName)
            viewModel.updateToDo(updatedToDo)
        } ?: run {
            Toast.makeText(context, getString(R.string.todo_not_found), Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun showMessage(message: String) {
        _binding?.let { binding ->
            binding.tvMessage.text = message
            binding.tvMessage.visibility = View.VISIBLE

            messageRunnable?.let { messageHandler.removeCallbacks(it) }

            messageRunnable = Runnable {
                _binding?.let { safeBinding ->
                    safeBinding.tvMessage.visibility = View.GONE
                }
            }

            messageRunnable?.let { messageHandler.postDelayed(it, 3000) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messageRunnable?.let { messageHandler.removeCallbacks(it) }
        messageRunnable = null
        _binding = null
    }

    companion object {
        private const val ARG_TODO_ID = "todo_id"

        fun newInstance(todoId: Int): UpdateFragment {
            return UpdateFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TODO_ID, todoId)
                }
            }
        }
    }
} 