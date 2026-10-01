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
                const reservationElement = document.createElement("div");
                reservationElement.className = "reservation";

                const seats = reservation.seats.length > 0
                    ? reservation.seats.map(seat => `<li>${seat}</li>`).join("")
                    : "<li>Ingen sæder tilknyttet</li>";

                reservationElement.innerHTML = `
                    <h3>${reservation.customerName}</h3>
                    <p>Telefon: ${reservation.phone}</p>
                    <p>Forestilling: ${reservation.movieTitle ?? "Ukendt film"} – ${reservation.theatreName ?? ""}, ${formatDateTime(reservation.showingDateTime)}</p>
                    <p>Sæder:</p>
                    <ul>${seats}</ul>
                `;

                reservationList.appendChild(reservationElement);
            });
        })
        .catch(error => {
            console.error("Fejl ved hentning af reservationer:", error);
            reservationList.innerHTML = `<p class="empty-message">Kunne ikke hente reservationer.</p>`;
        });
}

getReservations();
