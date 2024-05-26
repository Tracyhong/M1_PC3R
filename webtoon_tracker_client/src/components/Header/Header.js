import React, { useState, useContext, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {useMainContext} from '../../utils/Contexts';
import './Header.css';
import FavoriteWebtoonsModal from '../WebtoonComponent/FavoriteWebtoonModal';

const Header = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const navigate = useNavigate();
    const { userData, logout, webtoonsContext } = useMainContext();
    const [showHeader, setShowHeader] = useState(true);
    const [showSubNav, setShowSubNav] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleSearchSubmit = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            navigate(`/search?q=${searchQuery}`);
        }
    };

    const handleProfileMouseEnter = () => {
        setShowSubNav(true);
    };

    const handleProfileMouseLeave = () => {
        setShowSubNav(false);
    };

    const handleFavoritesClick = () => {
        console.log("webtoons in the context ----------------   : ", webtoonsContext);
        setIsModalOpen(true); // Open the modal

    };

    useEffect(() => {
        let lastScrollTop = 0;
        const handleScroll = () => {
            const currentScrollTop = window.pageYOffset || document.documentElement.scrollTop;
            if (currentScrollTop > lastScrollTop) {
                setShowHeader(false); // Scrolling down
            } else {
                setShowHeader(true); // Scrolling up
            }
            lastScrollTop = currentScrollTop <= 0 ? 0 : currentScrollTop; // For Mobile or negative scrolling
        };

        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

    return (
        <header className={`header ${showHeader ? 'header-show' : 'header-hide'}`}>
            <nav className="header-nav">
                <Link to="/" className="logo-link">
                    <h1 className={'logo-text'}>Webtoon Tracker</h1>
                </Link>
                <div className="end">
                    <Link to="/">Home</Link>
                    <Link to="/webtoons">All</Link>
                    <Link to="/randomWebtoon">Random</Link>
                    <form onSubmit={handleSearchSubmit} className="header-search-form">
                        <input
                            type="text"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            placeholder="Search for a webtoon"
                        />
                        <button type="submit">
                            <svg className="search-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path fill="none" d="M0 0h24v24H0V0z"/>
                                <path
                                    d="M15.5 14h-.79l-.28-.27a6.5 6.5 0 0 0 1.48-5.34c-.47-2.78-2.79-5-5.59-5.34a6.505 6.505 0 0 0-7.27 7.27c.34 2.8 2.56 5.12 5.34 5.59a6.5 6.5 0 0 0 5.34-1.48l.27.28v.79l4.25 4.25c.41.41 1.08.41 1.49 0 .41-.41.41-1.08 0-1.49L15.5 14zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
                            </svg>
                        </button>
                    </form>
                    <div className="profile-container" onMouseEnter={handleProfileMouseEnter}
                         onMouseLeave={handleProfileMouseLeave}>
                        {!userData ? (   // If user is not logged in
                            <Link to="/login">Login</Link>
                        ) : ( // If user is logged in
                            <div>
                                <Link to="/profile">Profile</Link>
                                {showSubNav && (
                                    <div className="sub-nav">
                                        <button onClick={logout}>Logout</button>
                                        <button onClick={handleFavoritesClick}>Favorite Webtoons</button>
                                    </div>
                                )}
                            </div>
                        )}
                    </div>
                </div>
            </nav>

            <FavoriteWebtoonsModal
                isOpen={isModalOpen}
                onRequestClose={() => setIsModalOpen(false)}
                webtoons={webtoonsContext}
            />
        </header>
    );
};

export default Header;
