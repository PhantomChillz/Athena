// src/pages/DashboardPage.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import  useAuth  from '../auth/useAuth';
import Navbar from '../components/NavBar'

function DashboardPage() {
  const { user } = useAuth();

  return (<>
    <Navbar/>
    <div className="min-h-screen bg-base-100 px-8 py-10">
      <h1 className="text-3xl font-bold mb-6">Welcome, {user?.username || 'User'} 👋</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Upload Files Card */}
        <div className="card bg-base-200 shadow-md hover:shadow-xl transition-all">
          <div className="card-body items-center text-center">
            <h2 className="card-title">📤 Upload Files</h2>
            <p>Add new files to generate embeddings and ask questions.</p>
            <div className="card-actions justify-center mt-4">
              <Link to="/files" className="btn btn-primary">Upload</Link>
            </div>
          </div>
        </div>

        {/* View Notes Card */}
        <div className="card bg-base-200 shadow-md hover:shadow-xl transition-all">
          <div className="card-body items-center text-center">
            <h2 className="card-title">📝 My Notes</h2>
            <p>Browse and manage your saved documents and notes.</p>
            <div className="card-actions justify-center mt-4">
              <Link to="/files" className="btn btn-primary">View Notes</Link>
            </div>
          </div>
        </div>

        {/* Ask AI Card */}
        <div className="card bg-base-200 shadow-md hover:shadow-xl transition-all">
          <div className="card-body items-center text-center">
            <h2 className="card-title">🤖 Ask AI</h2>
            <p>Ask questions based on your uploaded files' content.</p>
            <div className="card-actions justify-center mt-4">
              <Link to="/ai/ask" className="btn btn-primary">Ask Now</Link>
            </div>
          </div>
        </div>
      </div>
    </div>
    </>
  );
}

export default DashboardPage;
