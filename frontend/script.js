/* ===========================================================
   Note Calculator - Frontend JavaScript
   Talks to the Spring Boot REST API using fetch()
   =========================================================== */

// Base URL of our Spring Boot backend.
// Change this if your backend runs on a different host/port.
const API_BASE_URL = "http://localhost:8080/api/calculations";

/* -----------------------------------------------------------
   CALCULATOR PAGE (index.html)
----------------------------------------------------------- */

const amountInput = document.getElementById("amount");
const denominationSelect = document.getElementById("denomination");
const calculateBtn = document.getElementById("calculateBtn");
const clearBtn = document.getElementById("clearBtn");
const amountError = document.getElementById("amountError");
const denominationError = document.getElementById("denominationError");
const serverMessage = document.getElementById("serverMessage");
const resultCard = document.getElementById("resultCard");

if (calculateBtn) {
  calculateBtn.addEventListener("click", handleCalculate);
}

if (clearBtn) {
  clearBtn.addEventListener("click", clearForm);
}

/**
 * Validates the form, calls the backend API, and displays the result.
 */
async function handleCalculate() {
  clearMessages();

  const amountValue = amountInput.value.trim();
  const denominationValue = denominationSelect.value;

  // ---- Frontend validation ----
  let hasError = false;

  if (amountValue === "" || isNaN(amountValue)) {
    amountError.textContent = "Please enter a valid amount.";
    hasError = true;
  } else if (Number(amountValue) <= 0) {
    amountError.textContent = "Please enter a valid amount.";
    hasError = true;
  }

  if (denominationValue === "") {
    denominationError.textContent = "Please select a denomination.";
    hasError = true;
  }

  if (hasError) {
    return;
  }

  // ---- Call backend REST API ----
  try {
    calculateBtn.disabled = true;
    calculateBtn.textContent = "Calculating...";

    const response = await fetch(API_BASE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        amount: Number(amountValue),
        denomination: Number(denominationValue),
      }),
    });

    const data = await response.json();

    if (!response.ok) {
      // Backend returned an error (400, 500, etc.)
      showServerMessage(data.message || "Something went wrong.", "error");
      resultCard.style.display = "none";
      return;
    }

    displayResult(data);
    showServerMessage("Calculation saved successfully!", "success");
  } catch (err) {
    // Network error - e.g. backend not running
    showServerMessage(
      "Could not connect to the server. Please make sure the backend is running.",
      "error"
    );
  } finally {
    calculateBtn.disabled = false;
    calculateBtn.textContent = "Calculate";
  }
}

/** Fills in the result card with data returned from the API. */
function displayResult(data) {
  document.getElementById("resultAmount").textContent = `₹${formatNumber(data.amount)}`;
  document.getElementById("resultDenomination").textContent = `₹${data.denomination}`;
  document.getElementById("resultNotes").textContent = data.numberOfNotes;
  document.getElementById("resultRemainder").textContent = `₹${formatNumber(data.remainingAmount)}`;
  resultCard.style.display = "block";
}

/** Clears the form, hides the result card, and clears any messages. */
function clearForm() {
  amountInput.value = "";
  denominationSelect.value = "";
  resultCard.style.display = "none";
  clearMessages();
}

function clearMessages() {
  amountError.textContent = "";
  denominationError.textContent = "";
  serverMessage.textContent = "";
  serverMessage.className = "server-message";
}

function showServerMessage(text, type) {
  serverMessage.textContent = text;
  serverMessage.className = `server-message ${type}`;
}

function formatNumber(value) {
  return Number(value).toFixed(2).replace(/\.00$/, "");
}

/* -----------------------------------------------------------
   HISTORY PAGE (history.html)
----------------------------------------------------------- */

const historyTableBody = document.getElementById("historyTableBody");
const historyMessage = document.getElementById("historyMessage");
const clearHistoryBtn = document.getElementById("clearHistoryBtn");
const emptyHistoryText = document.getElementById("emptyHistoryText");

if (historyTableBody) {
  loadHistory();
}

if (clearHistoryBtn) {
  clearHistoryBtn.addEventListener("click", handleClearHistory);
}

/** Loads all calculations from the backend and renders them in the table. */
async function loadHistory() {
  try {
    const response = await fetch(API_BASE_URL);
    const data = await response.json();

    if (!response.ok) {
      showHistoryMessage("Could not load history.", "error");
      return;
    }

    renderHistoryTable(data);
  } catch (err) {
    showHistoryMessage(
      "Could not connect to the server. Please make sure the backend is running.",
      "error"
    );
  }
}

function renderHistoryTable(calculations) {
  historyTableBody.innerHTML = "";

  if (calculations.length === 0) {
    document.getElementById("historyTable").style.display = "none";
    emptyHistoryText.style.display = "block";
    return;
  }

  document.getElementById("historyTable").style.display = "table";
  emptyHistoryText.style.display = "none";

  calculations.forEach((calc) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${calc.id}</td>
      <td>₹${formatNumber(calc.amount)}</td>
      <td>₹${calc.denomination}</td>
      <td>${calc.numberOfNotes}</td>
      <td>₹${formatNumber(calc.remainingAmount)}</td>
      <td>${formatDate(calc.calculatedAt)}</td>
      <td><button class="btn btn-danger btn-small" onclick="deleteEntry(${calc.id})">Delete</button></td>
    `;
    historyTableBody.appendChild(row);
  });
}

/** Deletes a single history entry by ID (called from the inline Delete button). */
async function deleteEntry(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, { method: "DELETE" });
    if (response.ok) {
      loadHistory();
    } else {
      showHistoryMessage("Could not delete this entry.", "error");
    }
  } catch (err) {
    showHistoryMessage("Could not connect to the server.", "error");
  }
}

/** Clears all calculation history after user confirmation. */
async function handleClearHistory() {
  const confirmed = confirm("Are you sure you want to clear all history? This cannot be undone.");
  if (!confirmed) return;

  try {
    const response = await fetch(API_BASE_URL, { method: "DELETE" });
    if (response.ok) {
      showHistoryMessage("History cleared successfully.", "success");
      loadHistory();
    } else {
      showHistoryMessage("Could not clear history.", "error");
    }
  } catch (err) {
    showHistoryMessage("Could not connect to the server.", "error");
  }
}

function showHistoryMessage(text, type) {
  historyMessage.textContent = text;
  historyMessage.className = `server-message ${type}`;
}

function formatDate(isoString) {
  const date = new Date(isoString);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
}
