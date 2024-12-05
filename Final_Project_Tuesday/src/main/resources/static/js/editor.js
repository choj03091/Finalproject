//html 저장
document.getElementById('save-html-btn').addEventListener('click', () => {
    const content = tinymce.get('editor').getContent(); // 에디터 내용 가져오기
    const blob = new Blob([content], { type: 'text/html' }); // HTML 형식으로 Blob 생성
    const url = URL.createObjectURL(blob);

    // 가상의 다운로드 링크 생성
    const a = document.createElement('a');
    a.href = url;
    a.download = 'document.html'; // 저장할 파일 이름
    document.body.appendChild(a);
    a.click(); // 링크 클릭으로 다운로드 실행
    document.body.removeChild(a);
    URL.revokeObjectURL(url); // 메모리 해제
});

//pdf 저장
document.getElementById('save-pdf-btn').addEventListener('click', () => {
    const content = tinymce.get('editor').getContent(); // 에디터 내용 가져오기

    const doc = new jsPDF();
    doc.html(content, {
        callback: function (pdf) {
            pdf.save('document.pdf'); // 저장할 파일 이름
        }
    });
});

//word 저장
document.getElementById('save-docx-btn').addEventListener('click', () => {
    const content = tinymce.get('editor').getContent(); // 에디터 내용 가져오기

    const blob = new Blob(
        [`<html><head></head><body>${content}</body></html>`],
        { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' }
    );

    const url = URL.createObjectURL(blob);

    // 가상의 다운로드 링크 생성
    const a = document.createElement('a');
    a.href = url;
    a.download = 'document.docx'; // 저장할 파일 이름
    document.body.appendChild(a);
    a.click(); // 다운로드 실행
    document.body.removeChild(a);
    URL.revokeObjectURL(url); // 메모리 해제
});

// json 저장
document.getElementById('save-json-btn').addEventListener('click', () => {
    const content = tinymce.get('editor').getContent(); // 에디터 내용 가져오기
    const data = { content: content, savedAt: new Date().toISOString() }; // 추가 데이터 포함

    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = 'document.json';
    document.body.appendChild(a);
    a.click(); // 다운로드 실행
    document.body.removeChild(a);
    URL.revokeObjectURL(url); // 메모리 해제
});