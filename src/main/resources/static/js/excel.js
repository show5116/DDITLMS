document.write("<script src='https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.15.5/xlsx.full.min.js'><"+"/script>");
document.write("<script src='https://cdnjs.cloudflare.com/ajax/libs/FileSaver.js/1.3.8/FileSaver.min.js'><"+"/script>");
function exportExcel(excelData){
    let excelHandler = {
        getExcelFileName : function (){
            return excelData.fileName;
        },
        getSheetName : function (){
            return excelData.sheetName;
        },
        getExcelData : function (){
            return excelData.excelDatas;
        },
        getWorksheet : function (){
            return XLSX.utils.aoa_to_sheet(this.getExcelData());
        }
    };
    let workbook = XLSX.utils.book_new();
    let newWorkSheet = excelHandler.getWorksheet();
    XLSX.utils.book_append_sheet(workbook,newWorkSheet,excelHandler.getSheetName());
    let wbout = XLSX.write(workbook,{bookType:'xlsx',type:'binary'});
    saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}),excelHandler.getExcelFileName());
}

function exportsExcel(excelData){
    let excelHandler = {
        getExcelFileName : function (){
            return excelData.fileName;
        },
        getSheetName : function (){
            return excelData.sheetName;
        },
        getExcelData : function (){
            return excelData.excelDatas;
        },
        getWorksheet : function (){
            return XLSX.utils.aoa_to_sheet(this.getExcelData());
        }
    };
    let workbook = XLSX.utils.book_new();
    let newWorkSheet = excelHandler.getWorksheet();
    XLSX.utils.book_append_sheet(workbook,newWorkSheet,excelHandler.getSheetName());
    let wbout = XLSX.write(workbook,{bookType:'xlsx',type:'binary'});
    saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}),excelHandler.getExcelFileName());
}

function s2ab(s){
    var buf = new ArrayBuffer(s.length);
    var view = new Uint8Array(buf);
    for(var i=0; i<s.length;i++) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
}

