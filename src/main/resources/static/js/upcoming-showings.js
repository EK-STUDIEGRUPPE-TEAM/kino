const showingTableBody =
    document.getElementById("showing-table-body");

const upcomingShowingTableBody =
    document.getElementById("upcoming-showing-table-body");

const showingForm =
    document.getElementById("showing-form");

let editingShowingId = null;


// Henter alle forestillinger
function getShowings() {

    fetch("/api/showings")
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
                cell.textContent = "Ingen forestillinger";

                return;
            }

            showings.forEach(showing => {

                const row = showingTableBody.insertRow();


                // Film
                const movieCell = row.insertCell();
                movieCell.textContent =
                    showing.movie.title;


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
                actionCell.className = "action-cell";


                // Redigér
                const editButton =
                    document.createElement("button");

                editButton.type = "button";
                editButton.textContent = "Redigér";
                editButton.className = "edit-button";

                editButton.addEventListener(
                    "click",
                    function () {
                        editShowing(showing);
                    }
                );

                actionCell.appendChild(editButton);


                // Slet
                const deleteButton =
                    document.createElement("button");

                deleteButton.type = "button";
                deleteButton.textContent = "Slet";
                deleteButton.className = "delete-button";

                deleteButton.addEventListener(
                    "click",
                    function () {
                        deleteShowing(showing.id);
                    }
                );

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


// Henter forestillinger for de næste 2 uger
function getUpcomingShowings() {

    fetch("/api/showings/upcoming")
        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Kunne ikke hente kommende forestillinger"
                );
            }

            return response.json();
        })
        .then(showings => {

            upcomingShowingTableBody.innerHTML = "";

            if (showings.length === 0) {

                const row =
                    upcomingShowingTableBody.insertRow();

                const cell = row.insertCell();

                cell.colSpan = 4;
                cell.textContent =
                    "Ingen kommende forestillinger";

                return;
            }

            showings.forEach(showing => {

                const row =
                    upcomingShowingTableBody.insertRow();


                const movieCell = row.insertCell();
                movieCell.textContent =
                    showing.movie.title;


                const dateCell = row.insertCell();
                dateCell.textContent =
                    formatDate(showing.dateTime);


                const timeCell = row.insertCell();
                timeCell.textContent =
                    formatTime(showing.dateTime);


                const theatreCell = row.insertCell();
                theatreCell.textContent =
                    showing.theatre.name;
            });

        })
        .catch(error => {

            console.error(
                "Fejl ved hentning af kommende forestillinger:",
                error
            );

        });
}


// Henter film til dropdown
function getMovies() {

    fetch("/api/movies")
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


// Henter sale til dropdown
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


// Fylder formularen med den valgte forestilling
function editShowing(showing) {

    editingShowingId = showing.id;

    document.getElementById("movie-id").value =
        showing.movie.id;

    document.getElementById("theatre-id").value =
        showing.theatre.id;

    document.getElementById("showing-date-time").value =
        showing.dateTime.substring(0, 16);

    const submitButton =
        showingForm.querySelector(
            'button[type="submit"]'
        );

    submitButton.textContent = "Gem ændringer";
}


// Sletter forestilling
function deleteShowing(id) {

    fetch("/api/showings/" + id, {
        method: "DELETE"
    })
        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Kunne ikke slette forestillingen"
                );
            }

            getShowings();
            getUpcomingShowings();
        })
        .catch(error => {

            console.error(
                "Fejl ved sletning af forestilling:",
                error
            );

        });
}


// Opretter eller redigerer forestilling
showingForm.addEventListener(
    "submit",
    function (event) {

        event.preventDefault();


        const showing = {

            dateTime:
            document
                .getElementById(
                    "showing-date-time"
                )
                .value,

            movie: {
                id: Number(
                    document
                        .getElementById("movie-id")
                        .value
                )
            },

            theatre: {
                id: Number(
                    document
                        .getElementById("theatre-id")
                        .value
                )
            }
        };


        // Redigér eksisterende forestilling
        if (editingShowingId !== null) {

            fetch(
                "/api/showings/" + editingShowingId,
                {

                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(showing)
                }
            )
                .then(response => {

                    if (!response.ok) {
                        throw new Error(
                            "Kunne ikke redigere forestillingen"
                        );
                    }

                    return response.json();
                })
                .then(() => {

                    showingForm.reset();

                    editingShowingId = null;

                    const submitButton =
                        showingForm.querySelector(
                            'button[type="submit"]'
                        );

                    submitButton.textContent =
                        "Opret forestilling";

                    getShowings();
                    getUpcomingShowings();
                })
                .catch(error => {

                    console.error(
                        "Fejl ved redigering af forestilling:",
                        error
                    );

                });

        }

        // Opret ny forestilling
        else {

            fetch("/api/showings", {

                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(showing)

            })
                .then(response => {

                    if (!response.ok) {
                        throw new Error(
                            "Kunne ikke oprette forestillingen"
                        );
                    }

                    return response.json();
                })
                .then(() => {

                    showingForm.reset();

                    getShowings();
                    getUpcomingShowings();
                })
                .catch(error => {

                    console.error(
                        "Fejl ved oprettelse af forestilling:",
                        error
                    );

                });
        }
    }
);


// Kører når siden åbnes
getMovies();
getTheatres();
getShowings();
getUpcomingShowings();