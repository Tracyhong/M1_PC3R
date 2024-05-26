import React from 'react';
import { Link } from 'react-router-dom';
import './css/WebtoonTop.css';

const WebtoonTop = ({ webtoonTops }) => {
    return (
        <div className="webtoon-top-container">
            <ol className="webtoon-top">
                {webtoonTops.map((webtoon, index) => (
                    <div key={webtoon.webtoonId} className="webtoon-top-item-wrapper">
                        <li className="webtoon-top-item">
                            <Link to={`/webtoon/${webtoon.webtoonId}`} className="webtoon-top-link">
                                {index + 1}. {webtoon.title}
                            </Link>
                        </li>
                    </div>
                ))}
            </ol>
        </div>
    );
};

export default WebtoonTop;
