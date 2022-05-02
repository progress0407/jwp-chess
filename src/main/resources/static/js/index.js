const EMPTIES = ["", null, undefined];

$(document).ready(function () {
  triggerEvents();
  loadRooms();

  if (isEmptyRoomInfo()) {
    alert("방 이름이나 비밀번호는 비어있을 수 없습니다 !");
  }
});

function triggerEvents() {
  $("button#room-delete").click(function () {
    deleteClickEvent();
  });

  $("button#first-game").click(function () {
    createGame();
  });
}

function deleteClickEvent() {
  let isDel = confirm("delete this event ? ");
  if (isDel) {
    alert("deleted ... ");
    return;
  }
  console.log("cancel");
}

function loadRooms() {
  $.ajax({
    url: "/rooms",
    method: "GET",
  })
    .done(function (data) {
      for (let element of data.values) {
        $("#room-list").append(`<tr id=${element.gameId}>`);
        $("#room-list tr")
          .last()
          .append(`<td id="room-name">${element.roomName}</td>`)
          .append(`<td id="room-create-at">${element.createdAt}</td>`)
          .append(
            `<td><input type="password" id="room-password" name="room-password"></td>`
          )
          .append(`<td><button id="room-delete">DELETE</button></td>`);
      }
    })
    .fail(function (xhr, status, errorThrown) {
      console.log(xhr);
      alert(xhr.responseText);
    });
}

let isEmpty = (text) => EMPTIES.includes(text);

let createGame = () => {
  debugger;
  let roomName = $("#room-name").val().trim();
  let roomPassword = $("#room-password").val().trim();

  if (isEmpty(roomName) || isEmpty(roomPassword)) {
    alert("빈 값을 입력할 수 없습니다 !");
    return;
  }

  location.replace("/games/first");
};

function isEmptyRoomInfo() {
  return JSON.parse($("#is-empty-room-info").val());
}
