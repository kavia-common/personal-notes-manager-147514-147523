package com.example.notes.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentNotesListBinding
import com.example.notes.ui.list.adapter.NotesAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * NotesListFragment displays notes, provides a search field with debounce,
 * and a floating action button to create a new note.
 */
class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesListViewModel by viewModels()

    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = NotesAdapter { note ->
            findNavController().navigate(
                R.id.action_list_to_edit,
                Bundle().apply { putLong("noteId", note.id) }
            )
        }
        binding.recycler.adapter = adapter

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_edit, Bundle().apply {
                putLong("noteId", -1)
            })
        }

        binding.search.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText.orEmpty())
                return true
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notes.collectLatest { notes ->
                adapter.submitList(notes)
                binding.emptyState.isVisible = notes.isEmpty()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
