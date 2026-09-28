const movieList = document.getElementById("movie-list");
const movieForm = document.getElementById("movie-form");

function getMovies() {
    fetch("/api/movies")
        .then(response => response.json())
        .then(movies => {
            movieList.innerHTML = "";

            movies.forEach(movie => {
                const movieElement = document.createElement("div");

                movieElement.innerHTML = `
                    <h3>${movie.title}</h3>
                    <p>Genre: ${movie.genre}</p>
                    <p>Aldersgrænse: ${movie.ageLimit}</p>
                `;

                movieList.appendChild(movieElement);
            });
        })
        .catch(error => {
            console.error("Fejl ved hentning af film:", error);
        });
}

movieForm.addEventListener("submit", function(event) {
    event.preventDefault();

    const movie = {
        title: document.getElementById("title").value,
        genre: document.getElementById("genre").value,
        ageLimit: Number(document.getElementById("ageLimit").value)
    };

    fetch("/api/movies", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(movie)
    })
        .then(response => response.json())
        .then(() => {
            movieForm.reset();
            getMovies();
        })
        .catch(error => {
            console.error("Fejl ved oprettelse af film:", error);
        });
});

getMovies();