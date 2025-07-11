import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true
});

export const loginRequest = async (username, password) => {
  await API.post('/api/auth/signin', { username, password });
  return getCurrentUser();
};

export const logoutRequest = async () => {
  await API.post('/api/auth/signout');
};

export const getCurrentUser = async () => {
  const response = await API.get('/api/auth/user');
  return response.data;
};
export const signupRequest = async (userData) => {
  await API.post('/api/auth/signup', userData);
};