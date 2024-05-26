import React from 'react';
import './css/WebtoonUpdate.css';
import {Link} from "react-router-dom";

const WebtoonUpdate = ({ update }) => {
    return (
        <div className="update-container">
            <Link to={`/webtoon/${update.webtoonId}`} className="update-link">
                <img src={update.image} alt='Cover image' className="update-image" />
                <div className="update-details">
                    <p className="update-title" title={update.title}>{update.title}</p>

                    <p className="update-chapter">Ch.{update.chapter}</p>
                </div>
            </Link>
        </div>
    );
};

export default WebtoonUpdate;
