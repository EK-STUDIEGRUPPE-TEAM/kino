// Hent theatre ID fra URL'en
const urlParams = new URLSearchParams(window.location.search);
const theatreId = urlParams.get("id");

const theatreName = document.getElementById("theatre-name");
const theatreInfo = document.getElementById("theatre-info");
const seatContainer = document.getElementById("seat-container");


function getTheatre() {

    fetch("/api/theatres/" + theatreId)
        .then(response => {

            if (!response.ok) {
                throw new Error("Kunne ikke hente sal");
            }

            return response.json();
        })
        .then(theatre => {

            // Vis salens navn
            theatreName.textContent = theatre.name;

            // Vis information om salen
            theatreInfo.textContent =
                theatre.numberOfRows +
                " rækker × " +
                theatre.seatsPerRow +
                " sæder";

            // Lav sæderne
            createSeats(theatre);
        })
        .catch(error => {
            console.error("Fejl ved hentning af sal:", error);

            theatreName.textContent = "Fejl";
            theatreInfo.textContent = "Kunne ikke hente salen";
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


            seatWrapper.appendChild(seat);
            seatWrapper.appendChild(seatNumberLabel);

            seatContainer.appendChild(seatWrapper);
        }
    }
}

// Hent salen når siden åbnes
getTheatre();