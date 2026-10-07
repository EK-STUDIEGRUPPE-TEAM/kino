/*
    Finder movieId i adressen.

    Eksempel:
    movie-showings.html?movieId=1
*/
const params =
    new URLSearchParams(window.location.search);

const movieId =
    Number(params.get("movieId"));


/* Finder elementerne i HTML */
const movieTitle =
    document.getElementById("movie-title");

const movieDetails =
    document.getElementById("movie-details");

const datesContainer =
    document.getElementById("dates");

const showtimesContainer =
    document.getElementById("showtimes");

const selectedShowingText =
    document.getElementById("selected-showing");


/*
    Her gemmer vi filmens forestillinger,
    når de er hentet fra backend.
*/
let movieShowings = [];


/* Gør minutter til timer og minutter */
function formatDuration(minutes) {

    const hours =
        Math.floor(minutes / 60);

    const remainingMinutes =
        minutes % 60;

    if (remainingMinutes === 0) {
        return hours + " t";
    }

    return hours
        + " t "
        + remainingMinutes
        + " min";
}


/* Gør ENUM-genrer lidt pænere */
function formatGenre(genre) {

    if (genre === "SCIFI") {
        return "Science Fiction";
    }

    return genre;
}


/*
    Henter den valgte film
    fra vores REST API.
*/
function getMovie() {

    fetch("/api/movies/" + movieId)

        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Kunne ikke hente filmen"
                );
            }

            return response.json();
        })

        .then(movie => {

            movieTitle.textContent =
                movie.title;

            movieDetails.textContent =
                formatGenre(movie.genre)
                + " | "
                + formatDuration(movie.duration)
                + " | "
                + movie.ageLimit
                + " år";
        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af film:",
                error
            );

        });
}


/*
    Henter kommende forestillinger
    fra backend.
*/
function getShowings() {

    fetch("/api/showings/upcoming")

        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Kunne ikke hente forestillinger"
                );
            }

            return response.json();
        })

        .then(showings => {

            /*
                Vi har hentet alle kommende
                forestillinger.

                Her beholder vi kun dem,
                der tilhører den valgte film.
            */
            movieShowings =
                showings.filter(showing =>
                    showing.movie.id === movieId
                );


            createDateButtons();
        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af forestillinger:",
                error
            );

        });
}


/*
    Laver en knap for hver dato,
    filmen har en forestilling.
*/
function createDateButtons() {

    datesContainer.innerHTML = "";


    if (movieShowings.length === 0) {

        showtimesContainer.innerHTML =
            "<p>Der er ingen kommende forestillinger.</p>";

        return;
    }


    /*
        Finder datoerne uden dubletter.
    */
    const dates = [
        ...new Set(
            movieShowings.map(showing =>
                showing.dateTime.substring(0, 10)
            )
        )
    ];


    dates.forEach((date, index) => {

        const button =
            document.createElement("button");

        button.className =
            "date-button";


        /*
            Den første dato er valgt,
            når siden åbnes.
        */
        if (index === 0) {
            button.classList.add("active");
        }


        const dateObject =
            new Date(date + "T12:00:00");


        const weekday =
            dateObject.toLocaleDateString(
                "da-DK",
                {
                    weekday: "short"
                }
            );


        const dateText =
            dateObject.toLocaleDateString(
                "da-DK",
                {
                    day: "numeric",
                    month: "short"
                }
            );


        button.innerHTML =
            weekday
            + "<span>"
            + dateText
            + "</span>";


        /*
            Når brugeren vælger dato,
            viser vi dens forestillinger.
        */
        button.addEventListener(
            "click",
            function () {

                document
                    .querySelectorAll(".date-button")
                    .forEach(button => {

                        button.classList.remove(
                            "active"
                        );

                    });


                button.classList.add(
                    "active"
                );


                showShowings(date);
            }
        );


        datesContainer.appendChild(button);
    });


    /*
        Viser forestillingerne
        for første dato.
    */
    showShowings(dates[0]);
}


/*
    Viser sale og tidspunkter
    for den valgte dato.
*/
function showShowings(date) {

    showtimesContainer.innerHTML = "";


    const showingsForDate =
        movieShowings.filter(showing =>
            showing.dateTime.startsWith(date)
        );


    /*
        Finder hvilke sale,
        der har forestillinger den dag.
    */
    const theatreNames = [
        ...new Set(
            showingsForDate.map(showing =>
                showing.theatre.name
            )
        )
    ];


    theatreNames.forEach(theatreName => {

        const theatre =
            document.createElement("div");

        theatre.className =
            "theatre";


        const title =
            document.createElement("h2");

        title.textContent =
            theatreName;


        theatre.appendChild(title);


        /*
            Finder kun forestillingerne
            i denne sal.
        */
        const theatreShowings =
            showingsForDate.filter(showing =>
                showing.theatre.name === theatreName
            );


        theatreShowings.forEach(showing => {

            const button =
                document.createElement("button");

            button.className =
                "showtime-button";


            /*
                Vi viser kun tidspunktet
                på knappen.
            */
            const showingDate =
                new Date(showing.dateTime);

            button.textContent =
                showingDate.toLocaleTimeString(
                    "da-DK",
                    {
                        hour: "2-digit",
                        minute: "2-digit"
                    }
                );


            /*
                Brugeren vælger
                en bestemt forestilling.
            */
            button.addEventListener(
                "click",
                function () {

                    document
                        .querySelectorAll(
                            ".showtime-button"
                        )
                        .forEach(button => {

                            button.classList.remove(
                                "selected"
                            );

                        });


                    button.classList.add(
                        "selected"
                    );


                    selectedShowingText.textContent =
                        "Valgt: "
                        + theatreName
                        + " - "
                        + button.textContent;


                    window.location.href =
                        "opret-reservation.html?showingId="
                        + showing.id;

                }
            );


            theatre.appendChild(button);
        });


        showtimesContainer.appendChild(
            theatre
        );
    });
}


/*
    Når siden åbner,
    henter vi data fra backend.
*/
getMovie();
getShowings();