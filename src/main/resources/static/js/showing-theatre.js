// Hent theatre ID fra URL'en
const urlParams = new URLSearchParams(window.location.search);
const showingId = urlParams.get("showingId");

const theatreName = document.getElementById("theatre-name");
const theatreInfo = document.getElementById("theatre-info");
const seatContainer = document.getElementById("seat-container");
let seatStatuses = {};


function getTheatre() {

    fetch("/api/showings/" + showingId)
        .then(response => {

            if (!response.ok) {
                throw new Error("Kunne ikke hente forestilling");
            }

            return response.json();
        })
        .then(showing => {

            const theatre = showing.theatre
            // Vis salens navn
            theatreName.textContent = theatre.name;

            // Vis information om salen
            theatreInfo.textContent =
                theatre.numberOfRows +
                " rækker × " +
                theatre.seatsPerRow +
                " sæder";

            getSeatStatuses(theatre);

            // Lav sæderne
        })
        .catch(error => {
            console.error("Fejl ved hentning af sal:", error);

            theatreName.textContent = "Fejl";
            theatreInfo.textContent = "Kunne ikke hente salen";
        });
}


function getSeatStatuses(theatre) {
    fetch("/api/reservations/showing/" + showingId)
        .then(response => {
            if (!response.ok) {
                throw new Error("Kunne ikke hente reservationer");
            }
            return response.json();
        })
        .then(reservations => {

            seatStatuses = {};

            reservations.forEach(reservationSeat => {

                const seat = reservationSeat.seat;

                const key = seat.rowNumber + "-" + seat.seatNumber;

                seatStatuses[key] = reservationSeat.status;
            });

            console.log(seatStatuses);

            createSeats(theatre);
        })
        .catch(error => {
            console.error("Fejl ved hentning af reservationer:", error);

            // Hvis der ikke er reservationer, vis alle som ledige
            seatStatuses = {};
            createSeats(theatre);
        });
}

function createSeats(theatre) {

    // Ryd tidligere sæder
    seatContainer.innerHTML = "";

    // Lav kolonner
    seatContainer.style.gridTemplateColumns =
        `40px repeat(${theatre.seatsPerRow}, 45px)`;


    for (let row = 1; row <= theatre.numberOfRows; row++) {

        // Lav rækkebogstav
        const rowLabel = document.createElement("div");

        rowLabel.classList.add("row-label");

        // 1 = A, 2 = B, 3 = C osv.
        rowLabel.textContent =
            String.fromCharCode(64 + row);

        seatContainer.appendChild(rowLabel);


        // Lav sæderne
        for (let seatNumber = 1;
             seatNumber <= theatre.seatsPerRow;
             seatNumber++) {

            const seatWrapper = document.createElement("div");

            seatWrapper.classList.add("seat-wrapper");


            // Selve sædet
            const seat = document.createElement("div");

            seat.classList.add("seat");

            seat.dataset.row = row;
            seat.dataset.seat = seatNumber;


            // Sædenummer
            const seatNumberLabel = document.createElement("span");

            seatNumberLabel.classList.add("seat-number");

            seatNumberLabel.textContent = seatNumber;

            // sædestatus

            const status = seatStatuses[row + "-" + seatNumber];

            if (status === "RESERVED") {
                seat.classList.add("reserved");
            }
            else if (status === "SOLD") {
                seat.classList.add("sold");
            }
            else {
                seat.classList.add("available");
            }

            seatWrapper.appendChild(seat);
            seatWrapper.appendChild(seatNumberLabel);

            seatContainer.appendChild(seatWrapper);
        }
    }
}


// Hent salen når siden åbnes
getTheatre();