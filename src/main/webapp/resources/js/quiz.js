var easyButton = $("#easyButton")
var mediumButton = $("#mediumButton")
var hardButton = $("#hardButton")
var buttonSubjects = $(".buttonSubjects")
var buttonOptions = $(".buttonOptions")

var easySubjectsForm = $("#easySubjectsForm")
var mediumSubjectsForm = $("#mediumSubjectsForm")
var hardSubjectsForm = $("#hardSubjectsForm")
var exerciseForm = $("#exerciseForm")

easyButton.click(function() {
	easyButton.prop("disabled", true)
	mediumButton.prop("disabled", false)
	hardButton.prop("disabled", false)
	easySubjectsForm.fadeIn()
	mediumSubjectsForm.fadeOut()
	hardSubjectsForm.fadeOut()
	mediumSubjectsForm.css("display", "none")
	hardSubjectsForm.css("display", "none")
})

mediumButton.click(function() {
	easyButton.prop("disabled", false)
	mediumButton.prop("disabled", true)
	hardButton.prop("disabled", false)
	easySubjectsForm.fadeOut()
	mediumSubjectsForm.fadeIn()
	hardSubjectsForm.fadeOut()
	easySubjectsForm.css("display", "none")
	hardSubjectsForm.css("display", "none")
})

hardButton.click(function() {
	easyButton.prop("disabled", false)
	mediumButton.prop("disabled", false)
	hardButton.prop("disabled", true)
	easySubjectsForm.fadeOut()
	mediumSubjectsForm.fadeOut()
	hardSubjectsForm.fadeIn()
	easySubjectsForm.css("display", "none")
	mediumSubjectsForm.css("display", "none")
})

buttonOptions.click(function() {
	console.log("click")
})

button.click(function() {
	console.log("click")
})