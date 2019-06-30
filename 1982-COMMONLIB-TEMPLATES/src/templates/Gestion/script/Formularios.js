function VerContFormm(form) {
	var msg = "";
	for (i = 0;i<form.elements.length; i++)
	msg  += form.elements[i].name + "=" + form.elements[i].value + "\n";
	alert(msg)
}