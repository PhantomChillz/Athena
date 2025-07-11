// src/services/noteService.js
import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true // include cookies for auth
});

// CREATE
export const addNote = async (title, content) => {
  return await API.post('/api/notes', { title, content });
};

// READ - all notes
export const getNotes = async () => {
  const response = await API.get('/api/notes');
  return Array.isArray(response.data.notes) ? response.data.notes : [];
};

// READ - single note
export const getNotesById = async (noteId) => {
  const response = await API.get(`/api/notes/${noteId}`);
  return response.data;
};

// UPDATE
export const updateNotesById = async (noteId, title, content) => {
  return await API.put(`/api/notes/${noteId}`, {title, content});
};

// DELETE
export const delNotesById = async (noteId) => {
  return await API.delete(`/api/notes/${noteId}`);
};
