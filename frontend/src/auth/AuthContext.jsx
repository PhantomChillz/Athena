import { createContext, useState, useEffect } from "react";
import {getCurrentUser,loginRequest,logoutRequest} from './authService'
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // Initially true
  const [error, setError] = useState(null);

  // 🔄 Load user info on mount
  useEffect(() => {
    getCurrentUser()
      .then((res) => setUser(res))
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, []);

  const login = async (username, password) => {
    try {
      await loginRequest(username, password);
      const user = await getCurrentUser();
      setUser(user);
      setError(null);
      return true;
    } catch{
      setError("Invalid credentials");
      return false;
    }
  };

  const logout = async () => {
    await logoutRequest();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, error, login, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
};
export default AuthContext;