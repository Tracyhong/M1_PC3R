import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { fetchGet } from "../utils/api";
import WebtoonList from "../components/WebtoonComponent/WebtoonList";
import { HashLoader } from 'react-spinners';
import './css/SearchResultsPage.css';

const useQuery = () => {
    return new URLSearchParams(useLocation().search);
};

const SearchResultsPage = () => {
    const query = useQuery();
    const searchQuery = query.get('q');
    const [searchResults, setSearchResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchSearchResults = async () => {
            setLoading(true);
            setError('');
            try {
                const url = `webtoonServlet?get=search&query=${searchQuery}`;
                const data = await fetchGet(url);
                setSearchResults(data);
            } catch (err) {
                setError('Failed to fetch search results');
            } finally {
                setLoading(false);
            }
        };

        if (searchQuery) {
            fetchSearchResults();
        }
    }, [searchQuery]);

    return (
        <div className="search-results-container">
            <h1 className="search-results-title">Search results for "{searchQuery}"</h1>
            {loading && (
                <div className="loading-container">
                    <HashLoader color={'#123abc'} loading={true} size={50} />
                </div>
            )}
            {error ? (
                <p>Error: <span style={{ color: 'red' }}>{error}</span></p>
            ) : (
                <div>
                    <WebtoonList webtoons={searchResults} />
                </div>
            )}
        </div>
    );
};

export default SearchResultsPage;
