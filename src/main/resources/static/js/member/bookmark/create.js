let abortController = new AbortController(); // API 요청 중 signal로 상태 변경
const createParams = {}; // 즐겨찾기 등록 객체

// 공통 동작 함수
async function handleSearch() {
  const address = document.getElementById("searchInput").value; // 입력된 주소 가져오기

  if (address) {
    // 기존 요청을 취소
    if (abortController) {
      abortController.abort(); // 이전 요청 취소
    }

    // 새로운 컨트롤러 생성
    abortController = new AbortController();

    await searchAddress(address, abortController.signal);
  } else {
    alert("주소를 입력하세요!");
  }
}

// "click" 이벤트와 "Enter" 이벤트 모두 처리
document.getElementById("search").addEventListener("click", handleSearch);
// document.getElementById("searchInput").addEventListener("keydown", (event) => {
//   if (event.key === "Enter") {
//     handleSearch();
//   }
// });  // TODO : 엔터 이벤트 추가...

// 주소 검색 함수
async function searchAddress(address, signal) {
  input = document.getElementById("searchInput").value;
  // 검색할 주소
  const url = `https://apis.openapi.sk.com/tmap/pois?version=1&searchType=all&page=1&multiPoint=Y&searchtypCd=A&searchKeyword=${input}`;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Accept": "application/json",
        "appKey": "1bxEMLzGUg68a4EeRA5F14J5Vbgh6GWI3zLXabl9"
      },
      signal: signal
    });

    if (!response.ok) {
      throw new Error(`HTTP Error: ${response.status}`);
    }

    const data = await response.json();
    const pois = data.searchPoiInfo.pois.poi;

    if (pois && pois.length > 0) {// POI 이름만 추출
      updateSuggestions(pois);
    } else {
      updateSuggestions([]);
    }
  } catch (error) {
    if (error.name === 'AbortError') {
      // AbortError는 요청 취소이므로 그냥 무시하고 정상 처리
      console.log("검색이 취소되었습니다.");
    } else {
      console.error("Error during address search:", error);
    }
    updateSuggestions([]);
  }
}

//이벤트 핸들러 변수 저장
const handleClick = (poi) => {
  document.getElementById("searchInput").value = poi.name; // 클릭 시 입력 필드에 채우기
  document.getElementById("suggestions").style.visibility = "hidden";// 추천어 목록 숨김
  document.querySelector("#alias").value = poi.name;

  const address = poi.newAddressList.newAddress[0].fullAddressRoad; // 도로명 주소
  const lat = poi.noorLat;
  const lng = poi.noorLon;

  createParams.lat = lat;
  createParams.lng = lng;
  createParams.address = address;
};

function updateSuggestions(pois) {
  const suggestionsElement = document.getElementById("suggestions");
  suggestionsElement.innerHTML = ""; // 기존 추천어 목록 초기화

  if (pois.length === 0) {
    suggestionsElement.style.display = "none";
    return;
  }

  pois.forEach(poi => {
    console.log("poi : ", poi);
    const span = document.createElement("span");
    span.textContent = poi.name;
    span.className = "search-list";

    // 클릭 이벤트 리스너를 추가하기 전에 기존 이벤트 리스너 제거
    span.removeEventListener("click", handleClick); // 기존 이벤트 리스너 제거 (있다면)
    suggestionsElement.style.visibility = "visible";

    // 새로운 클릭 이벤트 리스너 추가
    span.addEventListener("click", () => handleClick(poi));
    suggestionsElement.appendChild(span);

  });

  suggestionsElement.classList.add("show"); // 추천어 목록 표시
  document.getElementById("search").style.display = "block";
}

const addBtn = (memberId) => {
  createParams.name = document.querySelector("#alias").value;

  if (!isValidUpdateParams(createParams)) {
    alert('등록 정보가 올바르지 않습니다.');
    return;
  }

  fetch(`/api/bookmarks`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(createParams),
  })
  .then(response => {
    if (response.ok) {
      alert('등록 완료됐습니다.');
      window.location.href = `/members/${memberId}/bookmarks`;
    }
  })
  .catch(error => {
    console.log('수정 실패 : ', error);
    alert('등록 실패했습니다.');
  })
}

const isValidUpdateParams = (params) => {
  return params && params.address && params.name && params.lat && params.lng;
};
