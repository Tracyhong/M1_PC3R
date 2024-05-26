import React, { useContext, useEffect, useState } from 'react';
import { useMainContext } from "../utils/Contexts";
import Calendar from '../components/Calendar/Calendar';
import { fetchGet, fetchPost } from "../utils/api";
import WebtoonList from "../components/WebtoonComponent/WebtoonList";
import './css/ProfilePage.css';

const ProfilePage = () => {
    const { userData, logout, addWebtoon, webtoonsContext } = useMainContext();
    const [webtoonChapter, setWebtoonChapter] = useState([]);
    const [webtoonInfo, setWebtoonInfo] = useState([]);
    const [fetchedWebtoons, setFetchedWebtoons] = useState(new Set());

    const { user } = userData;

    useEffect(() => {
        const fetchDataChapter = async () => {
            try {
                const url = "calendrierServlet";
                const body = JSON.stringify({
                    webtoonIds: user.webtoonList
                });
                const data = await fetchPost(url, body);
                setWebtoonChapter(data);
            } catch (error) {
                console.error('Failed to fetch webtoons chapter:', error.message);
            }
        };

        const fetchDataInfo = async (webtoonId) => {
            try {
                const url = `webtoonServlet?id=${webtoonId}`;
                const data = await fetchGet(url);
                setWebtoonInfo(prevInfo => {
                    if (!prevInfo.some(item => item.id === data.id)) {
                        return [...prevInfo, data];
                    }
                    return prevInfo;
                });
                addWebtoon(data);
                setFetchedWebtoons(prevFetched => new Set(prevFetched).add(webtoonId));
            } catch (error) {
                console.error('Failed to fetch webtoon info:', error.message);
            }
        };

        fetchDataChapter();

        user.webtoonList.forEach(webtoonId => {
            if (!fetchedWebtoons.has(webtoonId)) {
                fetchDataInfo(webtoonId);
            }
        });
    }, [user.webtoonList, fetchedWebtoons]);

    if (!userData) {
        return <p>Please log in.</p>;
    }

    return (
        <div className="profile-page">
            <div className="profile-header">
                <h1>Welcome, {user.name}</h1>
                {/*<button onClick={logout}>Logout</button>*/}
            </div>
            <div className="calendar-section">
                <h3>Calendar to track all the chapters of your favorite webtoons</h3>
                <Calendar webtoonChapters={webtoonChapter} webtoons={webtoonInfo}/>
            </div>
            <div className="favorite-webtoons">
                <p>Favorite Webtoons:</p>
                <WebtoonList webtoons={webtoonInfo}/>
            </div>
        </div>
    );
};

export default ProfilePage;
