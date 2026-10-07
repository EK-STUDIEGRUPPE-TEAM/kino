const showingSelect = document.getElementById("showing");
const seatMap = document.getElementById("seat-map");
const selectedSeatsText = document.getElementById("selected-seats");
const reservationForm = document.getElementById("reservation-form");
const reservationMessage = document.getElementById("reservation-message");

const params = new URLSearchParams(window.location.search);
const selectedShowingId = params.get("showingId");

let selectedSeats = [];


function formatDateTime(dateTime) {
    return new Date(dateTime).toLocaleString("da-DK", {
        dateStyle: "medium",
        timeStyle: "short"
    });
}


function showMessage(text, isError) {
    reservationMessage.className =
        isError ? "message error" : "message success";

    reservationMessage.textContent = text;
}


function updateSelectedSeatsText() {

    if (selectedSeats.length === 0) {
        selectedSeatsText.textContent =
            "Valgte sæder: ingen";

        return;
    }

    const names = selectedSeats.map(
        seat =>
            `Række ${seat.rowNumber}, sæde ${seat.seatNumber}`
    );

    selectedSeatsText.textContent =
        "Valgte sæder: " + names.join(" · ");
}


function getShowings() {

    fetch("/api/showings")

        .then(response =>
            response.json()
        )

        .then(showings => {

            if (showings.length === 0) {

                showingSelect.innerHTML =
                    `<option value="">
                        Der er ingen forestillinger
                    </option>`;

                return;
            }


            showingSelect.innerHTML =
                `<option value="">
                    Vælg forestilling
                </option>`;


            showings.forEach(showing => {

                const option =
                    document.createElement("option");

                option.value =
                    showing.id;

                option.textContent =
                    `${showing.movie.title} – ${showing.theatre.name}, ${formatDateTime(showing.dateTime)}`;

                showingSelect.appendChild(option);
            });


            if (selectedShowingId) {

                showingSelect.value =
                    selectedShowingId;

                getSeats(selectedShowingId);
            }
        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af forestillinger:",
                error
            );

            showingSelect.innerHTML =
                `<option value="">
                    Kunne ikke hente forestillinger
                </option>`;
        });
}


function getSeats(showingId) {

    selectedSeats = [];

    updateSelectedSeatsText();


    if (!showingId) {

        seatMap.innerHTML =
            `<p class="empty-message">
                Vælg en forestilling først.
            </p>`;

        return;
    }


    // Hent alle sæder i salen og de sæder, der allerede er reserveret
    Promise.all([

        fetch(
            `/api/seats?showingId=${showingId}`
        ).then(
            response => response.json()
        ),

        fetch(
            `/api/seats/reserved?showingId=${showingId}`
        ).then(
            response => response.json()
        )

    ])

        .then(([seats, reservedSeats]) => {

            seatMap.innerHTML = "";


            if (seats.length === 0) {

                seatMap.innerHTML =
                    `<p class="empty-message">
                        Der er ingen sæder i denne sal.
                    </p>`;

                return;
            }


            const reservedSeatIds =
                reservedSeats.map(
                    seat => seat.id
                );


            const screen =
                document.createElement("div");

            screen.className =
                "screen";

            screen.textContent =
                "LÆRRED";

            seatMap.appendChild(screen);


            // Grupper sæderne i rækker
            const rows = {};


            seats.forEach(seat => {

                if (!rows[seat.rowNumber]) {
                    rows[seat.rowNumber] = [];
                }

                rows[seat.rowNumber]
                    .push(seat);
            });


            Object.keys(rows)
                .forEach(rowNumber => {

                    const rowElement =
                        document.createElement("div");

                    rowElement.className =
                        "seat-row";


                    const label =
                        document.createElement("span");

                    label.className =
                        "row-label";

                    label.textContent =
                        rowNumber;

                    rowElement.appendChild(label);


                    rows[rowNumber]
                        .forEach(seat => {

                            const seatButton =
                                document.createElement("button");

                            seatButton.type =
                                "button";

                            seatButton.className =
                                "seat";

                            seatButton.textContent =
                                seat.seatNumber;

                            seatButton.title =
                                `Række ${seat.rowNumber}, sæde ${seat.seatNumber}`;


                            if (
                                reservedSeatIds.includes(
                                    seat.id
                                )
                            ) {

                                seatButton.classList.add(
                                    "reserved"
                                );

                                seatButton.disabled = true;
                            }


                            seatButton.addEventListener(
                                "click",
                                () => {

                                    const index =
                                        selectedSeats.findIndex(
                                            selectedSeat =>
                                                selectedSeat.id === seat.id
                                        );


                                    if (index === -1) {

                                        selectedSeats.push(
                                            seat
                                        );

                                        seatButton.classList.add(
                                            "selected"
                                        );

                                    } else {

                                        selectedSeats.splice(
                                            index,
                                            1
                                        );

                                        seatButton.classList.remove(
                                            "selected"
                                        );
                                    }


                                    updateSelectedSeatsText();
                                }
                            );


                            rowElement.appendChild(
                                seatButton
                            );
                        });


                    seatMap.appendChild(
                        rowElement
                    );
                });
        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af sæder:",
                error
            );

            seatMap.innerHTML =
                `<p class="empty-message">
                    Kunne ikke hente sæder.
                </p>`;
        });
}


showingSelect.addEventListener(
    "change",
    function () {

        reservationMessage.textContent = "";

        getSeats(
            showingSelect.value
        );
    }
);


reservationForm.addEventListener(
    "submit",
    function (event) {

        event.preventDefault();


        if (selectedSeats.length === 0) {

            showMessage(
                "Vælg mindst ét sæde.",
                true
            );

            return;
        }


        const reservation = {

            customerName:
            document
                .getElementById(
                    "customerName"
                )
                .value,

            phone:
            document
                .getElementById(
                    "phone"
                )
                .value,

            showing: {
                id: Number(
                    showingSelect.value
                )
            }
        };


        const seatIds =
            selectedSeats
                .map(
                    seat => seat.id
                )
                .join(",");


        fetch(
            `/api/reservations?seatIds=${seatIds}`,
            {

                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(
                        reservation
                    )
            }
        )

            .then(
                response =>
                    response
                        .json()
                        .then(
                            data => ({
                                ok: response.ok,
                                data: data
                            })
                        )
            )

            .then(({ok, data}) => {

                if (!ok) {

                    showMessage(
                        data.message
                        || "Reservationen kunne ikke oprettes.",
                        true
                    );

                    getSeats(
                        showingSelect.value
                    );

                    return;
                }


                const seatNames =
                    selectedSeats.map(
                        seat =>
                            `Række ${seat.rowNumber}, sæde ${seat.seatNumber}`
                    );


                showMessage(
                    `Reservation oprettet for ${data.customerName}: ${data.showing.movie.title}, ${seatNames.join(" · ")}`,
                    false
                );


                document
                    .getElementById(
                        "customerName"
                    )
                    .value = "";

                document
                    .getElementById(
                        "phone"
                    )
                    .value = "";


                getSeats(
                    showingSelect.value
                );
            })

            .catch(error => {

                console.error(
                    "Fejl ved oprettelse af reservation:",
                    error
                );

                showMessage(
                    "Reservationen kunne ikke oprettes.",
                    true
                );
            });
    }
);


getShowings();