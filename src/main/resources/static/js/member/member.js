const deleteBtn = (btn) => {
  const id = btn.dataset.id;
  const flag = confirm("탈퇴 하시겠습니까?");

  if (!flag) {
    return;
  }
  fetch(`/api/members/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    }
  })
  .then(data => {
    console.log('회원 탈퇴 성공:', data);
    alert("탈퇴 성공했습니다.")
    sendLogoutRequest();
  })
  .catch(error => {
    console.error('회원 탈퇴 실패:', error);
  });
}
const sendLogoutRequest = () => {
  // HTML Form을 동적으로 생성해 POST 요청으로 로그아웃 처리
  const form = document.createElement("form");
  form.method = "POST";
  form.action = "/logout"; // Spring Security의 기본 로그아웃 경로

  form.appendChild(csrfInput);
  document.body.appendChild(form);
  form.submit();
}

const modalOpen = () => {
  document.querySelector(".modal").style.display = "flex";
}

const modalClose = () => {
  document.querySelector(".modal").style.display = "none";
}