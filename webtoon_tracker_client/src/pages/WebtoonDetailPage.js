import React, {useEffect, useState} from 'react';
import { useParams } from 'react-router-dom';
import WebtoonDetail from "../components/WebtoonComponent/WebtoonDetail";
import {fetchGet} from "../utils/api";

const WebtoonDetailPage = () => {
    const { id } = useParams();

    const [webtoon, setWebtoon] = useState(null);
    const [error, setError] = useState(null);

    // get webtoon details from server using id and set it to webtoon
    useEffect(() => {

        const fetchData = async () => {
            try {
                const url = "webtoonServlet?id="+id;
                const data = await fetchGet(url);
                console.log("data to set : "+data);
                setWebtoon(data);
                setError(null);
                console.log("setWebtoon" + webtoon)
            } catch (error) {
                setError(error.message);
                setWebtoon(null);
            }
        };
        fetchData().then(r => console.log("setWebtoon fetch" + webtoon));
    }, []);

    return (
        <div>
            {error ? (
                <p>Error: {error}</p>
            ) : (
                <div>
                    {webtoon ? (
                        <WebtoonDetail webtoon={webtoon} />
                    ) : (
                        <p>Loading...</p>
                    )
                    }
                </div>
            )}

        </div>
    );
};

export default WebtoonDetailPage;
