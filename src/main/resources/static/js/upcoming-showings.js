const showingTableBody = document.getElementById("showing-table-body");


function getUpcomingShowings() {

    fetch("/api/showings/upcoming")
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not get upcoming showings");
            }

            return response.json();
        })
        .then(showings => {

            showingTableBody.innerHTML = "";

            if (showings.length == 0) {

                const row = showingTableBody.insertRow();

                const cell = row.insertCell();

                cell.colSpan = 4;
                cell.textContent = "No upcoming showings found";

                return;
            }


            showings.forEach(showing => {

                const row = showingTableBody.insertRow();


                const movieCell = row.insertCell();
                movieCell.textContent = showing.movie.title;


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
                "Error getting upcoming showings:",
                error
            );

        });
}


function formatDate(dateTime) {

    const date = new Date(dateTime);

    return date.toLocaleDateString("da-DK");
}

function formatTime(dateTime) {

    const date = new Date(dateTime);

    return date.toLocaleTimeString("da-DK", {
        hour: "2-digit",
        minute: "2-digit"
    });
}

getUpcomingShowings();