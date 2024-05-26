import React from 'react';
import { Carousel } from 'react-responsive-carousel';
import { Link } from 'react-router-dom';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
const WebtoonCarousel = ({ webtoonBaniere }) => {
    return (
        <div className="carousel-container">
            <Carousel
                showStatus={false}
                showThumbs={false}
                centerMode={true}
                centerSlidePercentage={100}
                infiniteLoop={true}
                autoPlay={true}
                interval={6000}
                stopOnHover={false}
                className="custom-carousel"
            >
                {webtoonBaniere.map((webtoon) => (
                    <div key={webtoon.id} style={{ height: '550px' }}>
                        <div className="image-container" >
                            <img src={webtoon.image} alt={webtoon.title} className="image" />
                            <Link to={`/webtoon/${webtoon.id}`} className="legend-container">
                                <h2 className="legend">{webtoon.title}</h2>
                            </Link>
                        </div>
                    </div>
                ))}
            </Carousel>
        </div>
    );
};

export default WebtoonCarousel;
