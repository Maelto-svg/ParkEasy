// Modal functionality
const modal = document.getElementById("modal");
const reserveBtn = document.getElementById("reserveBtn");
const closeBtn = document.getElementById("closeBtn");

// Show the modal when "Reserve a Spot" is clicked
reserveBtn.addEventListener("click", function(event) {
  event.preventDefault();
  modal.style.display = "flex";
});

// Close the modal when "X" is clicked
closeBtn.addEventListener("click", function() {
  modal.style.display = "none";
});

// Close the modal when clicking outside the modal content
window.addEventListener("click", function(event) {
  if (event.target === modal) {
    modal.style.display = "none";
  }
});

// Login validation
document.getElementById("loginForm").addEventListener("submit", function(event) {
  event.preventDefault();
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  // Validate credentials
  if (username === "admin" && password === "admin") {
    // Redirect to parking interface on successful login
    window.location.href = "parking.html";
  } else {
    alert("Invalid credentials. Please try again.");
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const parkingSpots = document.querySelectorAll(".parking-slot");

  parkingSpots.forEach(spot => {
    spot.addEventListener("click", () => {
      const statusText = spot.querySelector("span");

      if (spot.classList.contains("available")) {
        spot.classList.remove("available");
        spot.classList.add("reserved");
        statusText.textContent = "Reserved";
      } else if (spot.classList.contains("reserved")) {
        spot.classList.remove("reserved");
        spot.classList.add("occupied");
        statusText.textContent = "Occupied";
      } else if (spot.classList.contains("occupied")) {
        spot.classList.remove("occupied");
        spot.classList.add("available");
        statusText.textContent = "Available";
      }
    });
  });
});