import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Navigator from './pages/common/Navigator';

export default function App() {
  return (
    <BrowserRouter>
      <Navigator />
      <Routes>
        <Route></Route>
      </Routes>
    </BrowserRouter>
  );
}