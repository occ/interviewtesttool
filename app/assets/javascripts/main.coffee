@markers = []
@compilerTimer = null
@editor = ace.edit(document.getElementById("code-area"))

@editor.setTheme "ace/theme/chrome"
@editorSession = editor.getSession()
@editorSession.setMode "ace/mode/java"
@editorSession.setUseSoftTabs true
@editor.on "change", =>
  clearTimeout compilerTimer if @compilerTimer?
  @compilerTimer = setTimeout(compile, 100)

Range = ace.require("ace/range").Range

addMarkers = (data) =>
  clearMarkers()
  submitButton = $("#btn-submit")

  if data.success
    submitButton.removeAttr "disabled"
    return

  submitButton.attr "disabled", "disabled"
  codeIssues = $("#code-issues")
  i = 0

  while i < data.issues.length
    error = data.issues[i]
    range = new Range(error.startLine, error.startColumn, error.endLine, error.endColumn)
    @markers.push @editorSession.addMarker(range, "squiggly", "typo", true)
    kind = "warning"
    kind = "danger"  if error.kind is "ERROR"
    row = $("<tr></tr>").addClass(kind).append(
      $("<td></td>").text(error.startLine),
      $("<td></td>").text(error.startColumn),
      $("<td></td>").text(error.message))
    codeIssues.append row
    i++
  null

addTestFailure = (issue) =>
  row = $("<tr></tr>").addClass("warning").append(
    $("<td></td>").text("TEST"),
    $("<td></td>").text(""),
    $("<td></td>").text(issue.exceptionType + ": " + issue.message))
  $("#code-issues").append row

clearMarkers = =>
  i = 0

  while i < @markers.length
    @editorSession.removeMarker @markers[i]
    i++
  $("#code-issues").empty()
  @markers = []

compile = (callback) =>
  $.ajax
    type: "POST"
    url: compileUrl
    data:
      code: @editor.getValue()

    dataType: "json"
    success: [addMarkers, callback]


processTestResults = (result) =>
  if result.success
    $("#code-issues").empty()
    $("#code-issues").append($("<tr></tr>").addClass("success").append(
      $("<td></td>").text(""),
      $("<td></td>").text(""),
      $("<td></td>").text("All tests passed!")
    ))
    return

  addTestFailure issue for issue in result.issues

submitSolution = =>
  compile =>
    $.ajax
      type: "POST"
      url: testUrl
      data:
        code: @editor.getValue()

      dataType: "json"
      success: processTestResults

$ ->
  $("#btn-submit").click submitSolution
