import React, { useState, useEffect } from 'react';
import { fetchGet } from '../utils/api';
import WebtoonList from '../components/WebtoonComponent/WebtoonList';
import { HashLoader } from 'react-spinners';
import { css } from '@emotion/react';
import './css/WebtoonListPage.css';
import prevIcon from '../assets/images/previousButton.png';
import nextIcon from '../assets/images/nextButton.png';

const WebtoonListPage = () => {
    const [webtoons, setWebtoons] = useState([]);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const fetchData = async (page) => {
            setIsLoading(true);
            try {
                const url = `webtoonServlet?get=all&page=${page}`;
                const data = await fetchGet(url);
                setWebtoons(data.webtoons);
                setTotalPages(data.totalPages);
                setError(null);
            } catch (error) {
                setError(error.message);
                setWebtoons([]);
            } finally {
                setIsLoading(false);
            }
        };
        fetchData(currentPage);
    }, [currentPage]);

    const handleNextPage = () => {
        setCurrentPage((prevPage) => Math.min(prevPage + 1, totalPages));
    };

    const handlePreviousPage = () => {
        setCurrentPage((prevPage) => Math.max(prevPage - 1, 1));
    };

    const loaderOverride = css`
        display: block;
        margin: 0 auto;
    `;

    return (
        <div className="webtoon-list-page">
            <h1>All Webtoons</h1>
            {error ? (
                <p>Error: {error}</p>
            ) : (
                <div>
                    {isLoading ? (
                        <div className="loader-container">
                            <HashLoader color={'#123abc'} loading={true} css={loaderOverride} size={60} />
                        </div>
                    ) : (
                        <div>
                            <WebtoonList webtoons={webtoons} />
                            <div className="pagination-buttons">
                                <button onClick={handlePreviousPage} disabled={currentPage === 1 || isLoading}>
                                    <img src={prevIcon} alt="Previous" />
                                </button>
                                <button onClick={handleNextPage} disabled={currentPage === totalPages || isLoading}>
                                    <img src={nextIcon} alt="Next" />
                                </button>
                            </div>
                        </div>
                    )}

                </div>
            )}
        </div>
    );
};

export default WebtoonListPage;
