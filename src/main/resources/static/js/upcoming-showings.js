const showingTableBody = document.getElementById("showing-table-body");
const showingForm = document.getElementById("showing-form");

let editingShowingId = null;


// Henter kommende forestillinger
function getUpcomingShowings() {

    fetch("/api/showings/upcoming")
        .then(response => {

            if (!response.ok) {
                throw new Error("Kunne ikke hente forestillinger");
            }

            return response.json();
        })
        .then(showings => {

            showingTableBody.innerHTML = "";

            if (showings.length === 0) {

                const row = showingTableBody.insertRow();
                const cell = row.insertCell();

                cell.colSpan = 5;
                cell.textContent = "Ingen kommende forestillinger";

                return;
            }

            showings.forEach(showing => {

                const row = showingTableBody.insertRow();


                // Film
                const movieCell = row.insertCell();
                movieCell.textContent = showing.movie.title;


                // Dato
                const dateCell = row.insertCell();
                dateCell.textContent =
                    formatDate(showing.dateTime);


                // Tid
                const timeCell = row.insertCell();
                timeCell.textContent =
                    formatTime(showing.dateTime);


                // Sal
                const theatreCell = row.insertCell();
                theatreCell.textContent =
                    showing.theatre.name;


                // Handlinger
                const actionCell = row.insertCell();


                // Redigér-knap
                const editButton = document.createElement("button");

                editButton.type = "button";
                editButton.textContent = "Redigér";

                editButton.addEventListener("click", function () {
                    editShowing(showing);
                });

                actionCell.appendChild(editButton);


                // Slet-knap
                const deleteButton = document.createElement("button");

                deleteButton.type = "button";
                deleteButton.textContent = "Slet";

                deleteButton.addEventListener("click", function () {
                    deleteShowing(showing.id);
                });

                actionCell.appendChild(deleteButton);
            });

        })
        .catch(error => {

            console.error(
                "Fejl ved hentning af forestillinger:",
                error
            );

        });
}


// Henter alle film til dropdown
function getMovies() {

    fetch("/movies")
        .then(response => {

            if (!response.ok) {
                throw new Error("Kunne ikke hente film");
            }

            return response.json();
        })
        .then(movies => {

            const movieSelect =
                document.getElementById("movie-id");

            movies.forEach(movie => {

                const option =
                    document.createElement("option");

                option.value = movie.id;
                option.textContent = movie.title;

                movieSelect.appendChild(option);
            });

        })
        .catch(error => {

            console.error(
                "Fejl ved hentning af film:",
                error
            );

        });
}


// Henter alle sale til dropdown
function getTheatres() {

    fetch("/api/theatres")
        .then(response => {

            if (!response.ok) {
                throw new Error("Kunne ikke hente sale");
            }

            return response.json();
        })
        .then(theatres => {

            const theatreSelect =
                document.getElementById("theatre-id");

            theatres.forEach(theatre => {

                const option =
                    document.createElement("option");

                option.value = theatre.id;
                option.textContent = theatre.name;

                theatreSelect.appendChild(option);
            });

        })
        .catch(error => {

            console.error(
                "Fejl ved hentning af sale:",
                error
            );

        });
}


// Formaterer dato
function formatDate(dateTime) {

    const date = new Date(dateTime);

    return date.toLocaleDateString("da-DK");
}


// Formaterer tidspunkt
function formatTime(dateTime) {

    const date = new Date(dateTime);

    return date.toLocaleTimeString("da-DK", {
        hour: "2-digit",
        minute: "2-digit"
    });
}


// Fylder formularen med den forestilling,
// som brugeren vil redigere
function editShowing(showing) {

    editingShowingId = showing.id;

    document.getElementById("movie-id").value =
        showing.movie.id;

    document.getElementById("theatre-id").value =
        showing.theatre.id;

    document.getElementById("showing-date-time").value =
        showing.dateTime.substring(0, 16);

    const submitButton =
        showingForm.querySelector('button[type="submit"]');

    submitButton.textContent = "Gem ændringer";
}


// Sletter en forestilling
function deleteShowing(id) {

    fetch("/api/showings/" + id, {
        method: "DELETE"
    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Kunne ikke slette forestillingen");
            }

            getUpcomingShowings();
        })
        .catch(error => {

            console.error(
                "Fejl ved sletning af forestilling:",
                error
            );

        });
}


// Opretter eller redigerer en forestilling
showingForm.addEventListener("submit", function (event) {

    event.preventDefault();


    const showing = {

        dateTime:
        document.getElementById("showing-date-time").value,

        movie: {
            id: Number(
                document.getElementById("movie-id").value
            )
        },

        theatre: {
            id: Number(
                document.getElementById("theatre-id").value
            )
        }
    };


    // Hvis editingShowingId har en værdi,
    // redigerer vi en eksisterende forestilling
    if (editingShowingId !== null) {

        fetch("/api/showings/" + editingShowingId, {

            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(showing)

        })
            .then(response => {

                if (!response.ok) {
                    throw new Error("Kunne ikke redigere forestillingen");
                }

                return response.json();
            })
            .then(() => {

                showingForm.reset();

                editingShowingId = null;

                const submitButton =
                    showingForm.querySelector('button[type="submit"]');

                submitButton.textContent = "Opret forestilling";

                getUpcomingShowings();
            })
            .catch(error => {

                console.error(
                    "Fejl ved redigering af forestilling:",
                    error
                );

            });

    }

    // Ellers opretter vi en ny forestilling
    else {

        fetch("/api/showings", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(showing)

        })
            .then(response => {

                if (!response.ok) {
                    throw new Error("Kunne ikke oprette forestillingen");
                }

                return response.json();
            })
            .then(() => {

                showingForm.reset();

                getUpcomingShowings();
            })
            .catch(error => {

                console.error(
                    "Fejl ved oprettelse af forestilling:",
                    error
                );

            });
    }
});


// Kører når siden åbnes
getMovies();
getTheatres();
getUpcomingShowings();