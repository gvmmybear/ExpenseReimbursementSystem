// author: Christian Castro
// Last Edited: 05/05/2022

let i;
let roleId;
let columns;
let objValArray;
let uniqueRowID;
let reimbursementRequests;
window.onload = function(){
    console.log("window has loaded!");
    console.log("loading user credentials")
    loadUserCredentials();
    console.log("finished loading user credentials!")
};

/* ################## AJAX FUNCTIONS ################## */
/**
 * @name loadUserCredentials
 * Makes http request to server by get request to load user account 
 * information, then stores as a variable to determine which user
 * table will be displayed as well as the information for the user 
 * during their session.
 */
function loadUserCredentials(){
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function(){
        if(httpRequest.readyState == 4 && httpRequest.status == 200){
            let userRoleId = JSON.parse(httpRequest.responseText);
            roleId = userRoleId;
            getRequests(roleId);
            document.getElementById("refresh").addEventListener("click", refreshTable);
            if(roleId != 4){
                document.getElementById("addReqButton").addEventListener("click", addRequest);
            };
        };
    };
    httpRequest.open("POST", "http://localhost:8080/project1/useraccount");
    httpRequest.setRequestHeader("content-type", "application/json");
    httpRequest.send();
};

/**
 * @name getRequests
 * @param {number} roleId 
 * Makes http get request to server to obtain reimbursement requests, based on user's
 * role ID number. Also calls on createTable to generate table specific to user role ID.
 */
function getRequests(roleId){
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function(){
        if(httpRequest.readyState == 4 && httpRequest.status == 200){
            reimbursementRequests = JSON.parse(httpRequest.responseText);
            createTable(reimbursementRequests, roleId);
        };
    };
    httpRequest.open("GET", "http://localhost:8080/project1/viewrequests");
    httpRequest.send();
};

/**
 * @name addRequest
 * @param {*} event 
 * Adds a new reimbursement request via http post request.
 */
function addRequest(event){
    event.preventDefault();
    let reqAmount = document.getElementById("amount").value;
    let reqDescription = document.getElementById("textbox").value;
    let reqTypeId = document.getElementById("typeId").value;
    let jsonObject = {
        "reqAmount" : reqAmount,
        "reqDescription" : reqDescription,
        "reqTypeId" : reqTypeId
    };
   
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function(){
        if(httpRequest.readyState == 4 && httpRequest.status == 200){
            let request = JSON.parse(httpRequest.responseText);
            console.log("request: ",request);
            reimbursementRequests.push(request);
            console.log("request list: ", reimbursementRequests);
            appendRow();
        };
    };
    httpRequest.open("POST", "http://localhost:8080/project1/submitrequest");
    httpRequest.setRequestHeader("content-type", "application/json");
    httpRequest.send(JSON.stringify(jsonObject));
};

/**
 * @name updateRequest
 * @param {*} object 
 * @param {*} newStatus 
 * Updates reimbursement request via put request.
 */
function updateRequest(object, newStatus){
   let request = JSON.parse(object.value);
   let jsonObject = {
        "reqId" : request["reqId"],
        "reqStatus" : newStatus,
        "reqStatusId" : request["reqStatusId"]
    };

    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function(){
        if(httpRequest.readyState == 4 && httpRequest.status == 200){
            let request = JSON.parse(httpRequest.responseText);
            updateTable(request);
        };
    };
    httpRequest.open("PUT", "http://localhost:8080/project1/updaterequest");
    httpRequest.setRequestHeader("content-type", "application/json");
    httpRequest.send(JSON.stringify(jsonObject));
    
};
/////////////////////////////////END OF AJAX/////////////////////////////////

/* ######################### TABLE FUNCTIONS ######################### */
/**
 * @name createTable
 * @param {*} requests
 * Creates a table to display reimbursement requests for user. 
 */
function createTable(requests){
    let myTable = document.querySelector("#myTable");
    let headerRow = document.querySelector("#headerRow");
    let tableBody = document.querySelector("#tableBody");
    createTableCaption();
    createHeaderColumns(headerRow);
    generateTableRows(tableBody, requests);
};

/**
 * @name refreshTable
 * Refreshes table by calling on helper functions to empty table
 * then getRequests().
 */
function refreshTable(event){
    event.preventDefault();
    emptyTable();
    getRequests();
};

/**
 * @name emptyTable
 * Empties request table of all child elements.
 */
function emptyTable(){
    let tableCaption = document.getElementById("myTableCaption");
    tableCaption.removeChild(tableCaption.firstChild);
    let table = document.getElementById("tableBody");
    while (table.firstChild) {
        table.removeChild(table.firstChild);
    };
    let headerRow = document.getElementById("headerRow");
    while (headerRow.firstChild) {
        headerRow.removeChild(headerRow.firstChild);
    };
};

/**
 * @name updateTable
 * @param {*} updatedRow 
 * Updates table with new requests.
 */
function updateTable(updatedRow){
    for(let index=0; index < reimbursementRequests.length; index++){
        if(updatedRow["reqId"]==reimbursementRequests[index]["reqId"]){
            reimbursementRequests[index] = updatedRow;
            emptyTable();
            createTable(reimbursementRequests, roleId);
        };
    };
};

/**
 * @name filterRequests
 * @param {*} filterBy 
 * filters request table by parameter: "Pending", "Approved", "Denied".
 */
function filterRequests(filterBy){
    console.log("filter function called")
    let filteredArray = reimbursementRequests.filter(object => {
        return object["reqStatus"] == filterBy;
    });
    emptyTable();
    createTable(filteredArray, roleId);
};

// ------- create/update Helpers -------
/**
 * @name createTableCaption
 * function creates and appends a new table caption into existing 
 * caption element.
 */
function createTableCaption(){
    let caption = document.querySelector("#myTableCaption");
    let captionText = document.createTextNode("Reimbursement Requests");
    caption.appendChild(captionText);
};

/**
 * @name createHeaderColumns
 * @param {*} headerRow 
 * creates and appends header columns to existing table body.
 */
function createHeaderColumns(headerRow){
    // table column titles are prestored in an array
    let lastColumn;
    if(roleId != 4){
        lastColumn = "Resolved By";
    }else if(roleId == 4) {
        lastColumn = "Requested By";
    };
    let columnHeaderArray = [
        "Reimbursement ID Number", 
        "Reimbursement Amount (US Dollars)",
        "Request Submission Timestamp", 
        "Request Resolved Timestamp", 
        "Description", 
        "Reimbursement Type", 
        "Request Status",
        lastColumn
    ];
    if(roleId == 4) {
        columnHeaderArray.push("Approve/Deny");
    }
    // then table column titles are added to the table in parent HTML doc with loop.
    for(let i=0; i<columnHeaderArray.length; i++){
        let column = document.createElement("th");
        let columnTitle = document.createTextNode(columnHeaderArray[i]);
        column.setAttribute("id", "column" + i);
        column.appendChild(columnTitle);
        headerRow.appendChild(column);
    };
};

/**
 * @name generateTableRows
 * @param {*} tableBody 
 * @param {*} requests 
 * creates and appends new table rows with data values from getRequests().
 */
function generateTableRows(tableBody, requests){
    // makes new row for length of request query array
    for(let i=0; i<requests.length; i++){
        let row = document.createElement("tr");
        row.setAttribute("id", "row" + i);
        tableBody.appendChild(row);
    };
    // TODO: ideally this code block below should be refactored so I can just call on appendRow()
    objValArray = makeRowArray();
    uniqueRowID = 0;
    columns = roleId == 4 ? objValArray.length : objValArray.length - 1;
    // for each new row that was made gets the row id
    for(i=0; i<requests.length; i++){
        let row = document.querySelector("#row" + i);
        let rowRequestObj = requests[i]
        // then creates new table cell for each table column and adds data value
        for(let j=0; j < (columns); j++){
            let rowCell;
            if(j==4){ //data-target="#myModal"
                rowCell = document.createElement("div");
                rowCell.setAttribute("id", "rowCell" + j + uniqueRowID);
                rowCell.setAttribute("data-target", "#myModal");
                rowCell.setAttribute("onclick", "modalWindow(rowCell"+j+uniqueRowID+")");
                rowCell.setAttribute("data-toggle", "modal");
                rowCell.setAttribute("class", "description");
                uniqueRowID++;
            }else{
                rowCell = document.createElement("td");
                rowCell.setAttribute("id", "rowCell" + j);
            }
            // quick check on index value (mainly for last two indices for first & last name)
            if(j < (objValArray.length - 2)){
                let rowValue;
                if(rowRequestObj[objValArray[j]] != null){
                    rowValue = document.createTextNode(rowRequestObj[objValArray[j]]);
                }else{
                    rowValue = document.createTextNode("")
                };
                rowCell.appendChild(rowValue);
            }else if(j == objValArray.length - 2 && (rowRequestObj[objValArray[j]] != null)){ 
                let rowValue = document.createTextNode(
                    rowRequestObj[objValArray[j]] + " " + rowRequestObj[objValArray[j+1]]
                );
                rowCell.appendChild(rowValue);
            }else if(roleId == 4){
                let buttonDiv = makeTableButtons(i, requests);
                rowCell.appendChild(buttonDiv);
            };
            row.appendChild(rowCell);
        };
    };
};

/**
 * @name generateEmtpyRow
 * creates an empty row and appends to preexisting table body.
 */
function generateEmtpyRow(){
    let tableBody = document.querySelector("#tableBody");
    let newRow = document.createElement("tr");
    newRow.setAttribute("id", "row" + i);
    tableBody.appendChild(newRow);
};

/**
 * @name makeTableButtons
 * @param {*} i 
 * @param {*} requests 
 * @returns buttonDiv
 * 
 * create button element for request table to user friendly 
 * approve/deny action to update request object.
 */
function makeTableButtons(i, requests){
    let buttonClass = "btn btn-primary btn-sm";
    let buttonDiv = document.createElement("div");
    let approveButton = document.createElement("Button");
    let pgBreak = document.createElement("Br");
    let denyButton = document.createElement("Button");
    let approve = document.createTextNode("Approve")
    let deny = document.createTextNode("Deny");
    approveButton.setAttribute("class", buttonClass);
    denyButton.setAttribute("class", buttonClass);
    approveButton.setAttribute("value", `${JSON.stringify(requests[i])}`);
    denyButton.setAttribute("value", `${JSON.stringify(requests[i])}`);
    approveButton.setAttribute("id", "approve" + i);
    denyButton.setAttribute("id", "deny" + i);
    approveButton.setAttribute("onclick", `updateRequest(approve${i}, "Approved")`);
    denyButton.setAttribute("onclick", `updateRequest(deny${i}, "Denied")`);
    approveButton.appendChild(approve);
    denyButton.appendChild(deny);
    buttonDiv.appendChild(approveButton);
    buttonDiv.appendChild(pgBreak);
    buttonDiv.appendChild(denyButton);
    return buttonDiv;
};

/**
 * @name makeRowArray
 * @returns 
 * Makes title column row based on user role ID number.
 */
function makeRowArray(){
    let firstName;
    let lastName;
    if(roleId != 4){
        firstName = "resolverFirstName";
        lastName = "resolverLastName"
    }else if(roleId == 4){
        firstName = "requesterFirstName";
        lastName = "requesterLastName";
    };

    let objValArray = [
        "reqId", "reqAmount", "reqSubmissionTime", "reqResolvedTime", "reqDescription",
        "reqType", "reqStatus", firstName, lastName
    ];
    return objValArray;
};

/**
 * @name appendRow
 * Appends new row with a newly added reimbursement request.
 */
function appendRow(){
    generateEmtpyRow();

    let row = document.querySelector("#row" + i);
    let rowRequestObj = reimbursementRequests[i]
        // then creates new table cell for each table column and adds data value
        for(let j=0; j < (columns); j++){
            let rowCell;
            if(j==4){ //data-target="#myModal"
                rowCell = document.createElement("div");
                rowCell.setAttribute("id", "rowCell" + j + uniqueRowID);
                rowCell.setAttribute("data-target", "#myModal");
                rowCell.setAttribute("onclick", "modalWindow(rowCell"+j+uniqueRowID+")");
                rowCell.setAttribute("data-toggle", "modal");
                rowCell.setAttribute("class", "description");
                uniqueRowID++;
            }else{
                rowCell = document.createElement("td");
                rowCell.setAttribute("id", "rowCell" + j);
            }
            // quick check on index value (mainly for last two indices for first & last name)
            if(j < (objValArray.length - 2)){
                let rowValue;
                if(rowRequestObj[objValArray[j]] != null){
                    rowValue = document.createTextNode(rowRequestObj[objValArray[j]]);
                }else{
                    rowValue = document.createTextNode("")
                };
                rowCell.appendChild(rowValue);
            }else if(j == objValArray.length - 2 && (rowRequestObj[objValArray[j]] != null)){ 
                console.log(j);
                let rowValue = document.createTextNode(
                    rowRequestObj[objValArray[j]] + " " + rowRequestObj[objValArray[j+1]]
                );
                rowCell.appendChild(rowValue);
            }else if(roleId == 4){
                let buttonDiv = makeTableButtons(i, requests);
                rowCell.appendChild(buttonDiv);
            };
            row.appendChild(rowCell);
        };
    i++;
};

/**
 * @name modalWindow
 * @param {*} value 
 * Executes DOM manipulation on modal window when user clicks on
 * description box in table.
 */
function modalWindow(value){
    let modal = document.getElementById("modalText");
    let text = modal.childNodes[0];
    let newText = document.createTextNode(value.childNodes[0]["nodeValue"]);
    modal.removeChild(text);
    modal.appendChild(newText);
};
// end of table helpers
////////////////////////END OF TABLE FUNCTIONS////////////////////////


// ##################### USER INPUT VALIDATION #####################
// finish this if possible

/* misc depricated functions (not in use I think) */
function addEvents(requests){
    for(let i=0; i<requests.length; i++){
        document.getElementById("approve" + i).addEventListener("click", updateRequest);
        document.getElementById("deny" + i).addEventListener("click", updateRequest);
    };
};

let reqStatus;
function approveStatus(){
    reqStatus = "Approved";
    
};
function denyStatus(){
    reqStatus = "Denied";
};

function printRequestAttributes(){
    for(let i=0; i<requests.length; i++){
        console.log(requests[i].reqId);
        console.log(requests[i].reqAmount);
        console.log(requests[i].reqSubmissionTime);
        console.log(requests[i].reqResolvedTime);
        console.log(requests[i].reqDescription);
        console.log(requests[i].reqType);
        console.log(requests[i].reqStatus);
        console.log(requests[i].resolverFirstName, " ", requests[i].resolverLastName);
    };
};
