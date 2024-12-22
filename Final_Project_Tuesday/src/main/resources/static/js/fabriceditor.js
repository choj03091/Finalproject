const canvas = new fabric.Canvas('fabricCanvas');
let currentColor = '#ff0000';

function addRectangle() {
    const rect = new fabric.Rect({
        left: 100,
        top: 100,
        fill: currentColor,
        width: 100,
        height: 100,
        selectable: true,
    });
    canvas.add(rect);
}

function addCircle() {
    const circle = new fabric.Circle({
        left: 300,
        top: 100,
        fill: currentColor,
        radius: 50,
        selectable: true,
    });
    canvas.add(circle);
}

function addText() {
    const text = new fabric.IText('Edit me', {
        left: 200,
        top: 200,
        fill: currentColor,
        fontSize: 30,
        selectable: true,
    });
    canvas.add(text);
}

function updateColor(color) {
    currentColor = color;
    const activeObject = canvas.getActiveObject();
    if (activeObject) {
        activeObject.set('fill', color);
        canvas.renderAll();
    }
}

function deleteSelected() {
    const activeObject = canvas.getActiveObject();
    if (activeObject) {
        canvas.remove(activeObject);
    }
}

function saveAsPDF() {
    const canvasData = canvas.toDataURL({ format: 'png' });
    const { jsPDF } = window.jspdf;
    const pdf = new jsPDF();

    pdf.addImage(canvasData, 'PNG', 10, 10, 190, 140);
    pdf.save('canvas.pdf');
}

function saveAsPPT() {
    const canvasData = canvas.toDataURL({ format: 'png' });
    const pptx = new PptxGenJS();

    const slide = pptx.addSlide();
    slide.addImage({ data: canvasData, x: 0.5, y: 0.5, w: 9, h: 5 });
    pptx.writeFile('canvas.pptx');
}

function saveAsWord() {
    const canvasData = canvas.toDataURL({ format: 'png' });
    const doc = new docx.Document({
        sections: [{
            properties: {},
            children: [
                new docx.Paragraph({
                    children: [
                        new docx.ImageRun({
                            data: canvasData.split(',')[1],
                            transformation: {
                                width: 600,
                                height: 400,
                            },
                        }),
                    ],
                }),
            ],
        }],
    });

    docx.Packer.toBlob(doc).then(blob => {
        saveAs(blob, "canvas.docx");
    });
}