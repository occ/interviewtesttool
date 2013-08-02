var editor = ace.edit(document.getElementById("code-area"));
editor.setTheme("ace/theme/chrome");

var editorSession = editor.getSession();
editorSession.setMode("ace/mode/java");
editorSession.setUseSoftTabs(true);

editor.on("change", function() {
    if (compilerTimer != null) {
        clearTimeout(compilerTimer);
    }

    compilerTimer = setTimeout(compile, 100);
});

var Range = ace.require('ace/range').Range;

var markers = [];
var compilerTimer = null;

var addMarkers = function(response) {
    clearMarkers();

    data = response.responseJSON;
    if (data.success)
        return;

    for (var i = 0; i < data.errors.length; i++) {
        var error = data.errors[i];
        var range = new Range(error.startLine, error.startColumn, error.endLine, error.endColumn);
        markers.push(editorSession.addMarker(range, "squiggly", "typo", true));
    }
}

var clearMarkers = function () {
    for (var i = 0; i < markers.length; i++) {
        editorSession.removeMarker(markers[i]);
    }
    markers = [];
}

var compile = function(callback) {
    $.ajax({
        type: "POST",
        url: compileUrl,
        data: {code: editor.getValue()},
        dataType: 'json',
        complete: [addMarkers, callback]
    });
};

var processTestResults = function () {
    console.log(arguments)
};

var submitSolution = function() {
    compile(function () {
        $.ajax({
            type: "POST",
            url: testUrl,
            data: {code: editor.getValue()},
            dataType: 'json',
            success: processTestResults()
        });
    });
};

$(function() {
    $("#btn-submit").click(submitSolution);
});