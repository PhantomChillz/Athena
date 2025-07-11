import React, { useEffect, useState } from 'react';
import {
uploadFile,
getAllFiles,
deleteFile,
downloadFile,
previewFile,
} from '../services/fileService';
import NavBar from '../components/NavBar';

function FilesPage() {
const [files, setFiles] = useState([]);
const [selectedFile, setSelectedFile] = useState(null);
const [uploading, setUploading] = useState(false);
const [error, setError] = useState(null);

const fetchFiles = async () => {
try {
const data = await getAllFiles();
setFiles(data);
} catch (err) {
setError('Failed to load files');
}
};

useEffect(() => {
fetchFiles();
}, []);

const handleFileChange = (e) => {
setSelectedFile(e.target.files[0]);
};

const handleUpload = async (e) => {
e.preventDefault();
if (!selectedFile) return;
setUploading(true);
setError(null);
try {
await uploadFile(selectedFile);
setSelectedFile(null);
await fetchFiles();
} catch (err) {
setError('Upload failed');
} finally {
setUploading(false);
}
};

const handleDelete = async (id) => {
if (!window.confirm('Are you sure you want to delete this file?')) return;
try {
await deleteFile(id);
await fetchFiles();
} catch {
setError('Delete failed');
}
};

return (<>
<NavBar/>
<div className="p-8 max-w-5xl mx-auto">
<h1 className="text-3xl font-bold mb-6 text-center">📁 File Manager</h1>
  <form onSubmit={handleUpload} className="flex gap-4 items-center mb-8">
    <input
      type="file"
      onChange={handleFileChange}
      className="file-input file-input-bordered"
    />
    <button type="submit" className="btn btn-primary" disabled={uploading}>
      {uploading ? 'Uploading...' : 'Upload'}
    </button>
  </form>

  {error && <p className="text-red-500 mb-4">{error}</p>}

  <div className="overflow-x-auto shadow rounded-lg">
    <table className="table w-full">
      <thead>
        <tr>
          <th>Name</th>
          <th>Type</th>
          <th>Size (KB)</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {files.map((file) => (
          <tr key={file.fileId}>
            <td>{file.fileName}</td>
            <td>{file.contentType}</td>
            <td>{(file.size / 1024).toFixed(2)}</td>
            <td className="flex flex-wrap gap-2">
              <button
                className="btn btn-sm btn-outline btn-success"
                onClick={() => previewFile(file.fileId)}
              >
                Preview
              </button>
              <button
                className="btn btn-sm btn-outline btn-info"
                onClick={() => downloadFile(file.fileId)}
              >
                Download
              </button>
              <button
                className="btn btn-sm btn-outline btn-error"
                onClick={() => handleDelete(file.fileId)}
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
    {files.length === 0 && (
      <p className="text-center text-gray-500 my-8">No files uploaded yet.</p>
    )}
  </div>
</div>
</>
);
}

export default FilesPage;