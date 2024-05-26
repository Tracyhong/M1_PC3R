import React from 'react';
import { Link } from 'react-router-dom';
import './css/WebtoonCard.css';

const WebtoonCard = ({ webtoon }) => {
    const maxDescriptionLength = 200;
    const truncatedDescription = webtoon.description.length > maxDescriptionLength
        ? `${webtoon.description.substring(0, maxDescriptionLength)}...`
        : webtoon.description;

    return (
        <div className="webtoon-card">
            <img src={webtoon.image} alt='Cover image' className="webtoon-image" />
            <div className="webtoon-info">
                <h2 className="webtoon-title">{webtoon.title}</h2>
                <p className="webtoon-description">{truncatedDescription}</p>
                <Link to={`/webtoon/${webtoon.id}`} className="webtoon-link">View Details</Link>
            </div>
        </div>
    );
};

export default WebtoonCard;
