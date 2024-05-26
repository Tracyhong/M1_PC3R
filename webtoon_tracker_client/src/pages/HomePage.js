import React, { useEffect, useState } from 'react';
import { fetchGet } from "../utils/api";
import { css } from '@emotion/react';
import { SyncLoader, HashLoader } from 'react-spinners';
import WebtoonUpdate from "../components/WebtoonComponent/WebtoonUpdate";
import WebtoonTop from "../components/WebtoonComponent/WebtoonTop";
import WebtoonCarousel from "../components/WebtoonComponent/WebtoonCarousel";
import './css/HomePage.css';
import prevIcon from '../assets/images/previousButton.png';
import nextIcon from '../assets/images/nextButton.png';

const HomePage = () => {
    const [webtoonBaniere, setWebtoonBaniere] = useState([]);
    const [errorBaniere, setErrorBaniere] = useState(null);
    const [isLoadingBaniere, setIsLoadingBaniere] = useState(true);

    const [updates, setUpdates] = useState([]);
    const [errorUpdate, setErrorUpdate] = useState(null);
    const [isLoadingUpdate, setIsLoadingUpdate] = useState(true);

    const [tops, setTops] = useState([]);
    const [errorTop, setErrorTop] = useState(null);
    const [isLoadingTop, setIsLoadingTop] = useState(true);

    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        const fetchDataBaniere = async () => {
            try {
                const limit = 3;
                const data = await fetchGet(`webtoonServlet?get=baniere&limit=${limit}`);
                setWebtoonBaniere(data);
                setErrorBaniere(null);
            } catch (error) {
                setErrorBaniere(error.message);
                setWebtoonBaniere([]);
            } finally {
                setIsLoadingBaniere(false);
            }
        };

        const fetchDataTop = async () => {
            try {
                const limit = 10;
                const data = await fetchGet(`webtoonServlet?get=top&limit=${limit}`);
                setTops(data);
                setErrorTop(null);
            } catch (error) {
                setErrorTop(error.message);
                setTops([]);
            } finally {
                setIsLoadingTop(false);
            }
        };

        fetchDataBaniere();
        fetchDataTop();
    }, []);

    useEffect(() => {
        const fetchDataUpdate = async (page) => {
            try {
                const limit = 5;
                const data = await fetchGet(`webtoonServlet?get=lastUpdates&limit=${limit}&page=${page}`);
                setUpdates(data.webtoons);
                setTotalPages(data.totalPages);
                setErrorUpdate(null);
            } catch (error) {
                setErrorUpdate(error.message);
                setUpdates([]);
            } finally {
                setIsLoadingUpdate(false);
            }
        };

        fetchDataUpdate(currentPage);
    }, [currentPage]);

    const handleNextPage = () => {
        setCurrentPage((prevPage) => Math.min(prevPage + 1, totalPages));
    };

    const handlePreviousPage = () => {
        setCurrentPage((prevPage) => Math.max(prevPage - 1, 1));
    };

    const override = css`
        display: block;
        margin: 0 auto;
        border-color: red;
    `;

    return (
        <div className="homepage-container">
            <div className={`loader-container ${isLoadingBaniere || isLoadingUpdate || isLoadingTop ? '' : 'hidden'}`}>
                <HashLoader color={'#123abc'} loading={isLoadingBaniere || isLoadingUpdate || isLoadingTop}
                            css={override} size={100}/>
            </div>

            {isLoadingBaniere || isLoadingUpdate || isLoadingTop ? null : (
                <>
                    <SyncLoader color={'#123abc'} loading={isLoadingBaniere} size={15} css={override}/>
                    {errorBaniere ? (
                        <p>Error: {errorBaniere}</p>
                    ) : (
                        <WebtoonCarousel webtoonBaniere={webtoonBaniere}/>
                    )}
                    <div className="list-container">
                        <div className="updates-container">
                            <SyncLoader color={'#123abc'} loading={isLoadingUpdate} size={15} css={override}/>
                            {errorUpdate ? (
                                <p>Error: {errorUpdate}</p>
                            ) : (
                                <div>
                                    <div className="updates-header">
                                        <h2>Latest Updates</h2>
                                        <div className="pagination-buttons">
                                            <button onClick={handlePreviousPage}
                                                    disabled={currentPage === 1 || isLoadingUpdate}>
                                                <img src={prevIcon} alt="Previous" />
                                            </button>
                                            <button onClick={handleNextPage}
                                                    disabled={currentPage === totalPages || isLoadingUpdate}>
                                                <img src={nextIcon} alt="Next" />
                                            </button>
                                        </div>
                                    </div>
                                    <div className="updates-content">
                                        {updates.map(update => (
                                            <WebtoonUpdate key={update.id} update={update}/>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                        <div className="top-container">
                            <SyncLoader color={'#123abc'} loading={isLoadingTop} size={15} css={override}/>
                            {errorTop ? (
                                <p>Error: {errorTop}</p>
                            ) : (
                                <div>
                                    <h2>Top 10 webtoons</h2>
                                    <div className="top-content">
                                        <WebtoonTop webtoonTops={tops}/>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default HomePage;
