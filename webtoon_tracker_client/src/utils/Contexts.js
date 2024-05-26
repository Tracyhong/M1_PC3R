import React, { createContext, useState, useEffect, useContext } from 'react';

// Create the main context
export const MainContext = createContext(null);

// Create a provider component
export const MainProvider = ({ children }) => {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedUserData = JSON.parse(localStorage.getItem('user'));
        if (storedUserData) {
            setUserData(storedUserData);
        }
        setLoading(false);
    }, []);

    const login = (userData) => {
        setUserData(userData);
        localStorage.setItem('user', JSON.stringify(userData));
    };

    const logout = () => {
        setUserData(null);
        localStorage.removeItem('user');
        setWebtoons([]);
    };

    // Webtoon state
    const [webtoonsContext, setWebtoons] = useState([]);

    const addWebtoon = (webtoon) => {
        setWebtoons(prevWebtoons => {
            if (!prevWebtoons.some(existingWebtoon => existingWebtoon.id === webtoon.id)) {
                return [...prevWebtoons, webtoon];
            }
            return prevWebtoons;
        });
    };
    const removeWebtoon = (webtoonId) => {
        setWebtoons(prevWebtoons => prevWebtoons.filter(webtoon => webtoon.id !== webtoonId));
    }

    return (
        <MainContext.Provider value={{ userData, login, logout, webtoonsContext, addWebtoon, loading, removeWebtoon }}>
            {children}
        </MainContext.Provider>
    );
};

// Custom hook to use the context
export const useMainContext = () => useContext(MainContext);
