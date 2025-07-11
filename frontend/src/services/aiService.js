import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/ai',
  withCredentials: true,
});

export const askQuestion = async (question) => {
  const response = await API.post('/ask', { question });
  return response.data.answer;
};
