import React, {useState, useCallback, useEffect} from 'react';
import { Calendar as BigCalendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';

const localizer = momentLocalizer(moment);

const Calendar = ({webtoonChapters, webtoons}) => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        const chaptersArray = Array.isArray(webtoonChapters) ? webtoonChapters : [];

        const newEvents = chaptersArray.map(webtoonChapter => {
            const webtoon = webtoons.find(w => w.id === webtoonChapter.webtoonId);
            const title = webtoon ? webtoon.title : 'Unknown Webtoon';

            return {
                title: `${title} Ch.${webtoonChapter.chapter}`,
                start: new Date(webtoonChapter.updatedAt),
                end: new Date(webtoonChapter.updatedAt),
                desc: `Update: ${title} Ch.${webtoonChapter.chapter}`,
            };
        });

        // Predict the next two chapters
        webtoons.forEach(webtoon => {
            const latestChapter = chaptersArray
                .filter(chapter => chapter.webtoonId === webtoon.id)
                .sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt))[0];

            if (latestChapter) {
                const latestChapterNumber = parseInt(latestChapter.chapter, 10);

                for (let i = 1; i <= 2; i++) {
                    const nextUpdateDate = new Date(latestChapter.updatedAt);
                    nextUpdateDate.setDate(nextUpdateDate.getDate() + (7 * i)); // assuming weekly updates

                    newEvents.push({
                        title: `${webtoon.title} Ch.${latestChapterNumber + i}`,
                        start: nextUpdateDate,
                        end: nextUpdateDate,
                        desc: `Predicted Update: ${webtoon.title} Ch.${latestChapterNumber + i}`,
                    });
                }
            }
        });

        setEvents(newEvents);
    }, [webtoonChapters, webtoons]);


    // show event details in a window
    const handleSelectEvent = useCallback(
        (event) => window.alert(
            event.desc
        ),[]
    );

    const eventStyleGetter = (event, start, end, isSelected) => {
        const backgroundColor = event.color;
        const style = {
            backgroundColor,
            borderRadius: '5px',
            opacity: 0.8,
            color: 'white',
            border: '0px',
            display: 'block'
        };
        return {
            style: style
        };
    };

    return (
        <div>
            <BigCalendar
                localizer={localizer}
                events={events}
                startAccessor="start"
                endAccessor="end"
                style={{ /*width: 900 ,*/height: 550, margin: '50px'}}
                onSelectEvent={handleSelectEvent}
                eventPropGetter={eventStyleGetter}
            />
        </div>
    );
};

export default Calendar;