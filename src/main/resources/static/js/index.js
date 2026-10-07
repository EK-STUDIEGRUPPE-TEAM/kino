const movieList =
    document.getElementById("movie-list");

const featuredTitle =
    document.getElementById("featured-title");

const featuredInformation =
    document.getElementById("featured-information");

const featuredLetter =
    document.getElementById("featured-letter");



/* ---------- GET MOVIES ---------- */

function getMovies() {

    fetch("/api/movies")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Kunne ikke hente film"
                );

            }

            return response.json();

        })

        .then(movies => {

            showFeaturedMovie(movies);

            showMovies(movies);

        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af film:",
                error
            );


            movieList.innerHTML = `
                <p class="empty-message">
                    Filmene kunne ikke hentes.
                </p>
            `;

        });

}



/* ---------- FEATURED MOVIE ---------- */

function showFeaturedMovie(movies) {

    if (movies.length === 0) {

        featuredTitle.textContent = "KINO";

        featuredInformation.textContent =
            "Ingen aktuelle film";

        featuredLetter.textContent = "K";

        return;

    }


    const movie = movies[0];


    featuredTitle.textContent =
        movie.title;


    featuredLetter.textContent =
        movie.title.charAt(0).toUpperCase();


    featuredInformation.innerHTML = `

        <span>
            ${formatGenre(movie.genre)}
        </span>

        <span class="information-separator">
            •
        </span>

        <span>
            ${formatDuration(movie.duration)}
        </span>

        <span class="information-separator">
            •
        </span>

        <span>
            ${movie.ageLimit} år
        </span>

    `;

}



/* ---------- MOVIE LIST ---------- */

function showMovies(movies) {

    movieList.innerHTML = "";


    if (movies.length === 0) {

        movieList.innerHTML = `

            <p class="empty-message">
                Der er ingen aktuelle film.
            </p>

        `;

        return;

    }


    movies.forEach(movie => {

        const movieCard =
            document.createElement("article");


        movieCard.classList.add(
            "customer-movie-card"
        );


        movieCard.innerHTML = `

            <div class="movie-poster">

                ${movie.title.charAt(0).toUpperCase()}

            </div>


            <h3>
                ${movie.title}
            </h3>


            <p>
                ${formatGenre(movie.genre)}
            </p>


            <p>
                ${formatDuration(movie.duration)}
                ·
                ${movie.ageLimit} år
            </p>

        `;


        movieList.appendChild(movieCard);

    });

}



/* ---------- FORMAT DURATION ---------- */

function formatDuration(minutes) {

    const hours =
        Math.trunc(minutes / 60);

    const remainingMinutes =
        minutes % 60;


    if (remainingMinutes === 0) {

        return hours + " t";

    }


    return (
        hours +
        " t " +
        remainingMinutes +
        " min"
    );

}



/* ---------- FORMAT GENRE ---------- */

function formatGenre(genre) {

    if (genre === "SCIFI") {
        return "Sci-fi";
    }

    if (genre === "DRAMA") {
        return "Drama";
    }

    if (genre === "ACTION") {
        return "Action";
    }

    if (genre === "ROMANCE") {
        return "Romantisk";
    }

    if (genre === "HORROR") {
        return "Gyser";
    }

    if (genre === "THRILLER") {
        return "Thriller";
    }

    if (genre === "ANIMATION") {
        return "Animation";
    }

    if (genre === "COMEDY") {
        return "Komedie";
    }


    return genre;

}



/* ---------- START ---------- */

getMovies();