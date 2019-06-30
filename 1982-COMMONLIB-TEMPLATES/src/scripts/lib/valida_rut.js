function checkDigitoV(solorut, digit)
{
  var dv = digit;
  if ( checkCDV(dv) != true )
	return false;
  var dvr = '0';
      suma = 0;
 	  mul  = 2;
  
  for (i= solorut.length -1 ; i >= 0; i--)
  {
	suma = suma + solorut.charAt(i) * mul;
	if (mul == 7)
  	  mul = 2;
	else    
	mul++;
  }	
  res = suma % 11;
  if (res==1) dvr = 'k';
  else 
   if (res==0) dvr = '0';
   else
   {
	dvi = 11-res;
	dvr = dvi + "";
   }
  if ( dvr != digit.toLowerCase() )
	return(false);
  else
	return(true);
}

function checkCDV (dvr)
{
 dv1 = dvr + "";
if ( dv1 != '0' && dv1 != '1' && dv1 != '2' && dv1 != '3' && dv1 != '4' && dv1 != '5' && dv1 != '6' && dv1 != '7' && dv1 != '8' && dv1 != '9' && dv1 != 'k'  && dv1 != 'K')
return false;
return true;
}