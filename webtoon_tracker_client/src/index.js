import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { MainProvider } from './utils/Contexts';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
      <MainProvider>
         <App />
      </MainProvider>
  </React.StrictMode>
);

reportWebVitals();
