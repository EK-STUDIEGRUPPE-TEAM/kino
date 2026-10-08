const urlMovies = "/api/movies";
const movieTableBody = document.getElementById("movieTableBody");
const submitMovie = document.getElementById("submitMovie");
const formMovie = document.getElementById("addmovie");
const movieDialog = document.getElementById("movieDialog");
const movieDialogTitle = document.getElementById("movieDialogTitle");

let movieToEditId = null;

async function fetchAnyUrl(url) {
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`);
    }

    return response.json();
}


// Henter film
async function fetchMovies() {
    const movies = await fetchAnyUrl(urlMovies);
    movieTableBody.innerHTML = "";
    movies.forEach(insertMovieRow);
}

fetchMovies();


// Konverterer minutter til timer
function formatDuration(minutes) {
    const hours = Math.trunc(minutes / 60);
    const min = minutes % 60;

    // Hvis den er hele timer så vises der ikke minutter
    if (min === 0) {
        return hours + " t";
    }

    return hours + " t " + min + " min";
}

//Konverterer Genre ENUM's til danske genrenavne
function formatGenre(genre) {
    return document.querySelector(`#genre option[value="${genre}"]`).textContent;
}


// Indsætter film i table

function insertMovieRow(movie) {
    const row = movieTableBody.insertRow();

    row.insertCell().innerHTML = movie.title;
    row.insertCell().innerHTML = formatGenre(movie.genre);
    row.insertCell().innerHTML = movie.ageLimit + " år";
    row.insertCell().innerHTML = formatDuration(movie.duration);

    // Begge buttons kan være i en enkel celle (handlinger)
    const actionCell = row.insertCell();
    actionCell.classList.add("action-cell");

    // Knap til at vælge forestilling for filmen
    const buttonShowings = document.createElement("button");

    buttonShowings.textContent = "Vælg forestilling";
    buttonShowings.classList.add("showing-button");

    actionCell.appendChild(buttonShowings);


// Sender brugeren videre til filmens forestillinger
    buttonShowings.addEventListener("click", () => {

        window.location.href =
            "movie-showings.html?movieId=" + movie.id;

    });

    // Knap til at redigere film
    const buttonEdit = document.createElement("button");
    buttonEdit.textContent = "Rediger";
    buttonEdit.classList.add("edit-button");
    actionCell.appendChild(buttonEdit);

    buttonEdit.addEventListener("click", () => {
        document.getElementById("title").value = movie.title;
        document.getElementById("genre").value = movie.genre;
        document.getElementById("ageLimit").value = movie.ageLimit;
        document.getElementById("duration").value = movie.duration;
        movieToEditId = movie.id;
        submitMovie.textContent = "Gem ændringer";
        movieDialogTitle.textContent = "Rediger film";

        //Åbner vores dialog pop up vindue
        movieDialog.showModal();
    });


    // Knap til at slette film
    const buttonDelete = document.createElement("button");
    buttonDelete.textContent = "Slet";
    buttonDelete.classList.add("delete-button");
    actionCell.appendChild(buttonDelete);

    buttonDelete.addEventListener("click", async () => {
        await deleteMovie(movie.id);
        row.remove();
    });
}


// Nulstiller vores popup og åbner når der trykkes tilføj film
document.getElementById("openMovieDialog").addEventListener("click", () => {
    formMovie.reset();
    movieToEditId = null;
    movieDialogTitle.textContent = "Tilføj film";
    submitMovie.textContent = "Opret";

    //Åbner vores dialog pop up vindue
    movieDialog.showModal();
});

// Lukker vores popup når man trykker annuller
document.getElementById("cancelMovie").addEventListener("click", () => {
    movieDialog.close();
});


// Gemmer film ved opret eller rediger
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

    if (movieToEditId !== null) {
        await sendObjectAsJson(urlMovies + "/edit/" + movieToEditId, movie, "PUT");
        movieToEditId = null;
    } else {
        await sendObjectAsJson(url, movie, "POST");
    }

    form.reset();
    submitMovie.textContent = "Opret";

    // Lukker popup når filmen bliver gemt
    movieDialog.close();

    fetchMovies();
}


// Sletter en film
async function deleteMovie(id) {
    const response = await fetch(urlMovies + "/delete/" + id, {
        method: "DELETE"
    });

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }
}

