export function initUserStatusWebSocket() {
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log("WebSocket connected for user status.");

        // 사용자 상태 변경 수신
        stompClient.subscribe('/topic/user/status', (message) => {
            const statusUpdate = JSON.parse(message.body);
            console.log("Received status update:", statusUpdate);

            // 사용자 상태 업데이트 (예: HTML 요소 업데이트)
            const userElement = document.querySelector(
                `[data-user-id="${statusUpdate.userId}"]`
            );
            if (userElement) {
                userElement.querySelector('.status-text').textContent =
                    statusUpdate.status === "active"
                        ? "활동 중"
                        : statusUpdate.status === "away"
                        ? "자리 비움"
                        : "오프라인";

                // 상태 표시 색상 업데이트
                userElement.querySelector('.status-indicator').className =
                    `status-indicator ${statusUpdate.status}`;
            }
        });
    });

    return stompClient;
}
