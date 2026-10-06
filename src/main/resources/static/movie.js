
const urlMovies = "/api/movies";
const movieTableBody = document.getElementById("movieTableBody");
const submitMovie = document.getElementById("submitMovie");

let movieToEditId = null;

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
    movieTableBody.innerHTML = "";
    movies.forEach(movieTable);
}

fetchMovies();

// Indsætter film i table

function movieTable(movie) {
    const row = movieTableBody.insertRow();

    row.insertCell().innerHTML = movie.title;
    row.insertCell().innerHTML = movie.genre;
    row.insertCell().innerHTML = movie.duration + " min.";
    row.insertCell().innerHTML = movie.ageLimit;


    // Button til at redigere film
    const buttonEdit = document.createElement("button");
    buttonEdit.textContent = "Rediger";
    row.insertCell().appendChild(buttonEdit);

    buttonEdit.addEventListener("click", () => {
        document.getElementById("title").value = movie.title;
        document.getElementById("genre").value = movie.genre;
        document.getElementById("ageLimit").value = movie.ageLimit;
        document.getElementById("duration").value = movie.duration;
        movieToEditId = movie.id;
        submitMovie.textContent = "Gem ændringer";
    });


    // Button til at slette film
    const buttonDelete = document.createElement("button");
    buttonDelete.textContent = "Slet";
    row.insertCell().appendChild(buttonDelete);

    buttonDelete.addEventListener("click", async () => {
        await deleteMovie(movie.id);
        row.remove();

        // Tjekker om filmen er igang med at blive redigeret
        if (movieToEditId === movie.id) {
            movieToEditId = null;
            formMovie.reset();
            submitMovie.textContent = "Opret";
        }
    });
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

    if (movieToEditId !== null) {
        await sendObjectAsJson(urlMovies + "/edit/" + movieToEditId, movie, "PUT");
        movieToEditId = null;
    } else {
        await sendObjectAsJson(url, movie, "POST");
    }

    form.reset();
    submitMovie.textContent = "Opret";
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

