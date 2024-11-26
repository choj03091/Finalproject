document.addEventListener("DOMContentLoaded", function () {
    // 사용할 색상 배열
    const colors = ["#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#0000FF", "#4B0082", "#9400D3"]; // 빨, 주, 노, 초, 파, 남, 보

    // 랜덤 색상을 선택
    const randomColor = colors[Math.floor(Math.random() * colors.length)];

    // 프로필 초기 배경색 설정
    const profileInitial = document.getElementById("profileInitial");
    if (profileInitial) {
        profileInitial.style.backgroundColor = randomColor;
    }
});
