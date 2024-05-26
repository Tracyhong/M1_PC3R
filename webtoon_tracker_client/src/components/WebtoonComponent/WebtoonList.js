import React from "react";
import WebtoonCard from "./WebtoonCard";
import './css/WebtoonList.css';

const WebtoonList = ({ webtoons }) => {
    return (
        <div className="webtoon-list">
            {webtoons.length > 0 ? (
                webtoons.map((webtoon) => (
                    <WebtoonCard key={webtoon.id} webtoon={webtoon} />
                ))
            ) : (
                <p>No webtoons found.</p>
            )}
        </div>
    );
};

export default WebtoonList;
