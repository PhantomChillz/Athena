import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import  useAuth  from './useAuth'
import { signupRequest } from './authService';

function SignupPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      await signupRequest(form);
      const success = await login(form.username, form.password);
      if (success) navigate('/dashboard');
      else setError('Signup succeeded, but login failed.');
    } catch{
      setError('Signup failed');
    }
  };

  return (
    <div className="flex justify-center items-center h-screen">
      <form onSubmit={handleSubmit} className="flex flex-col gap-4 w-96 bg-base-100 p-10 rounded-xl shadow-xl">
        <h1 className="text-2xl font-semibold mb-4 text-center">Create Account</h1>

        <input
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
          className="input input-bordered"
          required
        />
        <input
          name="email"
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          className="input input-bordered"
          required
        />
        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="input input-bordered"
          required
        />

        {error && <p className="text-red-500 text-sm">{error}</p>}

        <button type="submit" className="btn btn-primary">Sign Up</button>

        <p className="text-center">
          Already have an account? <a href="/login" className="link">Login</a>
        </p>
      </form>
    </div>
  );
}

export default SignupPage;
