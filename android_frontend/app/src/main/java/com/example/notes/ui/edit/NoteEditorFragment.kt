package com.example.notes.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notes.databinding.FragmentNoteEditorBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

/**
 * PUBLIC_INTERFACE
 * NoteEditorFragment allows creating a new note or editing an existing one.
 * Shows delete button for existing notes.
 */
class NoteEditorFragment : Fragment() {

    private var _binding: FragmentNoteEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteEditorViewModel by viewModels()

    private val noteId: Long by lazy { arguments?.getLong("noteId", -1L) ?: -1L }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.load(noteId)

        if (noteId > 0) {
            // Load existing into fields by collecting StateFlow
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.note.collectLatest { loadedNote ->
                    loadedNote?.let { n ->
                        binding.title.setText(n.title)
                        binding.content.setText(n.content)
                    }
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.title.text?.toString().orEmpty()
            val content = binding.content.text?.toString().orEmpty()
            viewModel.save(noteId, title, content) {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.btnDelete.isVisible = noteId > 0
        binding.btnDelete.setOnClickListener {
            viewModel.delete(noteId) {
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
