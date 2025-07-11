// src/ai/AIPage.jsx
import React, { useState } from 'react';
import { askQuestion } from '../services/aiService'
import NavBar from '../components/NavBar';

function AIPage() {
  const [question, setQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleAsk = async () => {
    if (!question.trim()) return;
    setLoading(true);
    setError('');
    setAnswer('');

    try {
      const result = await askQuestion(question);
      setAnswer(result);
    } catch (err) {
      setError('Failed to get answer. Try again later.');
    } finally {
      setLoading(false);
    }
  };

  return (<>
    <NavBar/>
    <div className="max-w-3xl mx-auto mt-10 p-6 bg-base-100 rounded-xl shadow-md">
      <h1 className="text-2xl font-semibold mb-4">Ask AI Anything from Your Files</h1>

      <textarea
        className="textarea textarea-bordered w-full mb-4"
        rows="4"
        placeholder="Ask a question..."
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
      />

      <div className="flex justify-end">
        <button className="btn btn-primary" onClick={handleAsk} disabled={loading}>
          {loading ? 'Thinking...' : 'Ask'}
        </button>
      </div>

      {error && <p className="text-red-500 mt-4">{error}</p>}

      {answer && (
        <div className="mt-6 bg-neutral text-neutral-content p-4 rounded-md">
          <h2 className="text-lg font-medium mb-2">Answer:</h2>
          <p>{answer}</p>
        </div>
      )}
    </div>
    </>
  );
}

export default AIPage;
