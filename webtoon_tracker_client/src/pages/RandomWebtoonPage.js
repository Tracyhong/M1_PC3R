import React, { useState, useEffect } from 'react';
import { fetchGet } from '../utils/api';
import { useNavigate } from 'react-router-dom';
import { HashLoader} from 'react-spinners';
import { css } from '@emotion/react';
import './css/RandomWebtoonPage.css';

const RandomWebtoonPage = () => {
    const [webtoon, setWebtoon] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const url = "webtoonServlet?get=random";
                const data = await fetchGet(url);
                console.log(data);
                setWebtoon(data);
                setError(null);
            } catch (error) {
                setError(error.message);
                setWebtoon(null);
            }
        };
        fetchData();
    }, []);

    useEffect(() => {
        if (webtoon) {
            console.log("-------id : " + webtoon.id);
            navigate(`/webtoon/${webtoon.id}`);
        }
    }, [webtoon, navigate]); // Navigate when webtoon state is updated

    const override = css`
        display: block;
        margin: 0 auto;
        border-color: red;
    `;

    return (
        <div className="random-webtoon-page">
            <h1>Random Webtoon</h1>
            {error ? (
                <p>Error: {error}</p>
            ) : (
                <div className="loader-container">
                    <HashLoader color={'#123abc'} loading={true} css={override} size={60} />
                </div>
            )}
        </div>
    );
};

export default RandomWebtoonPage;
