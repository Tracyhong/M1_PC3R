// src/components/BackToTop/BackToTop.js
import React, { useState, useEffect } from 'react';
import './ButtonTop.css';

const ButtonTop = () => {
    const [isVisible, setIsVisible] = useState(false);

    const toggleVisibility = () => {
        if (window.pageYOffset > 300) {
            setIsVisible(true);
        } else {
            setIsVisible(false);
        }
    };

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    useEffect(() => {
        window.addEventListener('scroll', toggleVisibility);
        return () => {
            window.removeEventListener('scroll', toggleVisibility);
        };
    }, []);

    return (
        <div className="back-to-top">
            {isVisible &&
                <button onClick={scrollToTop}>
                    &#8679;
                </button>
            }
        </div>
    );
};

export default ButtonTop;
