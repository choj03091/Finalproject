document.addEventListener("DOMContentLoaded", () => {
    const toggleButton = document.querySelector(".sidebar-toggle"); // 연필 버튼
    const sidebar = document.querySelector(".sidebar"); // 사이드 메뉴
    

    toggleButton.addEventListener("click", () => {
        if (sidebar.classList.contains("visible")) {
            sidebar.classList.remove("visible");
            sidebar.classList.add("hidden"); // 숨길 때 "hidden" 클래스 추가
        } else {
            sidebar.classList.remove("hidden");
            sidebar.classList.add("visible"); // 보일 때 "visible" 클래스 추가
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const menuLinks = document.querySelectorAll(".sidebar-menu li a");

         menuLinks.forEach(link => {
        link.addEventListener("click", (event) => {
            event.preventDefault(); // 페이지 이동 막기 (옵션)
            const destination = link.getAttribute("href"); // 링크의 href 속성 가져오기
            console.log(`${destination}로 이동합니다.`); // 콘솔 출력 (테스트용)
            // window.location.href = destination; // 페이지 이동 수행
        });
    });
});