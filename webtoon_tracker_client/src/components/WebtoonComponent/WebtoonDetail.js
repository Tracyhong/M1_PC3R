import React, { useContext, useState } from 'react';
import {useMainContext} from "../../utils/Contexts";
import { fetchPost } from "../../utils/api";
import './css/WebtoonDetail.css';

const WebtoonDetail = ({ webtoon }) => {

    const { userData,removeWebtoon,addWebtoon } = useMainContext();
    const [error, setError] = useState('');
    const [msg, setMsg] = useState('');

    const addToFavorites = async () => {
        console.log("Add to favorites clicked");
        try {
            const url = `webtoonServlet`;
            const body = JSON.stringify({
                type: "add",
                userId: userData.user.id,
                webtoonId: webtoon.id
            });
            console.log("send body json : " + body)
            const response = await fetchPost(url, body);
            console.log('User data:', response);
            if (response.status === "ok") {
                setMsg(`Webtoon added to favorites!`);
                userData.user.webtoonList.push(webtoon.id); // Add webtoon to user data in context
                setError('');
                addWebtoon(webtoon); // Add webtoon to user data in context for the modal
            } else {
                setError(response.message);
            }
        } catch (error) {
            console.error('Add failed:', error.message);
            setError('Add failed: ' + error.message);
        }
    }

    const removeFromFavorites = async () => {
        console.log("Remove from favorites clicked");
        try {
            const url = `webtoonServlet`;
            const body = JSON.stringify({
                type: "delete",
                userId: userData.user.id,
                webtoonId: webtoon.id
            });
            console.log("send body json : " + body)
            const response = await fetchPost(url, body);
            console.log('User data:', response);
            if (response.status === "ok") {
                setMsg(`Webtoon removed from favorites!`);
                userData.user.webtoonList = userData.user.webtoonList.filter(id => id !== webtoon.id); // Remove webtoon from user data
                removeWebtoon(webtoon.id); // Remove webtoon from user data in context for the modal
                setError('');
            } else {
                setError(response.message);
            }
        } catch (error) {
            console.error('Remove failed:', error.message);
            setError('Remove failed: ' + error.message);
        }
    }

    const inFavorites = userData && userData.user && Array.isArray(userData.user.webtoonList) && userData.user.webtoonList.includes(webtoon.id);

    return (
        <div className="webtoon-detail">
            <div className="content">
                <div className="image-section">
                    <img src={webtoon.image} alt='Cover image' />
                    <div className="button-container">
                        {userData ? (
                            inFavorites ? (
                                <button onClick={removeFromFavorites}>Remove from Favorites</button>
                            ) : (
                                <button onClick={addToFavorites}>Add to Favorites</button>
                            )
                        ) : (
                            <p>Please log in to add to favorites.</p>
                        )}
                    </div>
                </div>
                <div className="info-container">
                    <h1>{webtoon.title}</h1>
                    <div className="info">

                        <h3>Alternative Titles:</h3>
                        <div className="array-section">
                            {webtoon.alternativeTitles.length > 0 ? (
                                webtoon.alternativeTitles.map((title, index) => (
                                    <p key={index}>{title}</p>
                                ))
                            ) : (
                                <p>No alternative titles found.</p>
                            )}
                        </div>

                        <div className="section">
                            <h3>Author:</h3>
                            {webtoon.author.length > 0 ? (
                                webtoon.author.map((authorName, index) => (
                                    <p key={index}>{authorName}</p>
                                ))
                            ) : (
                                <p>No author found.</p>
                            )}
                        </div>

                        <div className="section">
                            <h3>Artist:</h3>
                            <p>{webtoon.artist}</p>
                        </div>

                        <div className="section">
                            <h3>Status:</h3>
                            <p>{webtoon.year}, {webtoon.status}</p>
                        </div>

                        <div className="section">
                            <h3>Genres:</h3>
                            <div className="array-section">
                                {webtoon.tags.length > 0 ? (
                                    webtoon.tags.map((tag, index) => (
                                        <p key={index}>{tag.name}</p>
                                    ))
                                ) : (
                                    <p>No tags found.</p>
                                )}
                            </div>
                        </div>

                        <h3>Last Update:</h3>
                        <p>{webtoon.lastUpdate}</p>

                        {error && <p className="error">{error}</p>}
                        {msg && <p className="msg">{msg}</p>}
                    </div>
                </div>
            </div>
            <div className="section">
                <h3>Synopsis:</h3>
                <p>{webtoon.description}</p>
            </div>
        </div>
    );
};

export default WebtoonDetail;
