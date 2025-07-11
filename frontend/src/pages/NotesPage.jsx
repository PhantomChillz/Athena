import React, { useEffect, useState } from 'react';
import {
  addNote,
  getNotes,
  getNotesById,
  updateNotesById,
  delNotesById
} from '../services/notesService.js';
import NavBar from '../components/NavBar.jsx';

function NotesPage() {
  const [notes, setNotes] = useState([]);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [editingNoteId, setEditingNoteId] = useState(null);

  // Fetch notes on mount
  useEffect(() => {
    loadNotes();
  }, []);

  const loadNotes = async () => {
    const allNotes = await getNotes();
    setNotes(allNotes);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (editingNoteId) {
      await updateNotesById(editingNoteId, title, content);
      setEditingNoteId(null);
    } else {
      await addNote(title, content);
    }
    setTitle('');
    setContent('');
    loadNotes();
  };

  const handleEdit = async (noteId) => {
    const note = await getNotesById(noteId);
    setTitle(note.title);
    setContent(note.content);
    setEditingNoteId(noteId);
  };

  const handleDelete = async (noteId) => {
    await delNotesById(noteId);
    loadNotes();
  };

  return (
    <>
    <NavBar/>
    <div className="max-w-4xl mx-auto py-10">
      <h1 className="text-3xl font-bold mb-6 text-center">📒 My Notes</h1>

      <form onSubmit={handleSubmit} className="mb-8 bg-base-100 p-6 rounded-lg shadow-lg space-y-4">
        <input
          className="input input-bordered w-full"
          placeholder="Note Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />
        <textarea
          className="textarea textarea-bordered w-full"
          placeholder="Note Content"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          rows="5"
          required
        />
        <button className="btn btn-primary w-full">
          {editingNoteId ? 'Update Note' : 'Add Note'}
        </button>
      </form>

      <div className="grid gap-4">
        {notes.length === 0 && (
          <div className="text-center text-gray-500">No notes found</div>
        )}
        {notes.map((note) => (
          <div key={note.id} className="bg-base-200 p-4 rounded-lg shadow">
            <h2 className="font-semibold text-xl">{note.title}</h2>
            <p className="mt-2 whitespace-pre-wrap">{note.content}</p>
            <div className="mt-4 flex gap-2">
              <button className="btn btn-outline btn-sm" onClick={() => handleEdit(note.noteId)}>
                Edit
              </button>
              <button className="btn btn-error btn-sm" onClick={() => handleDelete(note.noteId)}>
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
    </>
  );
}

export default NotesPage;
