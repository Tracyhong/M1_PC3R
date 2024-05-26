import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import ProfilePage from './pages/ProfilePage';
import WebtoonListPage from './pages/WebtoonListPage';
import WebtoonDetailPage from './pages/WebtoonDetailPage';
import Header from './components/Header/Header';
import Footer from './components/Footer/Footer';
import RandomWebtoonPage from './pages/RandomWebtoonPage';
import { MainProvider} from './utils/Contexts';
import SearchResultsPage from "./pages/SearchResultsPage";
import ProtectedRoute from './utils/ProtectedRoute';
import ButtonTop from "./components/ButtonTop/ButtonTop";
import './App.css';

function App() {
    return (
        <MainProvider>
            <Router>
                <div className="app-container">
                    <Header />
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route element={<ProtectedRoute />}>
                            <Route path="/profile" element={<ProfilePage />} />
                        </Route>
                        <Route path="/webtoons" element={<WebtoonListPage />} />
                        <Route path="/randomWebtoon" element={<RandomWebtoonPage />} />
                        <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />
                        <Route path="/search" element={<SearchResultsPage />} />
                    </Routes>
                    <Footer />
                    <ButtonTop />
                </div>
            </Router>
        </MainProvider>
    );
}

export default App;
