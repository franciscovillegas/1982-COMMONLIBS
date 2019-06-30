function fechaValida(dia, mes, agno) {
	var valido = false;
	mes = mes - 1;
	var fecha = new Date(agno, mes, dia);
	if ((dia == fecha.getDate()) && (mes == fecha.getMonth()) && !(dia == fecha.getFullYear()))
		valido = true;
	return valido;
}