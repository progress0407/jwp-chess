const EVEN_COLOR = "#AD8B73";
const ODD_COLOR = "#CEAB93";

const FILES = ["A", "B", "C", "D", "E", "F", "G", "H"];
const RANKS = ["1", "2", "3", "4", "5", "6", "7", "8"];
const REVERSE_RANKS = RANKS.reverse();

const pieceRegExp = /images\/(\w*?)\.png/;

let globalFrom = "";
let globalTo = "";
let globalClickedSquare = "";

$(document).ready(function () {
  initChessBoard();
  triggerEvents();

  if (isNewGame()) {
    initChessPieces();
  } else {
    loadLastGame();
  }
});

function isNewGame() {
  return JSON.parse($("#is-new-game").val());
}

function initChessBoard() {
  for (let r = 0; r < REVERSE_RANKS.length; r++) {
    addChessBoardRow();
    for (let f = 0; f < FILES.length; f++) {
      addChessBoardSquare(r, f);
    }
  }
}

function addChessBoardRow() {
  $("#chess-board").append("<tr>");
}

function addChessBoardSquare(r, f) {
  const rank = REVERSE_RANKS[r];
  const file = FILES[f];
  const squareId = file + rank;
  const color = getColor(r, f);

  $("#chess-board tr")
    .last()
    .append(`<td id=${squareId} style="background-color: ${color}">`);
}

function initChessPieces() {
  $.ajax({
    url: "/chess/first",
    method: "GET",
    dataType: "json",
  })
    .done(function (data) {
      placeChessPieces(data.board);
      setCurrentTeam(data.teamName);
      setTeamScore(data.teamNameToScore);
      createRoom();
    })
    .fail(function (xhr, status, errorThrown) {
      console.log(xhr);
      alert(xhr.responseText);
    });
}

/**
    private String encryptedRoomPassword;
    private Map<String, String> pieces;
 */

function createRoom() {
  const createdRoomId = $("#created-room-id").val();
  const gameData = {
    roomName: $("#room-name").val(),
    encryptedRoomPassword: $("#encrypted-room-password").val(),
    pieces: getPieces(),
  };

  $.ajax({
    url: `/rooms/${createdRoomId}`,
    method: "POST",
    data: JSON.stringify(gameData),
    contentType: "application/json",
  })
    .done(function (data) {})
    .fail(function (xhr, status, errorThrown) {
      console.log(xhr);
      alert(xhr.responseText);
    });
}

function loadLastGame() {
  $.ajax({
    url: "/chess/last",
    method: "GET",
    dataType: "json",
  })
    .done(function (data) {
      placeChessPieces(data.board);
      setCurrentTeam(data.teamName);
      setTeamScore(data.teamNameToScore);
    })
    .fail(function (xhr, status, errorThrown) {
      console.log(xhr);
      alert(xhr.responseText);
    });
}

function triggerEvents() {
  $("button#save-game").click(function () {
    saveGameRequest();
  });

  $("table#chess-board td").click(function () {
    colorClickedSquare(this);
    movePiece(this);
  });
}

function colorClickedSquare(thisParam) {
  $(globalClickedSquare).removeClass("clicked");
  $(thisParam).addClass("clicked");
  globalClickedSquare = thisParam;
}

function getColor(r, f) {
  if (isEven(r, f)) {
    return EVEN_COLOR;
  }
  return ODD_COLOR;
}

function isEven(r, f) {
  return (r + f) % 2 === 0;
}

function placeChessPiece(position, pieceImage) {
  $("#chess-board td#" + position).append(
    `<img src="/images/${pieceImage}.png" />`
  );
}

function placeChessPieces(piecePositions) {
  for (let element of piecePositions) {
    element = element.value;
    let team = element["team"];
    let piece = element["piece"];
    let position = element["position"].toUpperCase();
    let pieceImage = team + piece; // ex) blackPawn

    if (team !== "empty") {
      placeChessPiece(position, pieceImage);
    }
  }
}

function movePiece(square) {
  if (globalFrom == "") {
    if (!isExistPiece(square)) {
      console.log("말을 선택해야 합니다.");
      return;
    }
    globalFrom = square.id;
    return;
  }
  globalTo = square.id;
  movePieceRequest(globalFrom, globalTo);
}

function clearFromAndTo() {
  globalTo = "";
  globalFrom = "";
}

function movePieceRequest(from, to) {
  const lastTeam = $("#current-team").text();
  clearFromAndTo();
  $.ajax({
    url: `/chess/move?from=${from}&to=${to}`,
    method: "PUT",
    dataType: "json",
  })
    .done(function (data) {
      clearPieceImages();
      placeChessPieces(data.board);
      setCurrentTeam(data.teamName);
      setTeamScore(data.teamNameToScore);
      processGameIfKingKill(data.gamePlaying, lastTeam);
    })
    .fail(function (xhr, status, errorThrown) {
      console.log(xhr);
      alert(xhr.responseText);
    });
}

function clearPieceImages() {
  $("table#chess-board").find("img").remove();
}

function setCurrentTeam(teamName) {
  $("#current-team").text(teamName);

  $("#current-team").removeClass();

  switch (teamName.toLowerCase()) {
    case "white team":
      $("#current-team").addClass("current-team white");
      break;

    case "black team":
      $("#current-team").addClass("current-team black");
      break;
  }
}

function setTeamScore(teamNameToScore) {
  $("#white-score").text(teamNameToScore.white);
  $("#black-score").text(teamNameToScore.black);
}

function processGameIfKingKill(gamePlaying, lastTeam) {
  if (!gamePlaying) {
    $.ajax({
      url: `/chess`,
      method: "DELETE",
    })
      .done(function (data) {
        alert("Game Win !!  winner : " + lastTeam);
        location.replace("/");
      })
      .fail(function (xhr, status, errorThrown) {
        console.log(xhr);
        alert(xhr.responseText);
      });
  }
}

function isExistPiece(square) {
  return $("#" + square.id).find("img").length == true;
}

function getPieces() {
  let pieces = {};

  $("#chess-board tr td").each(function (index, element) {
    const square = element;
    if (isExistPiece(square)) {
      const imageName = $(square).find("img").attr("src");
      const piece = imageName.replace(pieceRegExp, `$1`);
      pieces[square.id] = piece;
    }
  });

  return pieces;
}

function saveGameRequest() {
  const gameData = {
    currentTeam: $("#current-team").text(),
    pieces: getPieces(),
    createdAt: new Date().toISOString(),
  };

  $.ajax({
    url: "/chess",
    method: "POST",
    data: JSON.stringify(gameData),
    contentType: "application/json",
  })
    .done(function (data) {
      alert("Save Complete !");
    })
    .fail(function (xhr, status, errorThrown) {
      console.log(xhr);
      alert(xhr.responseText);
    });

  location.replace("/");
}
