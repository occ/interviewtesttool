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

addMarkers = (response) =>
  clearMarkers()
  data = response.responseJSON
  submitButton = $("#btn-submit")

  if data.success
    submitButton.removeAttr "disabled"
    return

  submitButton.attr "disabled", "disabled"
  codeIssues = $("#code-issues")
  i = 0

  while i < data.errors.length
    error = data.errors[i]
    range = new Range(error.startLine, error.startColumn, error.endLine, error.endColumn)
    @markers.push @editorSession.addMarker(range, "squiggly", "typo", true)
    kind = "warning"
    kind = "danger"  if error.kind is "ERROR"
    row = $("<tr></tr>").addClass(kind).append($("<td></td>").text(error.startLine), $("<td></td>").text(error.startColumn), $("<td></td>").text(error.message))
    codeIssues.append row
    i++
  null

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
    complete: [addMarkers, callback]


processTestResults = =>
  console.log arguments_

submitSolution = =>
  compile ->
    $.ajax
      type: "POST"
      url: testUrl
      data:
        code: @editor.getValue()

      dataType: "json"
      success: processTestResults()

$ ->
  $("#btn-submit").click submitSolution
