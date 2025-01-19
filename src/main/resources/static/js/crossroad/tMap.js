
let marker = null;// 마커 변수
let abortController = new AbortController(); // API 요청 중 signal로 상태 변경

let startMarker= null;
let endMarker= null;

let input = null; // 데이터
let routeLayer= null;

// 데이터 요청 상태 관리
let isFetchingData = false;
let marker_id;
let minTime;

let startLatLng;
let endLatLng;

const markers = {};


// 지도 초기화
var map = new Tmapv2.Map("map", {
    center: new Tmapv2.LatLng(37.5665, 126.9780), // 지도 중심 좌표 (위도, 경도)
    width: "100%",
    height: "900px",
    zoom: 12 // 줌 레벨 (값이 높을수록 확대)
});

map.addListener("click", function (event) {
    console.log("좌표값 : "+ startLatLng)
    startLatLng = null; // 값 초기화
    startLatLng = event.latLng; // 클릭한 위치의 위도, 경도 정보

    if(startLatLng){
        startMarker = new Tmapv2.Marker({
            position: startLatLng,
            map: map
        });
    } // 클릭 시 현재 startMarker 값을 이벤트로 업데이트
});

fetch('api/crossroads/marker')
    .then(response=> response.json())
    .then(data=> {
        console.log(data);
        // 데이터 순회하며 마커 생성
        data.forEach(crossroad => {
            const lat = crossroad.mapCtptIntLat;
            const lng = crossroad.mapCtptIntLot;

            const marker = new Tmapv2.Marker({
                position: new Tmapv2.LatLng(lat,lng),
                map,
                title: crossroad.itstNm
            });

            markers[crossroad.crossroadApiId]=marker;

            marker.addListener("click",()=> {
                marker_id=crossroad.itstId
                console.log("click");
                disableFetching();
                enableFetching();
                callSignalStatus(marker_id);
            });
        });
    }); /** TODO:
        *    PROBLEM: marker 찍기는 map 객체에 별도로 상태를 저장해두기가 불가
        *    SOLUTION: 서버에서 데이터의 상태를 유지하기 위한 기능 고민
        *    WHY: marker 갯수가 많아지면 랜더링 시간이 과하게 길어질 수 있음
        */

// 데이터 갱신 비활성화
function disableFetching() {
    isFetchingData = false;
    handleCountdown();
    console.log("Data fetching disabled.");
}

// 데이터 갱신 활성화
function enableFetching() {
    isFetchingData = true;
    console.log("Data fetching enabled.");
}

// 데이터 저장 및 UI 반영
function callSignalStatus(api_id){
    console.log(api_id);
    fetch(`api/crossroads/state/${api_id}`,{ cache: "no-store" })
        .then(response => response.json())
        .then( data => {
            updateUI(data);
        });
}

// UI 업데이트 함수
function updateUI(data) {
    minTime=10000;
    const directions = ["NW", "NT", "NE", "WT", "ET", "SW", "ST", "SE"];

    console.log(data);

    directions.forEach((dir) => {
        const stateKey = `${dir.toLowerCase()}PdsgStatNm`;
        const timeKey = `${dir.toLowerCase()}PdsgRmdrCs`;

        if (!(stateKey in data[0]) || !(timeKey in data[0])) {
            console.warn(`Missing data for direction: ${dir}`);
            return;
        }

        const state = data[0][stateKey];
        const time = data[0][timeKey];
        const circle = document.getElementById(dir);
        const card = document.getElementById("card");

       /* const statusColors = {
            "stop-And-Remain": "red",
            "protected-Movement-Allowed": "green",
            "permissive-Movement-Allowed": "gray"
        };*/

        if(!state){
            circle.style.backgroundColor = "white";
            circle.style.color = "white";
        }
        else if(state){
            card.style.visibility = "visible";
            circle.style.color = "black";
            circle.style.backgroundColor = state; // 상태 색상
            minTime = Math.min(minTime, time)+10; // 최소 남은 시간 계산
        }
    });

    time();
}

// 1초마다 카운트다운 실행
function handleCountdown() {
    if (!isFetchingData) return;

    if (minTime > 0) {
        minTime -= 10; // 남은 시간 감소
    }

    // 시간이 0이면 데이터 갱신
    if (minTime <= 0) {
        console.log("Time expired during countdown. Fetching new data...");
        callSignalStatus(marker_id);
    }

    time(); // UI 업데이트
}

function time(){
    // 최소 남은 시간 표시
    const timeSpan = document.querySelector(".text-body-secondary");
    timeSpan.textContent = `${Math.ceil(minTime/10)} s Left`;
}

// 1초마다 카운트다운 실행
setInterval(handleCountdown, 1000);

// 검색 버튼 클릭 시 동작
document.getElementById("search").addEventListener("click", async function() {
    var address = document.getElementById("searchInput").value; // 입력된 주소 가져오기

    if(address) {
        // 기존 요청을 취소
        if (abortController) {
            abortController.abort(); // 이전 요청 취소
        }

        //새로운 컨트롤러 생성
        abortController = new AbortController();

        await searchAddress(address, abortController.signal);
    } else {
        alert("주소를 입력하세요!");
    }
});

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
        console.log(data);
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

    if(poi){
        endLatLng = new Tmapv2.LatLng(poi.frontLat, poi.frontLon);
        endMarker = new Tmapv2.Marker({
            position:  endLatLng,
            map: map
        });
    }
};

function updateSuggestions(pois) {
    const suggestionsElement = document.getElementById("suggestions");
    suggestionsElement.innerHTML = ""; // 기존 추천어 목록 초기화


    if (pois.length === 0) {
        suggestionsElement.style.display = "none";
        return;
    }

    pois.forEach(poi => {
        const div = document.createElement("div");
        div.textContent = poi.name;

        // 클릭 이벤트 리스너를 추가하기 전에 기존 이벤트 리스너 제거
        div.removeEventListener("click", handleClick); // 기존 이벤트 리스너 제거 (있다면)
        suggestionsElement.style.visibility = "visible";

        // 새로운 클릭 이벤트 리스너 추가
        div.addEventListener("click", () => handleClick(poi));
        suggestionsElement.appendChild(div);

    });

    suggestionsElement.classList.add("show"); // 추천어 목록 표시
}

// 마커를 제거하는 함수
function clearMarker() {
    if( endMarker ){
        endMarker.setMap(null);
        endMarker = null;
    }
}

document.getElementById("searchInput").addEventListener("input", function () {
    const inputValue = searchInput.value.trim();
    if (inputValue === "") {
        // 검색박스가 비었을 때
        clearMarker(); // 마커 제거
    }
});


document.getElementById("route").addEventListener("click", function (){
    console.log("click route");
    findRoute();
}); // 경로 찾기

// 경로 검색
async function findRoute() {

    if(!startLatLng._lat || !startLatLng._lng){
        alert("출발지를 선택하세요!");
        return;
    }

    if (!endLatLng._lat || !endLatLng._lng) {
        alert("도착지를 선택하세요!");
        return;
    }

    let startLat = startLatLng._lat;
    let startLon = startLatLng._lng;
    let endLat = endLatLng._lat;
    let endLon = endLatLng._lng;

    console.log(startLon);
    console.log(endLon);

    const url = `https://apis.openapi.sk.com/tmap/routes/pedestrian`;
    const headers = {
        "Accept": "application/json",
        "appKey": "1bxEMLzGUg68a4EeRA5F14J5Vbgh6GWI3zLXabl9"
    };
    const body = JSON.stringify({
        startX: startLon,
        startY: startLat,
        endX: endLon,
        endY: endLat,
        reqCoordType: "WGS84GEO",
        resCoordType: "EPSG3857",
        startName: "출발지",
        endName: "도착지"
    });

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json", ...headers },
            body
        });
        const data = await response.json();
        const routeData = data.features;

        console.log(data);

        if (routeLayer) {
            routeLayer.setMap(null);
        }

        console.log(routeData);

        drawRoute(routeData);
    } catch (error) {
        console.error("길찾기 중 오류:", error);
    }
}

function convertToWGS84(x, y) {
    var lon = (x / 20037508.34) * 180;
    var lat = (y / 20037508.34) * 180;
    lat = 180 / Math.PI * (2 * Math.atan( Math.exp (lat * Math.PI / 180)) - Math.PI / 2);

    return { lat: lat, lon: lon };
} // 더러운거 보지 마세요 -> 좌표계 변경


// 경로 그리기
function drawRoute(routeData) {

    const path = routeData
        .filter(item => item.geometry.type === "LineString")
        .flatMap(item => item.geometry.coordinates.map(coord => {
                // 좌표 변환
                const converted = convertToWGS84(coord[0], coord[1]);

                // 변환된 좌표 유효성 검사
                if (converted.lat === 0 && converted.lon === 0) {
                    console.warn("Invalid coordinates detected:", coord);
                    return null; // 잘못된 좌표는 제외
                }

                // 유효한 좌표를 LatLng 객체로 반환
                return new Tmapv2.LatLng(converted.lat, converted.lon);
            })
        )
        .filter(Boolean);

    routeLayer = new Tmapv2.Polyline({
        path,
        strokeColor: "#ff0000",
        strokeWeight: 6,
        map: map
    });

}
