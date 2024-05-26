import React from 'react';
import Modal from 'react-modal';
import WebtoonList from './WebtoonList';
import './css/FavoriteWebtoonModal.css';

const FavoriteWebtoonsModal = ({ isOpen, onRequestClose, webtoons }) => {

    console.log("webtoons in the modal ----------------   : ", webtoons);
    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            className="favorite-webtoons-modal"
            overlayClassName="favorite-webtoons-modal-overlay"
        >
            <h2>Favorite Webtoons</h2>

            <WebtoonList webtoons={webtoons}/>
            <button className="close-button" onClick={onRequestClose}>X</button>

        </Modal>
    );
};

export default FavoriteWebtoonsModal;
