
const urlMovies = "/api/movies";
const showMovies = document.getElementById("showMovies");

async function fetchAnyUrl(url){
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}


// Henter film
async function fetchMovies() {
    const movies = await fetchAnyUrl(urlMovies);
    movies.forEach(movieTable);
}

fetchMovies();

// Indsætter film i table

function movieTable(movie) {
    const row = showMovies.insertRow();

    row.insertCell().innerHTML = movie.title;
    row.insertCell().innerHTML = movie.genre;
    row.insertCell().innerHTML = movie.duration + " min.";
    row.insertCell().innerHTML = movie.ageLimit;
}


// Gemmer film ved opret

const formMovie = document.getElementById("addmovie");
formMovie.addEventListener("submit", saveMovie);


async function sendObjectAsJson(url, object, method) {
    // Laver objekt til json
    const json = JSON.stringify(object);

    const fetchOptions = {
        // POST vælger @PostMapping mens PUT vælger @PutMapping
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: json,
    };
    // Sender til url
    const response = await fetch(url, fetchOptions);

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }

    // Vi returnerer det gemte objekt fra databasen
    return response.json();
}

async function saveMovie(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const url = form.action;

    const formData = new FormData(form);
    const movie = Object.fromEntries(formData.entries());

    const savedMovie = await sendObjectAsJson(url, movie, "POST");
    movieTable(savedMovie);
    form.reset();
}