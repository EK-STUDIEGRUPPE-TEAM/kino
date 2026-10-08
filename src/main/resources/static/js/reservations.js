const reservationList = document.getElementById("reservation-list");

function formatDateTime(dateTime) {
    if (!dateTime) {
        return "Ukendt tidspunkt";
    }
    return new Date(dateTime).toLocaleString("da-DK", {
        dateStyle: "medium",
        timeStyle: "short"
    });
}

function getReservations() {
    fetch("/api/reservations")
        .then(response => response.json())
        .then(reservations => {
            reservationList.innerHTML = "";

            if (reservations.length === 0) {
                reservationList.innerHTML = `<p class="empty-message">Der er ingen reservationer endnu.</p>`;
                return;
            }

            reservations.forEach(reservation => {
                const showing = reservation.showing;
                const seats = reservation.reservationSeats.map(reservationSeat => reservationSeat.seat);
                const reservationElement = document.createElement("div");
                reservationElement.className = "reservation";

                const seatItems = seats
                    .sort((a, b) => a.rowNumber - b.rowNumber || a.seatNumber - b.seatNumber)
                    .map(seat => `<li>Række ${seat.rowNumber}, sæde ${seat.seatNumber}</li>`)
                    .join("");

                reservationElement.innerHTML = `
                    <h3>${reservation.customerName}</h3>
                    <p>Telefon: ${reservation.phone}</p>
                    <p>Forestilling: ${showing.movie.title} – ${showing.theatre.name}, ${formatDateTime(showing.dateTime)}</p>
                    <p>Sæder:</p>
                                    <ul>${seatItems}</ul>
                    <button class="cancel-button">Annuller reservation</button>
                `;

                reservationElement.querySelector(".cancel-button")
                    .addEventListener("click", () => cancelReservation(reservation));


                reservationList.appendChild(reservationElement);
            });

        })
        .catch(error => {
            console.error("Fejl ved hentning af reservationer:", error);
            reservationList.innerHTML = `<p class="empty-message">Kunne ikke hente reservationer.</p>`;
        });
}
function cancelReservation(reservation) {
    if (!confirm(`Vil du annullere reservationen for ${reservation.customerName}?`)) {
        return;
    }

    fetch(`/api/reservations/${reservation.id}`, { method: "DELETE" })
        .then(response => {
            if (!response.ok) {
                throw new Error("Status " + response.status);
            }
            getReservations();
        })
        .catch(error => {
            console.error("Fejl ved annullering af reservation:", error);
            alert("Reservationen kunne ikke annulleres.");
        });
}


getReservations();
