
const showingSelect = document.getElementById("showing");
const seatMap = document.getElementById("seat-map");
const selectedSeatsText = document.getElementById("selected-seats");
const reservationForm = document.getElementById("reservation-form");
const reservationMessage = document.getElementById("reservation-message");

const params = new URLSearchParams(window.location.search);
const selectedShowingId = params.get("showingId");

let selectedSeats = [];


/* ---------- Date Formatting ---------- */

function formatDateTime(dateTime) {
    return new Date(dateTime).toLocaleString("da-DK", {
        dateStyle: "medium",
        timeStyle: "short"
    });
}


/* ---------- Messages ---------- */

function showMessage(text, isError) {
    reservationMessage.className = isError
        ? "message error"
        : "message success";

    reservationMessage.textContent = text;
}


/* ---------- Selected Seats ---------- */

function updateSelectedSeatsText() {
    if (selectedSeats.length === 0) {
        selectedSeatsText.textContent = "Valgte sæder: ingen";
        return;
    }

    const names = selectedSeats.map(
        seat => `Række ${seat.rowNumber}, sæde ${seat.seatNumber}`
    );

    selectedSeatsText.textContent =
        "Valgte sæder: " + names.join(" · ");
}


/* ---------- Get Showings ---------- */

function getShowings() {
    fetch("/api/showings")
        .then(response => {
            if (!response.ok) {
                throw new Error("Kunne ikke hente forestillinger");
            }
            return response.json();
        })
        .then(showings => {
            if (showings.length === 0) {
                showingSelect.innerHTML =
                    '<option value="">Der er ingen forestillinger</option>';
                return;
            }

            showingSelect.innerHTML =
                '<option value="">Vælg forestilling</option>';

            showings.forEach(showing => {
                const option = document.createElement("option");

                option.value = showing.id;
                option.textContent =
                    `${showing.movie.title} – ${showing.theatre.name}, ` +
                    formatDateTime(showing.dateTime);

                showingSelect.appendChild(option);
            });

            if (selectedShowingId) {
                showingSelect.value = selectedShowingId;

                if (showingSelect.value === selectedShowingId) {
                    getSeats(selectedShowingId);
                }
            }
        })
        .catch(error => {
            console.error("Fejl ved hentning af forestillinger:", error);

            showingSelect.innerHTML =
                '<option value="">Kunne ikke hente forestillinger</option>';
        });
}


/* ---------- Get Seats ---------- */

function getSeats(showingId) {
    selectedSeats = [];
    updateSelectedSeatsText();
    reservationMessage.textContent = "";

    if (!showingId) {
        seatMap.innerHTML =
            '<p class="empty-message">Vælg en forestilling først.</p>';
        return;
    }

    Promise.all([
        fetch(`/api/showings/${showingId}`).then(response => {
            if (!response.ok) {
                throw new Error("Kunne ikke hente forestillingen");
            }
            return response.json();
        }),

        fetch(`/api/seats?showingId=${showingId}`).then(response => {
            if (!response.ok) {
                throw new Error("Kunne ikke hente sæder");
            }
            return response.json();
        }),

        fetch(`/api/seats/reserved?showingId=${showingId}`).then(response => {
            if (!response.ok) {
                throw new Error("Kunne ikke hente reservationer");
            }
            return response.json();
        })
    ])
        .then(([showing, seats, reservedSeats]) => {
            seatMap.innerHTML = "";

            const theatre = showing.theatre;
            const numberOfRows = Number(theatre.numberOfRows);
            const seatsPerRow = Number(theatre.seatsPerRow);

            if (!numberOfRows || !seatsPerRow || seats.length === 0) {
                seatMap.innerHTML =
                    '<p class="empty-message">Der er ingen sæder i denne sal.</p>';
                return;
            }

            // Sæde-ID'er, der allerede er reserveret eller solgt.
            const reservedSeatIds = new Set(
                reservedSeats.map(seat => Number(seat.id))
            );

            // Find hvert sæde ud fra række og sædenummer.
            const seatsByPosition = new Map();

            seats.forEach(seat => {
                seatsByPosition.set(
                    `${seat.rowNumber}-${seat.seatNumber}`,
                    seat
                );
            });

            /*
             * Samme grid-struktur som showing-theatre:
             * Første kolonne er rækkebogstavet.
             * Resten af kolonnerne er sæderne.
             */
            seatMap.style.display = "grid";
            seatMap.style.gridTemplateColumns =
                `40px repeat(${theatre.seatsPerRow}, 45px)`;
            seatMap.style.gridAutoFlow = "row";
            seatMap.style.gridAutoRows = "auto";
            seatMap.style.columnGap = "5px";
            seatMap.style.rowGap = "8px";
            seatMap.style.justifyContent = "center";
            seatMap.style.alignItems = "center";
            seatMap.style.width = "max-content";
            seatMap.style.maxWidth = "none";
            seatMap.style.margin = "0 auto";

            for (let row = 1; row <= numberOfRows; row++) {
                // Opret rækkebogstav: A, B, C osv.
                const rowLabel = document.createElement("div");
                rowLabel.className = "row-label";
                rowLabel.textContent = String.fromCharCode(64 + row);

                seatMap.appendChild(rowLabel);

                for (
                    let seatNumber = 1;
                    seatNumber <= seatsPerRow;
                    seatNumber++
                ) {
                    const seat = seatsByPosition.get(
                        `${row}-${seatNumber}`
                    );

                    const wrapper = document.createElement("div");
                    wrapper.className = "seat-wrapper";

                    // Bevar pladsen, hvis et sæde mangler.
                    if (!seat) {
                        seatMap.appendChild(wrapper);
                        continue;
                    }

                    // Selve sædet.
                    const button = document.createElement("button");
                    button.type = "button";
                    button.className = "seat";
                    button.title =
                        `Række ${rowLabel.textContent}, sæde ${seatNumber}`;

                    button.setAttribute(
                        "aria-label",
                        `Række ${rowLabel.textContent}, sæde ${seatNumber}`
                    );

                    // Sædenummer under sædet.
                    const numberLabel = document.createElement("span");
                    numberLabel.className = "seat-number";
                    numberLabel.textContent = seatNumber;

                    if (reservedSeatIds.has(Number(seat.id))) {
                        button.classList.add("reserved");
                        button.disabled = true;
                    } else {
                        button.classList.add("available");

                        button.addEventListener("click", () => {
                            const index = selectedSeats.findIndex(
                                selected =>
                                    Number(selected.id) === Number(seat.id)
                            );

                            if (index === -1) {
                                selectedSeats.push(seat);

                                button.classList.replace(
                                    "available",
                                    "selected"
                                );
                            } else {
                                selectedSeats.splice(index, 1);

                                button.classList.replace(
                                    "selected",
                                    "available"
                                );
                            }

                            updateSelectedSeatsText();
                        });
                    }

                    wrapper.appendChild(button);
                    wrapper.appendChild(numberLabel);
                    seatMap.appendChild(wrapper);
                }
            }
        })
        .catch(error => {
            console.error("Fejl ved hentning af sæder:", error);

            seatMap.innerHTML =
                '<p class="empty-message">Kunne ikke hente sæder.</p>';
        });
}


/* ---------- Change Showing ---------- */

showingSelect.addEventListener("change", function () {
    getSeats(showingSelect.value);
});


/* ---------- Create Reservation ---------- */

reservationForm.addEventListener("submit", function (event) {
    event.preventDefault();

    if (!showingSelect.value) {
        showMessage("Vælg en forestilling.", true);
        return;
    }

    if (selectedSeats.length === 0) {
        showMessage("Vælg mindst ét sæde.", true);
        return;
    }

    const reservation = {
        customerName: document.getElementById("customerName").value,
        phone: document.getElementById("phone").value,
        showing: {
            id: Number(showingSelect.value)
        }
    };

    const seatIds = selectedSeats
        .map(seat => seat.id)
        .join(",");

    fetch(`/api/reservations?seatIds=${seatIds}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reservation)
    })
        .then(async response => {
            const data = await response.json();

            return {
                ok: response.ok,
                data: data
            };
        })
        .then(({ ok, data }) => {
            if (!ok) {
                showMessage(
                    data.message || "Reservationen kunne ikke oprettes.",
                    true
                );

                getSeats(showingSelect.value);
                return;
            }

            const seatNames = selectedSeats.map(
                seat => `Række ${seat.rowNumber}, sæde ${seat.seatNumber}`
            );

            showMessage(
                `Reservation oprettet for ${data.customerName}: ` +
                `${data.showing.movie.title}, ${seatNames.join(" · ")}`,
                false
            );

            document.getElementById("customerName").value = "";
            document.getElementById("phone").value = "";

            getSeats(showingSelect.value);
        })
        .catch(error => {
            console.error("Fejl ved oprettelse af reservation:", error);

            showMessage(
                "Reservationen kunne ikke oprettes.",
                true
            );
        });
});


/* ---------- Start ---------- */

getShowings();
