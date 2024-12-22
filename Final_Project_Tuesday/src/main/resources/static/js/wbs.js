// 프로젝트 시작일 및 종료일 가져오기
let projectStartDate = document.getElementById('project-start-date').value;
let projectEndDate = document.getElementById('project-end-date').value;

console.log('프로젝트 시작일:', projectStartDate, '종료일:', projectEndDate);

let stompClient; // WebSocket 클라이언트

// WebSocket 연결 설정 및 실시간 업데이트 수신
function initializeWebSocket() {
    if (stompClient && stompClient.connected) {
        console.warn('WebSocket이 이미 연결되어 있습니다.');
        return;
    }

    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log('WebSocket 연결 성공');

        stompClient.subscribe('/topic/wbs-updates', (message) => {
            const data = JSON.parse(message.body);
            console.log('WebSocket 메시지 수신:', data);

            if (typeof data === 'number') {
                handleDeleteTask(data); // 삭제된 작업 처리
            } else {
                handleAddOrUpdateTask(data); // 추가 또는 수정된 작업 처리
            }
        });
    }, (error) => {
        console.error('WebSocket 연결 실패:', error);
    });
}

document.addEventListener('DOMContentLoaded', async () => {
    try {
        const data = await fetchWbsData(); // 데이터 로드 및 반환
        renderGanttChart(data); // Gantt Chart 초기화
        initializeWebSocket(); // WebSocket 연결
        renderGanttTimeline(projectStartDate, projectEndDate); // 타임라인 생성
        console.log('DOMContentLoaded 초기화 완료');
    } catch (error) {
        console.error('초기화 중 오류 발생:', error);
    }
});


function resetAndRenderGanttChart() {
    console.log('Gantt Chart 초기화 및 재렌더링 시작');

    // Gantt Chart 컨테이너 초기화
    const ganttContainer = document.getElementById('gantt-container');
    if (!ganttContainer) {
        console.error('gantt-container 요소를 찾을 수 없습니다.');
        return;
    }
    ganttContainer.innerHTML = ''; // 모든 기존 Bar 제거

    // 타임라인 재생성
    const ganttTimeline = document.createElement('div');
    ganttTimeline.id = 'gantt-timeline';
    ganttTimeline.className = 'gantt-timeline';
    ganttContainer.appendChild(ganttTimeline);

    // 타임라인 렌더링
    renderGanttTimeline(projectStartDate, projectEndDate);

    // WBS 데이터 다시 가져오기
    fetchWbsData()
        .then(data => {
            console.log('Gantt Chart 데이터 로드 성공:', data);
            renderGanttChart(data); // Gantt Chart 재렌더링
        })
        .catch(error => {
            console.error('Gantt Chart 재렌더링 중 오류 발생:', error);
        });
}



// WBS 데이터를 가져와 테이블 및 Gantt Chart 렌더링
async function fetchWbsData() {
    const projectId = document.getElementById('project-id').value;

    try {
        const response = await fetch(`/api/wbs/${projectId}`);
        if (!response.ok) {
            throw new Error(`API 호출 실패: ${response.status}`);
        }

        const data = await response.json();
        if (!data || data.length === 0) {
            console.warn('WBS 데이터가 비어 있습니다.');
        }
        console.log('WBS 데이터 새로고침 성공:', data);

        renderWbsTable(data);
        return data; // 데이터를 반환
    } catch (error) {
        console.error('WBS 데이터를 가져오는 중 오류 발생:', error);
        throw error; // 에러를 다시 던져줌
    }
}



// WBS 테이블 렌더링
function renderWbsTable(data) {
    const tbody = document.querySelector('#wbs-table tbody');
    tbody.innerHTML = '';
    data.forEach(task => addWbsRow(task));
}

// WBS 테이블 행 추가
function addWbsRow(task) {
    const tbody = document.querySelector('#wbs-table tbody');
    const row = document.createElement('tr');
    row.id = `wbs-row-${task.id}`;
    row.innerHTML = `
        <td>${task.taskName || '없음'}</td>
        <td>${task.assignee || '미정'}</td>
        <td>${task.status || '미정'}</td>
        <td>${task.startDate || '미정'}</td>
        <td>${task.endDate || '미정'}</td>
        <td>
            <button onclick="openEditWbsModal(${task.id})">수정</button>
            <button onclick="deleteTask(${task.id})">삭제</button>
        </td>
    `;
    tbody.appendChild(row);
}

// WBS 테이블 행 업데이트
function updateWbsRow(row, task) {
    row.innerHTML = `
        <td>${task.taskName || '없음'}</td>
        <td>${task.assignee || '미정'}</td>
        <td>${task.status || '미정'}</td>
        <td>${task.startDate || '미정'}</td>
        <td>${task.endDate || '미정'}</td>
        <td>
            <button onclick="openEditWbsModal(${task.id})">수정</button>
            <button onclick="deleteTask(${task.id})">삭제</button>
        </td>
    `;
}

// 작업 추가/수정 처리
function handleAddOrUpdateTask(task) {
    console.log('handleAddOrUpdateTask 호출:', task);

    // WBS 테이블 업데이트
    const existingRow = document.querySelector(`#wbs-row-${task.id}`);
    if (existingRow) {
        console.log('기존 WBS 행 수정:', task.id);
        updateWbsRow(existingRow, task);
    } else {
        console.log('새로운 WBS 행 추가:', task.id);
        addWbsRow(task);
    }

    // Gantt Chart 초기화 및 재렌더링 호출
    resetAndRenderGanttChart();
}

// Gantt Chart 렌더링
function renderGanttChart(data = []) {
    const ganttContainer = document.getElementById('gantt-container');
    const timeline = document.getElementById('gantt-timeline');
    if (!ganttContainer || !timeline) {
        console.error('Gantt Chart 요소를 찾을 수 없습니다.');
        return;
    }

    ganttContainer.innerHTML = ''; // Gantt Chart 초기화
    ganttContainer.appendChild(timeline); // 타임라인 유지

    const baseDate = new Date(projectStartDate).getTime();

    if (!data || data.length === 0) {
        console.warn('렌더링할 데이터가 없습니다.');
        return;
    }

    data.forEach((task, index) => {
        if (!task.startDate || !task.endDate) {
            console.warn('작업에 시작일 또는 종료일이 없습니다:', task);
            return;
        }

        const taskStart = new Date(task.startDate).getTime();
        const taskEnd = new Date(task.endDate).getTime() + (1000 * 60 * 60 * 24); // 종료일 포함

        const left = Math.ceil((taskStart - baseDate) / (1000 * 60 * 60 * 24)) * 40;
        const width = Math.ceil((taskEnd - taskStart) / (1000 * 60 * 60 * 24)) * 40;

        console.log('Gantt Bar 좌표 계산:', { taskName: task.taskName, left, width });

        const bar = document.createElement('div');
        bar.className = 'gantt-bar';
        bar.style.left = `${left}px`;
        bar.style.width = `${width}px`;
        bar.style.top = `${index * 50}px`; // 간격 유지
        bar.textContent = task.taskName;

        ganttContainer.appendChild(bar);
    });
}


// Gantt Chart 타임라인 생성
function renderGanttTimeline(startDate, endDate) {
    const ganttTimeline = document.getElementById('gantt-timeline');
    if (!ganttTimeline) {
        console.error('gantt-timeline 요소가 없습니다. 새로 생성합니다.');

        const ganttContainer = document.getElementById('gantt-container');
        const newTimeline = document.createElement('div');
        newTimeline.id = 'gantt-timeline';
        newTimeline.className = 'gantt-timeline';
        ganttContainer.appendChild(newTimeline);

        renderGanttTimeline(startDate, endDate); // 재귀 호출
        return;
    }

    ganttTimeline.innerHTML = ''; // 타임라인 초기화

    const start = new Date(startDate).getTime();
    const end = new Date(endDate).getTime();
    const days = Math.ceil((end - start) / (1000 * 60 * 60 * 24));

    for (let i = 0; i <= days; i++) {
        const day = new Date(start + i * (1000 * 60 * 60 * 24));
        const label = document.createElement('div');
        label.className = 'gantt-timeline-day';
        label.textContent = `${day.getMonth() + 1}/${day.getDate()}`;
        label.style.width = '40px';
        ganttTimeline.appendChild(label);
    }
}


// Gantt Chart 단일 작업 업데이트
function updateGanttBar(task) {
    console.log('Gantt Bar 업데이트 시작:', task);

    const ganttContainer = document.getElementById('gantt-container');

    // 기존 Bar 찾기 및 제거
    const existingBar = document.querySelector(`#gantt-bar-${task.id}`);
    if (existingBar) {
        console.log('기존 Gantt Bar 제거:', task.id);
        existingBar.remove();
    } else {
        console.warn('기존 Gantt Bar를 찾지 못했습니다:', task.id);
    }

    const baseDate = new Date(projectStartDate).getTime();
    const taskStart = new Date(task.startDate).getTime();
    const taskEnd = new Date(task.endDate).getTime() + (1000 * 60 * 60 * 24);

    const left = Math.ceil((taskStart - baseDate) / (1000 * 60 * 60 * 24)) * 40;
    const width = Math.ceil((taskEnd - taskStart) / (1000 * 60 * 60 * 24)) * 40;

    console.log('Gantt Bar 좌표 계산:', { taskName: task.taskName, left, width });

    const bar = document.createElement('div');
    bar.id = `gantt-bar-${task.id}`;
    bar.className = 'gantt-bar';
    bar.style.left = `${left}px`;
    bar.style.width = `${width}px`;
    bar.textContent = task.taskName;

    ganttContainer.appendChild(bar);
    console.log('Gantt Bar 업데이트 완료.');
}



function openEditWbsModal(taskId) {
    const row = document.querySelector(`#wbs-row-${taskId}`);
    if (!row) {
        console.error(`작업 ID ${taskId}에 대한 행을 찾을 수 없습니다.`);
        return;
    }

    const task = {
        id: taskId,
        taskName: row.children[0].innerText,
        assignee: row.children[1].innerText,
        status: row.children[2].innerText,
        startDate: row.children[3].innerText,
        endDate: row.children[4].innerText,
    };

    // 모달의 입력 필드에 데이터 채우기
    document.getElementById('edit-task-id').value = task.id;
    document.getElementById('edit-taskName').value = task.taskName;
    document.getElementById('edit-assignee').value = task.assignee;
    document.getElementById('edit-status').value = task.status;
    document.getElementById('edit-startDate').value = task.startDate;
    document.getElementById('edit-endDate').value = task.endDate;

    // 수정 모달 열기
    document.getElementById('edit-wbs-modal').style.display = 'block';
    console.log('수정 모달 열림:', task);
}

// 작업 삭제 처리
function handleDeleteTask(taskId) {
    console.log('삭제된 작업 처리 시작:', taskId);

    // WBS 테이블에서 작업 삭제
    const rowToDelete = document.querySelector(`#wbs-row-${taskId}`);
    if (rowToDelete) {
        console.log('WBS 테이블에서 작업 삭제:', taskId);
        rowToDelete.remove();
    } else {
        console.warn('WBS 테이블에서 작업을 찾을 수 없습니다:', taskId);
    }

    // Gantt Chart 전체 초기화 및 재렌더링
    resetAndRenderGanttChart();
    console.log('삭제된 작업 처리 완료 및 차트 초기화 완료:', taskId);
}





// 작업 삭제 요청
async function deleteTask(taskId) {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    try {
        const response = await fetch(`/api/wbs/${taskId}`, { method: 'DELETE' });
        if (!response.ok) {
            throw new Error(`작업 삭제 실패: ${response.status}`);
        }

        console.log('작업 삭제 성공:', taskId);

        // UI에서 즉시 반영
        handleDeleteTask(taskId);

        // WebSocket 메시지가 오기 전에 바로 반영
        resetAndRenderGanttChart();
    } catch (error) {
        console.error('작업 삭제 중 오류 발생:', error);
    }
}



document.getElementById('add-wbs-btn').addEventListener('click', () => {
    closeAddWbsModal(); // 다른 모달 닫기
    const addWbsModal = document.getElementById('add-wbs-modal');
    addWbsModal.style.display = 'block'; // 추가 모달 열기
    console.log('작업 추가 모달 열림');
});


// 작업 추가 요청
document.getElementById('add-wbs-form').addEventListener('submit', async (event) => {
    event.preventDefault(); // 기본 폼 제출 방지

    const task = {
        projectId: document.getElementById('project-id').value,
        taskName: document.getElementById('taskName').value,
        assignee: document.getElementById('assignee').value,
        status: document.getElementById('status').value,
        startDate: document.getElementById('startDate').value,
        endDate: document.getElementById('endDate').value
    };

    // 시작일과 종료일 유효성 검사
    if (new Date(task.startDate) > new Date(task.endDate)) {
        alert('시작일은 종료일보다 늦을 수 없습니다.');
        return;
    }

    try {
        const response = await fetch('/api/wbs', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(task),
        });

        if (!response.ok) {
            throw new Error(`작업 추가 실패: ${response.status}`);
        }

        console.log('작업 추가 성공');
        fetchWbsData(); // 새로 추가된 작업 포함 전체 데이터 다시 가져오기
        resetAddWbsForm(); // 폼 초기화
        closeAddWbsModal(); // 모달 닫기
    } catch (error) {
        console.error('작업 추가 중 오류 발생:', error);
    }
});



function resetAddWbsForm() {
    const form = document.getElementById('add-wbs-form');
    if (form) {
        form.reset(); // 입력 필드 초기화
    }
}


// 작업 수정 요청
document.getElementById('edit-wbs-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const task = {
        id: document.getElementById('edit-task-id').value,
        taskName: document.getElementById('edit-taskName').value,
        assignee: document.getElementById('edit-assignee').value,
        status: document.getElementById('edit-status').value,
        startDate: document.getElementById('edit-startDate').value,
        endDate: document.getElementById('edit-endDate').value
    };

    // 시작일과 종료일 유효성 검사
    if (new Date(task.startDate) > new Date(task.endDate)) {
        alert('시작일은 종료일보다 늦을 수 없습니다.');
        return;
    }

    try {
        const response = await fetch('/api/wbs', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(task),
        });

        if (!response.ok) {
            throw new Error(`작업 수정 실패: ${response.status}`);
        }

        const updatedTask = await response.json();
        console.log('수정 성공:', updatedTask);

        handleAddOrUpdateTask(updatedTask); // 수정된 작업 반영
        closeEditWbsModal(); // 수정 모달 닫기
    } catch (error) {
        console.error('작업 수정 중 오류 발생:', error);
    }
});



function closeAddWbsModal() {
    const addWbsModal = document.getElementById('add-wbs-modal');
    if (addWbsModal) {
        addWbsModal.style.display = 'none';
    }
}

function closeEditWbsModal() {
    const editWbsModal = document.getElementById('edit-wbs-modal');
    if (editWbsModal) {
        editWbsModal.style.display = 'none';
    }
}

function closeAllModals() {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        modal.style.display = 'none';
    });
}

