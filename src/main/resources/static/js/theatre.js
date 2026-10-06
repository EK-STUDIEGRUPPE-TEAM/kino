const theatreForm = document.getElementById("theatre-form");

const theatreName = document.getElementById("theatre-name");
const numberOfRows = document.getElementById("number-of-rows");
const seatsPerRow = document.getElementById("seats-per-row");

const submitTheatreButton =
    document.getElementById("submit-theatre-button");

const theatreFormTitle =
    document.getElementById("theatre-form-title");

const theatreTableBody =
    document.getElementById("theatre-table-body");


let theatreIdToEdit = null;


/* ---------- GET THEATRES ---------- */

function getTheatres() {

    fetch("/api/theatres")
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not get theatres");
            }

            return response.json();
        })
        .then(theatres => {

            theatreTableBody.innerHTML = "";

            if (theatres.length === 0) {

                const row = theatreTableBody.insertRow();
                const cell = row.insertCell();

                cell.colSpan = 5;
                cell.textContent = "No theatres found";
                cell.classList.add("empty-table");

                return;
            }


            theatres.forEach(theatre => {

                const row = theatreTableBody.insertRow();


                /* ID */

                const idCell = row.insertCell();
                idCell.textContent = theatre.id;


                /* Name */

                const nameCell = row.insertCell();
                nameCell.textContent = theatre.name;


                /* Number of rows */

                const rowsCell = row.insertCell();
                rowsCell.textContent = theatre.numberOfRows;


                /* Seats per row */

                const seatsCell = row.insertCell();
                seatsCell.textContent = theatre.seatsPerRow;



                const handlingCell = row.insertCell();
                handlingCell.classList.add("action-cell");


                const viewButton = document.createElement("button");

                viewButton.type = "button";
                viewButton.textContent = "Se sal";
                viewButton.classList.add("view-button");

                viewButton.addEventListener("click", function () {

                    window.location.href =
                        "theatre-view.html?id=" + theatre.id;

                });


                /* ---------- EDIT BUTTON ---------- */

                const editButton = document.createElement("button");

                editButton.type = "button";
                editButton.textContent = "Rediger";
                editButton.classList.add("edit-button");

                editButton.addEventListener("click", function () {

                    theatreIdToEdit = theatre.id;

                    theatreName.value = theatre.name;
                    numberOfRows.value = theatre.numberOfRows;
                    seatsPerRow.value = theatre.seatsPerRow;

                    theatreFormTitle.textContent = "Rediger biografsal";
                    submitTheatreButton.textContent = "Gem ændringer";


                    document
                        .querySelectorAll("#theatre-table tbody tr")
                        .forEach(tableRow => {

                            tableRow.classList.remove("editing-row");

                        });

                    row.classList.add("editing-row");

                });


                /* ---------- DELETE BUTTON ---------- */

                const deleteButton = document.createElement("button");

                deleteButton.type = "button";
                deleteButton.textContent = "Slet";
                deleteButton.classList.add("delete-button");

                deleteButton.addEventListener("click", function () {

                    deleteTheatre(theatre.id);

                });


                /* ---------- ADD BUTTONS ---------- */

                handlingCell.appendChild(viewButton);
                handlingCell.appendChild(editButton);
                handlingCell.appendChild(deleteButton);

            });

        })
        .catch(error => {

            console.error("Error getting theatres:", error);

        });
}


/* ---------- CREATE THEATRE ---------- */

function addTheatre(event) {

    event.preventDefault();


    const theatre = {

        name: theatreName.value,

        numberOfRows: Number(numberOfRows.value),

        seatsPerRow: Number(seatsPerRow.value)

    };


    /* If a theatre is being edited */

    if (theatreIdToEdit !== null) {

        updateTheatre(theatreIdToEdit, theatre);

        return;
    }


    /* Otherwise create a new theatre */

    fetch("/api/theatres/add", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(theatre)

    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not create theatre");
            }

            return response.json();

        })
        .then(() => {

            resetTheatreForm();

            getTheatres();

        })
        .catch(error => {

            console.error("Error creating theatre:", error);

        });
}


/* ---------- UPDATE THEATRE ---------- */

function updateTheatre(id, theatre) {

    fetch("/api/theatres/update/" + id, {

        method: "PUT",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(theatre)

    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not update theatre");
            }

            return response.json();

        })
        .then(() => {

            resetTheatreForm();

            getTheatres();

        })
        .catch(error => {

            console.error("Error updating theatre:", error);

        });
}


/* ---------- DELETE THEATRE ---------- */

function deleteTheatre(id) {

    fetch("/api/theatres/delete/" + id, {

        method: "DELETE"

    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not delete theatre");
            }


            if (theatreIdToEdit === id) {

                resetTheatreForm();

            }


            getTheatres();

        })
        .catch(error => {

            console.error("Error deleting theatre:", error);

        });
}


/* ---------- RESET FORM ---------- */

function resetTheatreForm() {

    theatreIdToEdit = null;

    theatreForm.reset();

    theatreFormTitle.textContent = "Opret biografsal";

    submitTheatreButton.textContent = "Opret Sal";


    document
        .querySelectorAll("#theatre-table tbody tr")
        .forEach(row => {

            row.classList.remove("editing-row");

        });
}


/* ---------- EVENTS ---------- */

theatreForm.addEventListener("submit", addTheatre);


/* ---------- LOAD THEATRES ---------- */

getTheatres();