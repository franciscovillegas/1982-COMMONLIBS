function checkMarcarTodos(check, estado) {
	var i;
	var largo = eval("check.length");
	if (largo) 	{
		for(i=0; i<largo; i++)
			check[i].checked = estado;
	}
	else {
		check.checked = estado;
	}
}