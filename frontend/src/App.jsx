import './index.css'
import LoginPage from './auth/LoginPage'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import SignupPage from './auth/SignupPage';
import ProtectedRoute from './auth/ProtectedRoute';
import LandingPage from './pages/LandingPage';
import DashboardPage from './pages/DashboardPage';
import NotesPage from './pages/NotesPage';
import FilesPage from './pages/FilesPage';
import AIPage from './pages/AIPage';
function App() {

  return (
    <>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/notes" element={<ProtectedRoute><NotesPage /></ProtectedRoute>} />
            <Route path="/" element={<LandingPage />} />
            <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
            <Route path="/files" element={<ProtectedRoute><FilesPage /></ProtectedRoute>} />
            <Route path="/ai/ask" element={<ProtectedRoute><AIPage/></ProtectedRoute>} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </>
  )
}

export default App
