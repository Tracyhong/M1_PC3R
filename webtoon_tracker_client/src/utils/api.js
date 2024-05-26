// src/services/api.js

const BASE_URL = 'http://localhost:8080/webtoonTracker_server';

export const fetchGet = async (url) => {
    try {
        const response = await fetch(`${BASE_URL}/${url}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        console.log("fetching data detail")
        const data = await response.json();
        console.log("api data detail : "+data);
        return data;
    } catch (error) {
        throw new Error(`Fetching webtoon detail failed: ${error.message}`);
    }
}

export const fetchPost = async (url, body) => {
    try {
        const response = await fetch(`${BASE_URL}/${url}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: body
        });
        console.log("fetching data");
        const data = await response.json();
        console.log(data);
        return data;
    } catch (error) {
        throw new Error(`Fetching register failed: ${error.message}`);
    }
}
