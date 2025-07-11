import axios from 'axios';

const API = axios.create({
baseURL: 'http://localhost:8080/api',
withCredentials: true,
});

export const uploadFile = async (file) => {
const formData = new FormData();
formData.append('file', file);
const response = await API.post('/files', formData, {
headers: { 'Content-Type': 'multipart/form-data' },
});
return response.data;
};

export const getAllFiles = async () => {
const response = await API.get('/files');
return response.data.files; // Assuming your FileResponseDTO has files field
};

export const getFileById = async (fileId) => {
const response = await API.get(`/files/${fileId}`);
return response.data;
};

export const deleteFile = async (fileId) => {
const response = await API.delete(`/files/${fileId}`);
return response.data;
};

export const downloadFile = (fileId) => {
window.open(`http://localhost:8080/api/files/download/${fileId}`, '_blank');
};

export const previewFile = (fileId) => {
window.open(`http://localhost:8080/api/files/preview/${fileId}`, '_blank');
};