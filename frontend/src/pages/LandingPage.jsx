// src/pages/LandingPage.jsx
import React from 'react';
import { Link } from 'react-router-dom';

function LandingPage() {
  return (
    <div className="min-h-screen bg-base-200 flex flex-col items-center justify-center text-center px-6">
      <h1 className="text-4xl md:text-6xl font-bold mb-4">Welcome to Knowledge Hub</h1>
      <p className="text-lg md:text-xl mb-8 text-gray-500 max-w-xl">
        Your smart, AI-powered assistant to organize files, take notes, and ask questions with context.
      </p>
      <div className="flex gap-4">
        <Link to="/login" className="btn btn-primary btn-wide">Login</Link>
        <Link to="/signup" className="btn btn-outline btn-wide">Sign Up</Link>
      </div>
    </div>
  );
}

export default LandingPage;